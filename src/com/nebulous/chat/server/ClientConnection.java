package com.nebulous.chat.server;

import java.io.*;
import java.net.*;

/**
 * Represents a connection to a client on the server side. This class provides
 * methods to manage the client's socket and writer, check the connection
 * status, and cleanly close the connection.
 */
public class ClientConnection {

    /**
     * The {@link PrintWriter} used to send messages to the client.
     */
    private PrintWriter writer;

    /**
     * The {@link Socket} representing the client's connection to the server.
     */
    private Socket socket;

    /**
     * Constructs a {@code ClientConnection} instance with the provided
     * writer and socket.
     *
     * @param writer The {@link PrintWriter} for sending messages to the client.
     * @param socket The {@link Socket} representing the client's connection.
     */
    public ClientConnection(PrintWriter writer, Socket socket) {
        this.writer = writer;
        this.socket = socket;
    }

    /**
     * Returns the {@link PrintWriter} associated with this connection.
     *
     * @return The {@link PrintWriter} used to send messages to the client.
     */
    public PrintWriter getWriter() {
        return writer;
    }

    /**
     * Returns the {@link Socket} associated with this connection.
     *
     * @return The {@link Socket} representing the client's connection.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Checks whether the client's socket is open and active.
     *
     * @return {@code true} if the socket is not {@code null} and not closed;
     *         {@code false} otherwise.
     */
    public boolean isSocketOpen() {
        return socket != null && !socket.isClosed();
    }

    /**
     * Closes the client's socket if it is open. This method ensures that the
     * resources associated with the client's connection are released.
     *
     * @throws IOException If an I/O error occurs while closing the socket.
     */
    public void close() throws IOException {
        if (socket != null && !socket.isClosed())
            socket.close();
    }
}
