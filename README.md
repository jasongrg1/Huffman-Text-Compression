# Huffman-Text-Compression
Huffman coding implementation to compress and decompress text files.

#Introduction
The program is able to compress and decompress files.



# How to use the program
The program can be used to compress and decompress files by editing 
the Huffman.java file.
In the main method of the file, there are three strings which can be 
edited.

When file name is written in "encodingPath", the program generates the encoding for the file entered.
This is usefull when using different encodings to compress files.
For example, this can be used to generate the encoding for the French version of a book then use it to compress the English version of a book.

The file that is being compressed needs to be entered in "convertPath". The file entered here is compressed using the encoding entered in "encodingPath".
"encodingPath" can also be left empty if you want to generate the encoding for and convert the same file.
This can be seen in the demo when the file "Hamlet (French).txt" is compressed.

The file that needs to be decompressed is entered in "decompressPath".
This can also be left empty if the no file needs to be compressed.
This will output a message saying "No file to be decompressed".

"convertPath" can also be left empty if no file needs to be compressed. This will output a message saying "No file needs to be compressed".


Below are the some of the texts I used to test my code:
https://www.gutenberg.org/ebooks/2265
https://www.gutenberg.org/ebooks/15032
https://www.gutenberg.org/ebooks/15632

https://www.gutenberg.org/ebooks/2267
https://www.gutenberg.org/ebooks/18179
https://www.gutenberg.org/ebooks/7185
