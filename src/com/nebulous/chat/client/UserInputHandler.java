package com.nebulous.chat.client;

import java.util.Scanner;

import com.nebulous.chat.utils.ChatConstants;

/**
 * The {@code UserInputHandler} class is responsible for interacting with the
 * user to gather necessary input such as the user's name and the server's
 * address. This class provides methods to prompt the user for input, and if no
 * input is provided, it uses default values.
 */
public class UserInputHandler {

    /**
     * Prompts the user to enter their desired username. If the user does not
     * provide a username (i.e., they press ENTER without typing), the default
     * username is used. The default username is defined in
     * {@link ChatConstants#DEFAULT_USER_NAME}.
     *
     * @param scanner The {@link Scanner} object used to read user input from the
     *                console.
     * @return The username entered by the user or the default username if no input
     *         is provided.
     */
    public static String getUserName(Scanner scanner) {
        // Prompt the user for their username, with a default option displayed
        System.out.println("[Hit ENTER for default: " + ChatConstants.DEFAULT_USER_NAME + "]");
        System.out.print("Enter your User Name: ");
        String userName = scanner.nextLine().trim();

        // If no username is entered, use the default
        if (userName.isEmpty()) {
            userName = ChatConstants.DEFAULT_USER_NAME;
            System.out.println("User Name: " + ChatConstants.DEFAULT_USER_NAME);
        }

        // Add a blank line for readability
        System.out.println();

        return userName;
    }

    /**
     * Prompts the user to enter the server IP address to which the client should
     * connect. If the user does not provide an address (i.e., they press ENTER
     * without typing), the default address "localhost" is used.
     * 
     * @param scanner The {@link Scanner} object used to read user input from the
     *                console.
     * @return The server address entered by the user or "localhost" if no input is
     *         provided.
     */
    public static String getServerAddress(Scanner scanner) {
        // Prompt the user for the server IP address, with a default option displayed
        System.out.println("[Hit ENTER for default: localhost]");
        System.out.print("Enter the server IP address to connect to: ");
        String serverAddress = scanner.nextLine().trim();

        // If no server address is entered, use the default "localhost"
        if (serverAddress.isEmpty()) {
            serverAddress = "localhost";
            System.out.println("Server: localhost");
        }

        // Add a blank line for readability
        System.out.println();

        return serverAddress;
    }
}
