# Ciklum test application

Implemented service is used for finding some word in the big text file.

## Installation

This project is written by using the latest JDK 11

## Build application
This command build the project and create executable jar in target folder
```bash
mvn clean install
```

## Run application
```bash
java -jar target\Test-1.0-SNAPSHOT-jar-with-dependencies.jar ${word} ${path_to_file}\big.txt
```

## Usage

In the resources there is big.txt, it contains big text which can be used for finding a word from the given txt file:
```

## Exmaple of using
java -jar target\Test-1.0-SNAPSHOT-jar-with-dependencies.jar Timothy big.txt
Timothy-->[[lineOffset=9600,charOffset=0][lineOffset=9935,charOffset=29]]