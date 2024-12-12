@echo off

:: Navigate to the project root directory and compile Java files
cd /d %~dp0\..

:: Run the client
java -cp bin/ com.nebulous.chat.client.Client