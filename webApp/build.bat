@echo off

javac JAVA/ConnectionPooling.java
 
javac JAVA/DBModel.java

javac JAVA/Profile.java

javac JAVA/User.java

javac -classpath %CLASSPATH%;JAVA/servlet-api.jar JAVA/Login.java

javac -classpath %CLASSPATH%;JAVA/servlet-api.jar JAVA/Signup.java
