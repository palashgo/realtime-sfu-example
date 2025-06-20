.PHONY: all
all: build

.PHONY: build
build:
	protoc proto/schema.proto --go_out=. ||:
	go build -o ws-demo ./cmd/main
