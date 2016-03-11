#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#CONTAINER_ID="$(docker inspect -f '{{.Id}}' 'go-cd')"
docker exec go-cd "$@"