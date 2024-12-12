package com.nebulous.chat.client;

import java.io.*;
import java.net.*;
import java.util.*;

import com.nebulous.chat.utils.ChatConstants;

/**
 * The {@code Client} class handles the client-side functionality for a chat
 * application. It establishes a connection to the server, sends the user's
 * name, and starts both the message receiving and sending functionalities. This
 * class runs the client-side chat interface, allowing the user to interact with
 * the server.
 */
public class Client {

    /**
     * The username of the client. This value is set by the user during the initial
     * setup.
     */
    private static String userName;

    /**
     * The address of the server to which the client will connect.
     * This value is provided by the user at the start of the program.
     */
    private static String serverAddress;

    /**
     * The entry point of the client application. This method initializes the client
     * by prompting the user for their username and the server address, and then
     * establishes a connection to the server. Once the connection is established,
     * it handles the sending and receiving of messages.
     *
     * @param args command-line arguments (not used in this implementation)
     */
    public static void main(String[] args) {

        // Create a Scanner object to handle user input from the console
        try (Scanner scanner = new Scanner(System.in)) {

            // Get the username and server address from the user
            userName = UserInputHandler.getUserName(scanner);
            serverAddress = UserInputHandler.getServerAddress(scanner);

            // Try connecting to the server and start the chat
            try (
                    // Establish the socket connection to the server
                    Socket socket = new Socket(serverAddress, ChatConstants.PORT);
                    // Create PrintWriter for sending messages to the server
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    // Create BufferedReader to receive messages from the server
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Append the client’s IP address to the username and send it to the server
                // immediately after connection
                userName += "@" + socket.getInetAddress().getHostAddress();
                writer.println(userName);

                // Start a new thread to listen for incoming messages from the server
                MessageReceiver.start(reader, socket);

                // Start allowing the user to send messages to the server
                MessageSender.start(scanner, writer, socket, userName);

            } catch (IOException e) {
                System.err
                        .println("Could not connect to server at " + serverAddress + " on port " + ChatConstants.PORT);
                System.err.println(e.getMessage());
                System.err.println("Maybe the server isn't running?");
            }

        } catch (Exception e) {
            // Handle any unforeseen circumstances and print a stack trace
            e.printStackTrace();
        }
    }

}