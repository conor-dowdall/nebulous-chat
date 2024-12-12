#!/bin/bash

# Navigate to the project root directory to compile the Java files
cd "$(dirname "$0")/.."

# Run the server
java -cp bin/ com.nebulous.chat.server.Server
