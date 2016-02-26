#!/bin/sh

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

mkdir -p $DIR/../deploy
CONTAINER_ID=`docker run --name "go-cd" -d -e "JVM_DEBUG=Y" -p 8153:8153 -p 5005:5005 -v $DIR/../deploy:/var/lib/go-server/plugins/external/ thoughtworks/strongauth-tests`
echo $CONTAINER_ID > $DIR/../.strong-auth-test-id
cp $DIR/../../build/libs/strong-auth-1.0-SNAPSHOT.jar $DIR/../deploy/