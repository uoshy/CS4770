#!/bin/sh
BASEDIR=$(dirname $0)
exec java -cp $BASEDIR EchoEnv
