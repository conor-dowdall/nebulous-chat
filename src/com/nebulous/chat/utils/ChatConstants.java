package com.nebulous.chat.utils;

/**
 * A utility class that holds constant values used throughout the chat
 * application. These constants define configuration parameters, default values,
 * and common messages shared between the client and server.
 */
public class ChatConstants {

    /**
     * The port number on which the server and client use for connections.
     */
    public static final int PORT = 55555;

    /**
     * The maximum number of threads in the server's thread pool. This value
     * determines the maximum number of concurrent client connections the server
     * can handle.
     */
    public static final int THREAD_POOL_SIZE = 2;

    /**
     * The default username assigned to a client if none is provided.
     */
    public static final String DEFAULT_USER_NAME = "anonymous";

    /**
     * The command a client or server can use to exit the chat application.
     */
    public static final String EXIT_COMMAND = "\\q";

    /**
     * A message sent by the server to notify clients that the server is shutting
     * down.
     */
    public static final String SERVER_SHUTDOWN_MESSAGE = "Server is shutting down...";

    /**
     * A message sent by the server to notify a client that the server is full and
     * cannot accept new connections.
     */
    public static final String SERVER_FULL_MESSAGE = "Server is full. Try again later.";

}
