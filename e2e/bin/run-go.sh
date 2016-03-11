#!/bin/bash

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

mkdir -p $DIR/../deploy
$DIR/stop-go.sh
docker run --name "go-cd" -d -e "JVM_DEBUG=Y" -p 8153:8153 -p 5005:5005 -v $DIR/../deploy:/var/lib/go-server/plugins/external/ thoughtworks/strongauth-tests
cp $DIR/../../build/libs/strong-auth-*.jar $DIR/../deploy/
