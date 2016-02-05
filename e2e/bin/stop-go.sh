#!/bin/sh

set -e

CONTAINER_ID=`cat .strong-auth-test-id`
docker kill $CONTAINER_ID
rm .strong-auth-test-id
