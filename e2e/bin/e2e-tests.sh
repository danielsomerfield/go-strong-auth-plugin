#!/bin/sh

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

$DIR/../gradlew jar copyBuild

docker build --tag thoughtworks/strongauth-tests .
CONTAINER_ID=`docker run -d -p 8153:8153 thoughtworks/strongauth-tests`

#run tests

#docker kill $CONTAINER_ID