#!/bin/bash

protoc -I=./Server/src/main/java --java_out=./Server/src/main/java/ --proto_path=./src/main/protobuf ./src/main/protobuf/JCoincheProtocol.proto
