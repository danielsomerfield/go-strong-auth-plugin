#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

docker ps -a -f "name=go-cd" | grep go-cd
if [ $? == 0 ]; then
    docker rm -f go-cd
    echo "stopping go image"
else
    echo "go image not running"
fi

