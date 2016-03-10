#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

CONTAINER_ID=`cat $DIR/../.strong-auth-test-id`
docker exec $CONTAINER_ID "$@"