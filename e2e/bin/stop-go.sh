#!/bin/sh

set -e

if [ -e .strong-auth-test-id ]; then
    CONTAINER_ID=`cat .strong-auth-test-id`
    docker kill $CONTAINER_ID
    rm .strong-auth-test-id
fi

