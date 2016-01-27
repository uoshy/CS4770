#!/bin/bash

function create_user {
    if [ $# -ne 4 ]; then
        echo "usage: create_user id password name role"
        return 1
    fi

    local u="\"id\":\"$1\""
    local p="\"password\":\"$2\""
    local n="\"name\":\"$3\""
    local r="\"role\":\"$4\""
    curl -X POST -H "Content-Type: application/json" \
    -d "{$u,$p,$n,$r}" \
    "http://localhost:4567/db/user/$1"
}

create_user "$@" ; st=$?
echo
exit $st
