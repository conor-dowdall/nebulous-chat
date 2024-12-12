package com.nebulous.chat.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.nebulous.chat.utils.ChatConstants;

/**
 * The {@code MessageSender} class is responsible for sending messages from the
 * client to the server. It reads user input from the console, sends messages to
 * the server, and handles the client’s exit command to gracefully close the
 * connection.
 */
public class MessageSender {

    /**
     * Starts the process of sending messages from the client to the server.
     * This method listens for user input, sends each message to the server,
     * and handles the graceful termination of the chat session when the exit
     * command is received.
     *
     * @param scanner  The {@link Scanner} used to read input from the console.
     * @param writer   The {@link PrintWriter} used to send messages to the server.
     * @param socket   The {@link Socket} through which the client communicates with
     *                 the server.
     * @param userName The username of the client, which is sent as part of the
     *                 messages.
     */
    public static void start(Scanner scanner, PrintWriter writer, Socket socket, String userName) {
        String message;

        // Continuously read user input and send it to the server
        while (true) {
            message = scanner.nextLine();

            // Check if the user entered the exit command
            if (ChatConstants.EXIT_COMMAND.equalsIgnoreCase(message)) {
                // Inform the server that the user is leaving the chat
                writer.println(userName + " is leaving the chat.");
                System.out.println("Ending chat session...");
                // Exit the loop and close the connection
                break;
            }

            // Send the message to the server
            writer.println(message);
        }

        // Close resources after the loop ends
        try {

            // Close the PrintWriter, Scanner, and Socket
            writer.close();
            scanner.close();
            socket.close();

            System.out.println("Connection closed.");

        } catch (IOException e) {
            // Handle any errors encountered while closing resources
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
}
