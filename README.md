# Crossdocking Allocation Problem

## Dependencies

* Java 17
* Maven 3.8.4

## Testing

This software has a series of tests that can be launched with this command on the root folder

```shell
mvn test
```

## Compiling

Running the next command and compile/assemble the project with all the dependencies packed

```shell
mvn clean compile assembly:single
```

This will create a JAR in the target folder called `crossdocking-assignment-problem-1.0-jar-with-dependencies.jar`

## Running

Once the jar is generated, you can run the program using this command:
```java
java -cp target/crossdocking-assignment-problem-1.0-jar-with-dependencies.jar Main [filename]
```
