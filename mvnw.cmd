@ECHO OFF
SETLOCAL

SET BASEDIR=%~dp0

SET WRAPPER_JAR=%BASEDIR%\.mvn\wrapper\maven-wrapper.jar
IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO Missing %WRAPPER_JAR%. Please ensure .mvn/wrapper is present.
  EXIT /B 1
)

IF NOT "%JAVA_HOME%"=="" (
  SET JAVA_EXEC=%JAVA_HOME%\bin\java.exe
) ELSE (
  SET JAVA_EXEC=java
)

"%JAVA_EXEC%" -Dmaven.multiModuleProjectDirectory="%BASEDIR%" -jar "%WRAPPER_JAR%" %*
ENDLOCAL

