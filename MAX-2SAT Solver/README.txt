Yixin Chen

COMP 610 Final Project MAX2SAT

Spring 2017



Programming language is C++ 14



Put the input file instance.txt in the same folder with the source code.



Compile instruction (need g++ version 4.7 or higher): In terminal, excute the following two commands.

g++ -c max2sat.cpp -std=c++1y

g++ -o max2sat max2sat.o


Excute command:
./max2sat



Note: If the version of g++ is lower than 4.7, this program cannot be compiled. 



This program reads an input file named instance.txt, and outputs the results to a file named result.txt.

If no file named instance.txt is in the current directory, the program will give an error message and then terminate.