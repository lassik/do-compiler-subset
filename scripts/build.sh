#! /bin/sh
set -e
cd "$(dirname "$0")"
cd ../java
javac *.java
cd ../do
rm -f DoPrim.class
cp -p ../java/DoPrim.class DoPrim.class
