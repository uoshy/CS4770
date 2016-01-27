#!/bin/bash

function get_user {
    if [ $# -ne 1 ]; then
        echo "missing user"
        return 1
    fi
    curl -X GET "http://localhost:4567/db/user/$1"
}

get_user "$@"; st=$?
echo
exit $st
