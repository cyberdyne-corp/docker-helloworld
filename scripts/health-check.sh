#!/bin/bash

# Wait for the app to boot
sleep 7

RET=$(/usr/bin/http -b http://localhost:8081/health | /usr/bin/jq -e 'contains({status: "UP"})')
CODE=$?

echo "CODE: $CODE"

exit $CODE
