#! /bin/sh
set -e
cd "$(dirname "$0")"
cd ../java
outdir="../doc/javadoc"
test -e "$outdir" && rm -rf "$outdir"
find . -type f -iname "*.java" | xargs javadoc -d "$outdir"
