#!/bin/bash

# Wait for the app to boot
sleep 7

RET=$(/usr/bin/http -b http://localhost:8081/health | grep UP)
CODE=$?

echo "CODE: $CODE"

exit $CODE
