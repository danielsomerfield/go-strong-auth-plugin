#!/bin/sh

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ -e $DIR/../.strong-auth-test-id ]; then
    CONTAINER_ID=`cat $DIR/../.strong-auth-test-id`
    docker kill $CONTAINER_ID
    docker rm $CONTAINER_ID
    rm $DIR/../.strong-auth-test-id
fi

