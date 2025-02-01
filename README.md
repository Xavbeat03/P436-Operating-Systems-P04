# P436-Operating-Systems-P04
A simple threading assignment

## Requirements
You are to use a language with threads. There should be four command line parameters, first is the input file name, followed by three thread names.

- %p04 infile.txt apple banana carrot

This would run program p04 and will:

- Provide the input file to a producer thread (name apple).
- Create two consumer threads (banana and carrot).
- Create two cubbyholes, one for each consumer.
- Thread apple will read the input file in the parameter.It will go through every character.
- Send the vowels to banana through Cub_1.
- Send the consonants to carrot through Cub_2.
- At the conclusion of the input file, send a period to both consumers.
- Have both cosumer threads print each character received and at the end of the input file print each thread should print their name and the count of the characters received.
- The main shall join all 3 threads and print “the end”
