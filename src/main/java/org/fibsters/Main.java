package org.fibsters;

public class Main {

    // TODO: Make a config file for the port number and other settings.
    public static void main(String[] args) {
        int port = 8080; // default port.

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        FibHttpHandlerAPI handler = new FibHttpHandlerAPI(port);

        try {
            handler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
