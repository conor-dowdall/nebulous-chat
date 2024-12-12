package com.nebulous.chat.server;

import java.io.*;
import java.net.*;

import com.nebulous.chat.utils.ChatConstants;

/**
 * Handles the server-side processing for a single client connection.
 * Manages receiving messages from the client, broadcasting messages to other
 * clients, and cleaning up resources upon disconnection.
 */
public class ClientHandler extends Thread {

    /**
     * The socket representing the connection to the client.
     */
    private Socket socket;

    /**
     * The username of the connected client.
     */
    private String userName;

    /**
     * Constructs a ClientHandler with the specified client socket.
     * 
     * @param socket the socket representing the client's connection.
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Handles communication with the connected client. This includes reading
     * messages, broadcasting them to other clients, and handling disconnections.
     */
    @Override
    public void run() {

        try (InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(inputStream)) {

            // Read and set the username, or use a default if empty.
            userName = reader.readLine();
            if (userName.isEmpty())
                userName = ChatConstants.DEFAULT_USER_NAME;

            // Register the client connection with the server.
            Server.getClientConnections()
                    .add(new ClientConnection(new PrintWriter(socket.getOutputStream(), true), socket));
            System.out
                    .println(userName + " has connected. Active connections: " + Server.getClientConnections().size());
            sendToAll(userName + " has joined the chat.");

            // Process incoming messages from the client.
            String message;
            while ((message = reader.readLine()) != null) {
                if (message.equalsIgnoreCase(ChatConstants.EXIT_COMMAND))
                    break;
                sendToAll(userName + ": " + message);
            }

            // Notify others when the client disconnects.
            sendToAll(userName + " has left the chat.");

        } catch (SocketTimeoutException e) {
            System.out.println("Connection timed out for " + userName);
        } catch (SocketException e) {
            handleSocketException(e);
        } catch (IOException e) {
            handleClientException(e);
        } finally {
            cleanupClientConnection(socket);
        }
    }

    /**
     * Handles exceptions related to the client's socket.
     * 
     * @param e the SocketException that occurred.
     */
    private void handleSocketException(SocketException e) {
        if (socket.isClosed()) {
            System.out.println("Socket closed. Ending client handler for " + userName);
        } else {
            System.out.println("Unexpected socket exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles general IOExceptions during client processing.
     * 
     * @param e the IOException that occurred.
     */
    private void handleClientException(IOException e) {
        System.out.println("Client handler exception: " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Cleans up resources associated with the client connection, including removing
     * the client from the active connections list and closing the socket.
     * 
     * @param socket the socket to clean up.
     */
    private void cleanupClientConnection(Socket socket) {
        Server.getClientConnections().removeIf(clientConnection -> clientConnection.getSocket().equals(socket));
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing socket: " + e.getMessage());
            e.printStackTrace();
        }
        System.out
                .println(userName + " disconnected. Active connections: " + Server.getClientConnections().size());
    }

    /**
     * Broadcasts a message to all active clients connected to the server.
     * 
     * @param message the message to send to all clients.
     */
    private void sendToAll(String message) {
        for (ClientConnection clientConnection : Server.getClientConnections()) {
            try {
                if (clientConnection.isSocketOpen()) {
                    clientConnection.getWriter().println(message);
                }
            } catch (Exception e) {
                System.out.println("Error sending message: " + e.getMessage());
            }
        }
    }
}
