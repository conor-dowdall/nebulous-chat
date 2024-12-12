# Nebulous Chat Application

The Nebulous Chat Application is a multi-user, thread-based client-server chat system implemented in Java. It leverages sockets for communication and supports real-time message exchange. The system aims to provide a flexible and robust framework for messaging with considerations for extensibility and fault tolerance. The CopyOnWriteArraySet ensures thread-safe operations when managing active clients.
## Design Overview

- How does the client know the server's address?
    - The client retrieves the server's address via interactive input from the user.
    - Default is provided (localhost) if no input is given.
    - Port 55555 is hardcoded into the application in the ChatConstants class.

- What happens if the client can’t reach the server during startup?
    - If the server is unavailable, the client displays an error message indicating the failure.
    - The application exits gracefully, allowing the user to retry.

- What happens if the client-server connection is lost during a chat session?
    - Server:
        - Detects disconnection when a client's socket closes and cleans up the corresponding connection.
        - Broadcasts a "user left the chat" message to remaining clients.
    - Client:
        - Automatically stops receiving or sending messages if the connection is severed.
        - Notifies the user and terminates the session gracefully.

- What happens if the server is full?
    - Server:
        - For testing, the server run only two client connection threads.
        - Connection attempt is logged to the terminal.
    - Client:
        - User receives a server full message, and is gracefully disconnected.


## Convenience Scripts
Linux, Mac, & Windows scripts.
```
bin
├── nebulous-client.bat
├── nebulous-client.sh
├── nebulous-server.bat
└── nebulous-server.sh
```
## Commands

- Exit Chat: Type \q to leave the chat or shut down the server.
- Custom Server Address: Enter the IP address or hostname of the server during client setup.

Happy chatting with Nebulous! 🏆