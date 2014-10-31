#! /bin/sh
set -e
cd "$(dirname "$0")"
./build.sh
cd ../do
java -cp ../java CmdLineProgDo2Java "$1".do
test -e DoProgram.java && rm -f DoProgram.java
mv "$1".java DoProgram.java
javac DoProgram.java
java DoProgram
