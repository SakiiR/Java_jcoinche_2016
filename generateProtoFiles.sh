#!/bin/bash

echo "[^] Generating Google Protocol Buffers Files !"

echo -ne "\033[0;33m"
head -10 ./src/main/protobuf/JCoincheProtocol.proto
echo -ne "\033[0;0m"

protoc -I=./Server/src/main/java --java_out=./Server/src/main/java/ --proto_path=./src/main/protobuf ./src/main/protobuf/JCoincheProtocol.proto
protoc -I=./Client/src/main/java --java_out=./Client/src/main/java/ --proto_path=./src/main/protobuf ./src/main/protobuf/JCoincheProtocol.proto
