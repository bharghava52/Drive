@echo off

javac -classpath %CLASSPATH%;WEB-INF\lib\*;WEB-INF\classes;.; JAVA\*.java -d WEB-INF\classes\

jar -cvf Drive.war *
