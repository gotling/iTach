package se.gotling.itach;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ITachLib {

    private String host = "169.254.1.70";
    private int port = 4998;
    private int repeat = 3;
    private int timeout = 2000;
    private static int module = 1;
    private static int connection = 1;
    private static String baseCommand = "sendir,[MODULE]:[CONNECTION],1,38000,[REPEAT],";

    /**
     * Initialize library with default values. Good for ADHOC testing.
     *  host    = 169.254.1.70
     *  port    = 4998
     *  repeat  = 3
     *  timeout = 2000 milliseconds
     *  module  = 1
     *  connection = 1
     */
    public ITachLib() {
    }

    /**
     * Initialize iTach communication library
     * @param host IP or host name of iTach module
     * @param port iTach module is configured to listen to
     * @param repeat each sending command to be be sur it's received
     * TODO: Allow timeout, module and connection to be set
     */
    public ITachLib(String host, int port, int repeat) {
        this.host = host;
        this.port = port;
        this.repeat = repeat;
    }

    /**
     * Build command that will be sent to iTach module
     * @param subCommand accepts output from NexaLib
     * @return command with all parameters set, ended with carriage return (\\r)
     */
    private String buildCommand(String subCommand) {
        String command = baseCommand + subCommand;
        command = command.replace("[MODULE]", String.valueOf(module)).replace("[CONNECTION]", String.valueOf(connection)).replace("[REPEAT]", String.valueOf(repeat));
        command += "\r";

        return command;
    }

    /**
     * Sends supplied command to iTach module
     * @param subCommand command created by NexaLib
     * @return response from iTach module
     * @throws UnknownHostException is thrown if connection could not be established
     * @throws IOException is thrown if there is a failure during transmission
     * TODO: Analyze return value from iTach
     */
    public String sendCommand(String subCommand) throws UnknownHostException, IOException {
        Socket socket = null;
        PrintWriter out = null;
        InputStreamReader in = null;

        String command = buildCommand(subCommand);

        // Initialize connection
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(timeout);
        } catch (UnknownHostException e) {
            throw new UnknownHostException("Failed to connect to host " + host + " on port " + port);
        }

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new InputStreamReader(socket.getInputStream());
            
            out.print(command);
            out.flush();

            char[] buffer = new char[100];
            int chars_read;
            chars_read = in.read(buffer);
            String result = String.copyValueOf(buffer, 0, chars_read - 1);

            return result;
        } finally {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to close connection: " + e.getMessage());
            }
        }
    }
}
