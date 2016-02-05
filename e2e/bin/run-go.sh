#!/bin/sh

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#$DIR/../gradlew jar copyBuild

#docker build --tag thoughtworks/strongauth-tests .
CONTAINER_ID=`docker run -d -p 8153:8153 thoughtworks/strongauth-tests`
echo $CONTAINER_ID > .strong-auth-test-id