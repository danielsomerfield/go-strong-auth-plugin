#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#TODO: if the file exists but the container doesn't it should move on...
if [ -e $DIR/../.strong-auth-test-id ]; then
    CONTAINER_ID=`cat $DIR/../.strong-auth-test-id`
    docker kill $CONTAINER_ID
    docker rm $CONTAINER_ID
    rm $DIR/../.strong-auth-test-id
fi

