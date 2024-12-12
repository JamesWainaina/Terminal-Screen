import client.CommandClient;
import server.ScreenServer;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Main <server|client>");
            return;
        }

        if (args[0].equalsIgnoreCase("server")) {
            // start the server
            ScreenServer server = new ScreenServer();
            Thread serverThread = new Thread(server);
            serverThread.start();
        } else if (args[0].equalsIgnoreCase("client")) {
            if (args.length < 3) {
                System.out.println("Usage: java Main client <host> <port>");
                return;
            }
            // start the client
            String host = args[1];
            int port = Integer.parseInt(args[2]);
            CommandClient client = new CommandClient();
            client.run();  // call the run method to start client communication
        } else {
            System.out.println("Unknown argument. Use 'server' or 'client'.");
        }
    }
}
