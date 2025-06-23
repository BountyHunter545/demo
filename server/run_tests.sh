#!/bin/sh
set -e
mkdir -p target/test-classes

JUNIT=/usr/share/java/junit4.jar
HAMCREST=/usr/share/java/hamcrest-core.jar

javac -d target/test-classes \
    -cp $JUNIT:$HAMCREST \
    src/main/java/com/example/todo/*.java src/test/java/com/example/todo/*.java

java -cp target/test-classes:$JUNIT:$HAMCREST org.junit.runner.JUnitCore \
    com.example.todo.TodoServiceTest com.example.todo.ServerIntegrationTest
