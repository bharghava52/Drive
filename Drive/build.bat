@echo off

javac JAVA\ConnectionPooling.java -d WEB-INF\classes\

javac JAVA\ZipHelper.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\servlet-api.jar JAVA\CreateFolder.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\servlet-api.jar JAVA\DeleteFolder.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\servlet-api.jar JAVA\FriendData.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\servlet-api.jar JAVA\FriendDatas.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\servlet-api.jar JAVA\GetFiles.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\servlet-api.jar JAVA\GetSession.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\servlet-api.jar JAVA\LocFiles.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\servlet-api.jar JAVA\Logout.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\*; JAVA\Share.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\*; JAVA\Upload.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\*;WEB-INF\classes;.; JAVA\DownloadFolder.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\*;WEB-INF\classes;.; JAVA\DownloadFile.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\*;WEB-INF\classes;.; JAVA\FriendFiles.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\*;WEB-INF\classes;.; JAVA\Login.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\*;WEB-INF\classes;.; JAVA\SharedFiles.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\*;WEB-INF\classes;.; JAVA\Signup.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\*;WEB-INF\classes;.; JAVA\User.java -d WEB-INF\classes\

javac -classpath %CLASSPATH%;WEB-INF\lib\*;WEB-INF\classes;.; JAVA\Users.java -d WEB-INF\classes\

jar -cvf Drive.war *