# Do compiler (University project) #

This is a compiler for a small stack-based programming language. I did
it as a university project in 2010. The language is a subset of a more
fully-featured language called Do that I was designing as a hobby
project at the time.

The compiler turns `.do` source files into Java source files (`.java`)
or directly into equivalent JVM class files (`.class`).

The source repository includes a few toy programs to test the compiler
and demonstrate the language.

No compiler optimizations are implemented.

The resulting Java class files depend on a small utility library
implemented in the source fiel `DoPrim.java`.

## Documentation ##

[Description of the assignment](http://lassikortela.net/do-compiler/assignment.pdf) -- Submitted at the beginning of the project.

[Final document](http://lassikortela.net/do-compiler/loppudokumentti.pdf) -- Submitted at the end of the project.

[JavaDoc documentation](http://lassikortela.net/do-compiler/javadoc/) -- Extracted from the Java source code.
