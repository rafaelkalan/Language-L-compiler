# Language-L-compiler

The purpose of the practical work is to develop a complete compiler that translates programs written in source language "L" into a subset of ASSEMBLY of the 80x86 family. Both languages ​​will be described during the semester. At the end of the work, the compiler must produce a text file that can be converted into machine language by the MASM editor and executed successfully on an actual processor. In case the program contains errors, the compiler should report the first error and finish the compilation process. The format of the error messages will be specified later and should be strictly observed. The compiler executable program should be called "LC" and receive 2 command-line parameters (arguments): the full name of the source program to be compiled (extension .L) and the full name of the ASSEMBLY program (extension .ASM) to be generated

## Language Definition

The "L" language is a simplified imperative language, with characteristics of C and Pascal. The language offers treatment for two explicit basic types: char and integer, in addition to the logical type that is implicit. The char type is a scalar that varies from 0 to 255, and can be written in alphanumeric or hexadecimal format. Constants in hexadecimal format are of the form 0DDh, where DD is a hexadecimal number. Constants in alphanumeric format are of the form 'c' where c is a printable character. The integer type is a scalar ranging from -32768 to 32767, occupying 2 bytes. In addition to the basic types, the language allows the definition of unidimensional characters and integer vectors, up to 4kbytes. A string is a vector of characters that when stored in memory has its useful content terminated by the character '$'. Constants representing strings are delimited by quotation marks and should not contain quotation marks, line breaks, or $. However, these characters are allowed in the character vectors. In this way, vectors and strings are compatible with each other, being the responsibility of the programmer to control their contents and sizes. logical type assumes values ​​0 (false) and 1 (true), occupying one byte of memory

### Prerequisites

To run this projece you will need [install Java and configure java](https://docs.oracle.com/cd/B28359_01/java.111/b31225/chfour.htm#BABCFGAB)

To compiler project 
```
javac Compiler.java
```

To run the Compiler 
```
java Compiler < t1.l
```
#### Note 
* t1.l is one program write on the compiler language

## Built With

* [Java](https://docs.oracle.com/javase/7/docs/api/) - Language used on the development of this project
* [Assembly](https://en.wikipedia.org/wiki/Assembly_language) - Asssembly

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [Git](https://git-scm.com/) for versioning. For the versions available, see the [tags on this repository](https://github.com/rafaelkalan/Language-L-compiler/branches). 

## Authors

* **Rafael Oliveira Mendes Lima** - *Initial work* - [Rafael Oliveira](https://github.com/PurpleBooth)
* **Matheus David Lauar** - *Initial work* - [Matheus David](https://github.com/MatShouldPutStuffHere)
* **Leonardo Machado Decina** - *Initial work* - [Leonardo Decina](https://github.com/LeonardoDecina)


See also the list of [contributors](https://github.com/rafaelkalan/Language-L-compiler/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Incressed the knowlodged about how a compiler works. 
