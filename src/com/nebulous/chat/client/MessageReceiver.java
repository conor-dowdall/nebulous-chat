package com.nebulous.chat.client;

import java.io.*;
import java.net.Socket;

import com.nebulous.chat.utils.ChatConstants;

/**
 * The {@code MessageReceiver} class is responsible for receiving messages from
 * the server and displaying them to the client. It listens for incoming
 * messages in a separate thread and handles special messages such as server
 * shutdown or server full notifications.
 */
public class MessageReceiver {

    /**
     * Starts a thread to continuously listen for incoming messages from the server.
     * This method reads messages from the server and displays them on the client's
     * console. If a special server message (such as shutdown or server full) is
     * received, it terminates the client application.
     *
     * @param reader The {@link BufferedReader} used to read messages from the
     *               server.
     * @param socket The {@link Socket} through which the client communicates with
     *               the server.
     */
    public static void start(BufferedReader reader, Socket socket) {

        // Create and start a new thread to receive messages from the server
        Thread receiveThread = new Thread(() -> {
            try {
                String message;

                // Continuously listen for incoming messages from the server
                while ((message = reader.readLine()) != null) {

                    // Print a newline for better message formatting in the console
                    System.out.print("\n");
                    System.out.println(message);
                    System.out.print("\n");

                    // Check if the message is a shutdown or full server message
                    if (ChatConstants.SERVER_SHUTDOWN_MESSAGE.equals(message)
                            || ChatConstants.SERVER_FULL_MESSAGE.equals(message)) {
                        // Print the server's message and terminate the client application
                        System.out.println("Received: " + message);
                        System.out.println("Exiting...");
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                // Handle any IO exceptions while reading from the server
                if (!socket.isClosed()) {
                    e.printStackTrace();
                }
            }
        });

        // Start the thread that handles receiving messages
        receiveThread.start();

    }
}
