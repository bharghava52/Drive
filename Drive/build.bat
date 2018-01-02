@echo off

javac Users/ConnectionPooling.java
 
javac Users/DBModel.java

javac Users/Profile.java

javac Users/User.java

javac -classpath %CLASSPATH%;Users/servlet-api.jar Users/GetCookies.java

javac -classpath %CLASSPATH%;Users/servlet-api.jar Users/GetFiles.java

javac -classpath %CLASSPATH%;Users/servlet-api.jar Users/Login.java

javac -classpath %CLASSPATH%;Users/servlet-api.jar Users/Logout.java

javac -classpath %CLASSPATH%;Users/servlet-api.jar Users/Signup.java

javac -classpath %CLASSPATH%;Users/servlet-api.jar Users/DownloadFile.java

javac -classpath %CLASSPATH%;Users/servlet-api.jar Users/LocFiles.java



