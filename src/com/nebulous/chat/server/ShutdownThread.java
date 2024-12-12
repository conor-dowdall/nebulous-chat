package com.nebulous.chat.server;

import java.util.Scanner;

import com.nebulous.chat.utils.ChatConstants;

/**
 * A thread that listens for a shutdown command from the server administrator.
 * This thread continuously monitors the server console for user input and
 * triggers a server shutdown when the specified exit command is entered.
 */
public class ShutdownThread extends Thread {

    /**
     * Continuously monitors the server console for user input. If the input matches
     * the server's exit command, it initiates the server shutdown process.
     */
    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase(ChatConstants.EXIT_COMMAND)) {
                    Server.shutdownServer();
                    break;
                }
            }
        }
    }
}
