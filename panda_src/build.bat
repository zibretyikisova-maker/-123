@echo off
rem Requires JDK 21 and Gradle 8.11+ in PATH
gradle build
echo.
echo Jar: build\libs\furnacespawner-1.0.0.jar
pause
