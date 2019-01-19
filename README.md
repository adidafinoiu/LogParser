#  Custom Log Parser

This application parses a web server access log file, loads the log to MySQL and checks if a given IP makes more than a certain number of requests for the given duration. 

#### Build

The project makes use of maven to get dependencies and to pack everything into a jar archive.
In order to create the jar file you must run:
 
 **"mvn -DskipTests=true package"**. 
 
This will create a parser.jar in the target file.

#### Run

The parser.jar file can be run from the command line with the following command:

**"java -cp "parser.jar" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100"**

In order for the application to run a connection to a MySql database is required. 
The credentials to this database can be changed from _DatabaseManager.java_.
The current implementation tries to connect to:

**jdbc:mysql://192.168.99.100:3306/log with user=user and password=pass**