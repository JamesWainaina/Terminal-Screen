import client.CommandClient;
import server.ScreenServer;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("Usage: java main <server|client>");
            return;
        }

        if (args[0].equalsIgnoreCase("server")){
            // start the server
            ScreenServer server = new ScreenServer();
            Thread serverThread = new Thread(server);
            serverThread.start();
        } else if (args[0].equalsIgnoreCase("client")){
            // start the client
            CommandClient client = new CommandClient();
            client.run();
        }else {
            System.out.println("Unknown argument. Use 'server' or 'client' .");
        }
    }
}

