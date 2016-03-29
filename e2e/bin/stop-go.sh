#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

docker ps -a -f "name=go-cd" | grep go-cd
if [ $? == 0 ]; then
    echo "stopping go container"
    docker rm -f go-cd
else
    echo "go container not running"
fi