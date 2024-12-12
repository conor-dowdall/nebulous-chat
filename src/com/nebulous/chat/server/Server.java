package com.nebulous.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.nebulous.chat.utils.ChatConstants;

/**
 * The {@code Server} class initializes and manages a chat server. It listens
 * for client connections, handles communication, and allows for a graceful
 * shutdown.
 * 
 * <p>
 * Key features include:
 * <ul>
 * <li>Thread-safe management of active client connections.</li>
 * <li>A fixed-size thread pool to handle client requests.</li>
 * <li>Graceful shutdown that ensures resources are cleaned up properly.</li>
 * </ul>
 */
public class Server {

    /**
     * Server socket to accept client connections.
     */
    private static ServerSocket serverSocket;

    /**
     * Set of active client connections, thread-safe for concurrent access.
     */
    private static Set<ClientConnection> clientConnections = new CopyOnWriteArraySet<>();

    /**
     * Boolean flag to indicate if the server is running.
     */
    private static boolean isServerRunning = true;

    /**
     * Thread pool to manage client handler threads
     */
    private static ExecutorService threadPool = Executors.newFixedThreadPool(ChatConstants.THREAD_POOL_SIZE);

    /**
     * The main entry point of the server. Initializes the server socket,
     * starts a shutdown listener, and handles incoming client connections.
     *
     * @param args Command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        System.out.println("Server starting...");

        // Start a thread to listen for shutdown commands
        new ShutdownThread().start();

        try {

            // Initialize the server socket and start listening for connections
            serverSocket = new ServerSocket(ChatConstants.PORT);
            System.out.println("Server successfully started. Waiting for connections...");

            // Handle incoming client connections
            while (isServerRunning && !serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    // Reject connection if the server is full
                    if (clientConnections.size() >= ChatConstants.THREAD_POOL_SIZE) {
                        System.out.println("Server is full. Rejecting new connection.");
                        PrintWriter tempWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                        tempWriter.println(ChatConstants.SERVER_FULL_MESSAGE); // Alert the client
                        tempWriter.flush(); // Make sure tempWriter flushed
                        clientSocket.close(); // Close the connection
                        continue;
                    }

                    // Submit a new client handler task to the thread pool
                    threadPool.submit(new ClientHandler(clientSocket));
                } catch (SocketException e) {
                    if (serverSocket.isClosed())
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            shutdownServer();
        }

        // Server shutdown logic: exit the program after the server is stopped
        System.out.println("Server has been shut down.");
        System.exit(0); // Exit the program
    }

    /**
     * Returns the set of active client connections.
     *
     * @return A thread-safe set of active {@link ClientConnection} objects.
     */
    public static Set<ClientConnection> getClientConnections() {
        return clientConnections;
    }

    /**
     * Shuts down the server gracefully by closing all client connections,
     * stopping the thread pool, and releasing server resources.
     */
    public static void shutdownServer() {
        try {
            System.out.println("Shutting down the server...");

            // Notify and close all client connections
            for (ClientConnection clientConnection : clientConnections) {
                if (clientConnection.isSocketOpen()) {
                    clientConnection.getWriter().println(ChatConstants.SERVER_SHUTDOWN_MESSAGE);
                    clientConnection.close(); // Close the client's socket
                }
            }

            // Update the flag to indicate the server is no longer running
            isServerRunning = false;

            // Close the server socket
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();

            // Shut down the thread pool
            threadPool.shutdown();
            if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                threadPool.shutdownNow(); // Force shutdown if tasks don't terminate
            }

            System.out.println("Server resources have been released.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Server shutdown exception: " + e.getMessage());
        }
    }
}
