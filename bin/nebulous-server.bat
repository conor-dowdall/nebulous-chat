@echo off

:: Navigate to the project root directory and compile Java files
cd /d %~dp0\..

:: Run the server
java -cp bin/ com.nebulous.chat.server.Server
