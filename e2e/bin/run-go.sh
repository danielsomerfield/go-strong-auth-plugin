#!/bin/sh

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#$DIR/../gradlew jar copyBuild

#docker build --tag thoughtworks/strongauth-tests .
mkdir -p $DIR/../deploy
CONTAINER_ID=`docker run -d -e "JVM_DEBUG=Y" -p 8153:8153 -p 5005:5005 -v $DIR/../deploy:/var/lib/go-server/plugins/external/ thoughtworks/strongauth-tests`
echo $CONTAINER_ID > .strong-auth-test-id
cp $DIR/../../build/libs/strong-auth-1.0-SNAPSHOT.jar $DIR/../deploy/