#! /bin/sh
set -e
cd "$(dirname "$0")"
./build.sh
cd ../do
java -cp ../java CmdLineProgDo2Class "$1".do
test -e DoProgram.java && rm -f DoProgram.java
mv "$1".class DoProgram.class
java DoProgram
