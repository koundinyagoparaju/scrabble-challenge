Scrabble-Challenge

Pre-requisites: docker

Steps to run the application:
1. Sample board and letters files are provided in resources directory in src/main. Change them if you want to test different combinations
2. execute run.sh file

Approach:

My approach is to calculate score for each player for each row and column and add them.

I have considered following corner cases apart from the happy path scenario:
1. Invalid letters (words as letters)
2. Missing letters (Letters which are missing from letters.json but used in board.json)
3. Players using single letter words.

