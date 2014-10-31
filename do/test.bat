@echo off
if exist DoPrim.class del DoPrim.class
copy ..\java\DoPrim.class DoPrim.class > nul
java -cp ..\java CmdLineProgDo2Class %1.do
if exist DoProgram.class del DoProgram.class
ren %1.class DoProgram.class
java DoProgram
