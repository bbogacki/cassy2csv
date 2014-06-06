# What is Cassy2CSV ?

Cassy2CSV is simple shell-like application that allows you to export and save query result from [Cassandra](https://github.com/apache/cassandra) to CSV file.

# Sample Cassy2CSV session

Sample session might look like the one below

![alt tag](https://s3-eu-west-1.amazonaws.com/bidlab-public/cassy2csv/cassy2csv_session.png)

# Installation

1. Download binary from https://github.com/bbogacki/cassy2csv/releases/download/0.1/cassy2csv-0.1.jar
2. From the command prompt execute cassy2csv shell
```
java -jar cassy2csv-0.1.jar
```

# Usage

Having cassy2csv shell executed, type "help" to see available options. 

You may also prepare script that can be processed by Cassy2CSV.

```
$ cat script.cmds
set --separator ,
connect --node 127.0.0.1 --keyspace intracker
query --stmt "select * from flight_costs" --filename myoutput2.csv
$ java -jar cassy2csv-0.1.jar --cmdfile script.cmds
cze 06, 2014 5:33:46 PM org.springframework.shell.core.AbstractShell handleExecutionResult
INFO: separator = ,
directory = .
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
cze 06, 2014 5:33:46 PM org.springframework.shell.core.AbstractShell handleExecutionResult
INFO: Connected to cluster: Test Cluster
Keyspace: intracker
cze 06, 2014 5:33:46 PM org.springframework.shell.core.AbstractShell handleExecutionResult
INFO: Success, 15 rows saved to ./myoutput2.csv
$ ls -l myoutput2.csv 
-rw-r--r--  1 devel  staff  1256  6 cze 17:33 myoutput2.csv
```

