#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

docker images thoughtworks/strongauth-tests | grep thoughtworks/strongauth-tests
if [ $? == 0 ]; then
    echo "deleting go image"
    docker rmi -f thoughtworks/strongauth-tests
else
    echo "go image doesn't exist"
fi

docker build --tag "thoughtworks/strongauth-tests" $DIR/..
