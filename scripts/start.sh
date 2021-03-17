#!/usr/bin/env bash

SRC="./examples/space.rs"

if [ -n "$1" ]; then
    SRC=$1
fi

java -Dfile.encoding=UTF-8 \
 -p lib/rsvm-0.9-SNAPSHOT.jar:lib/antlr4-runtime-4.9.1.jar \
 -m rs.rsvm/rs.RsVmLauncher $SRC