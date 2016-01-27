#!/bin/bash

function delete_user {
    if [ $# -ne 1 ]; then
        echo "missing user"
        return 1
    fi
    curl -X DELETE "http://localhost:4567/db/user/$1"
}

delete_user "$@"; st=$?
echo
exit $st
