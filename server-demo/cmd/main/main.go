package main

import (
	"bytes"
	"errors"
	"fmt"
	"github.com/gorilla/websocket"
	"github.com/zaf/resample"
	"google.golang.org/protobuf/proto"
	"gopkg.in/hraban/opus.v2"
	"io"
	"os"
	"time"
)

func Int16ToByteSlice(payload []int16) []byte {
	pcmBytes := make([]byte, len(payload)*2)
	for i, sample := range payload {
		pcmBytes[i*2] = byte(sample & 0xff)
		pcmBytes[i*2+1] = byte(sample >> 8)
	}
	return pcmBytes
}

func main() {
	file, err := os.Open(os.Args[2])
	if err != nil {
		panic(err)
	}

	conn, _, err := websocket.DefaultDialer.Dial(fmt.Sprintf("wss://bridge.orange.cloudflare.dev/ws/%s/publish", os.Args[1]), nil)
	if err != nil {
		panic(err)
	}
	defer conn.Close()

	stream, err := opus.NewStream(file)
	if err != nil {
		panic(err)
	}

	var timestamp uint32
	var seqNo uint16

	channels := 1
	pcm := make([]int16, 16384)

	buf := bytes.NewBuffer(nil)
	// The server expects 48khz audio
	resampler, err := resample.New(buf, 24000, 48000, 1, resample.I16, resample.Quick)
	if err != nil {
		panic(err)
	}

	ticker := time.NewTicker(time.Millisecond * 20)
	for {
		<-ticker.C

		n, err := stream.Read(pcm)
		if errors.Is(err, io.EOF) {
			fmt.Println("Resetting stream")
			if _, err := file.Seek(0, 0); err != nil {
				panic(err)
			}
			stream, err = opus.NewStream(file)
			if err != nil {
				panic(err)
			}
			continue
		} else if err != nil {
			panic(err)
		}

		sampleCount := n * channels

		buf.Reset()
		if _, err := resampler.Write(Int16ToByteSlice(pcm[:sampleCount])); err != nil {
			panic(err)
		}

		fmt.Println("sn", seqNo, "ts", timestamp)

		packet := Packet{
			SequenceNumber: uint32(seqNo),
			Timestamp:      timestamp,
			Payload:        buf.Bytes(),
		}
		payload, err := proto.Marshal(&packet)
		if err != nil {
			panic(err)
		}

		if err := conn.WriteMessage(websocket.BinaryMessage, payload); err != nil {
			panic(err)
		}

		timestamp += uint32(sampleCount)
		seqNo++
	}
}
