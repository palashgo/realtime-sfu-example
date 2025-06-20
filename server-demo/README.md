# Websocket Demo

Websocket in/out demo for Realtime SFU

## Websocket In

WebSocket In can be used to consume the audio of the participant present in a given meeting. This only works when the other participant is already present in the meeting

1. Make a POST request to the Realtime SFU. Replace `<MEETING_ID>` with the ID of a RealtimeKit meeting, and `<CHANNEL_ID>` with any arbitary identifier, eg. `wsDemoConsume`

```sh
$ curl -X POST https://media-hub-production.realtime.cloudflare.com/rooms/<MEETING_ID>/ws/<CHANNEL_ID>/in
```

2. Visit https://bridge.orange.cloudflare.dev/player and enter the WebSocket URL `wss://bridge.orange.cloudflare.dev/ws/<CHANNEL_ID>/subscribe` to start consuming the audio

## Websocket Out

WebSocket Out can be used to publish audio data to a meeting via a dummy participant. For the purpose of the demo, we will publish data from the `ogg` files in this repo

1. Install build dependencies

```sh
$ brew install protobuf protoc-gen-go libopusenc opus opusfile libsoxr
```

2. Start publishing audio data. Replace `<CHANNEL_ID>` with another arbitary identifier, eg `wsDemoProduce` and specify the file to publish

```sh
$ make
# ./ws-demo <CHANNEL_ID> <AUDIO_FILE>
$ ./ws-demo wsDemoProduce ./resources/PinkPanther60.ogg
```

3. Link up the `CHANNEL_ID` to the meeting. This will make a dummy participant called `Agent` join the meeting

```
$ curl -X POST https://media-hub-production.realtime.cloudflare.com/rooms/<MEETING_ID>/ws/<CHANNEL_ID>/out
```
