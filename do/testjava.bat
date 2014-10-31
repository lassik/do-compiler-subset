@echo off
if exist DoPrim.class del DoPrim.class
copy ..\java\DoPrim.class DoPrim.class > nul
java -cp ..\java CmdLineProgDo2Java %1.do
if exist DoProgram.java del DoProgram.java
ren %1.java DoProgram.java
javac DoProgram.java
java DoProgram
