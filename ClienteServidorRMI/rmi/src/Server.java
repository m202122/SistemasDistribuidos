import java.rmi.Naming;

public class Server {

    public Server(String serverName) {
        try {
			System.out.println("Starting server...");
            AgendaInterface a = new AgendaImpl();
            Naming.rebind("rmi://"+ serverName +":1099/AgendaService", a);
			System.out.println("Server started!");
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    public static void main(String args[]) {

        String serverName;

        if (args.length == 0){
            serverName = "127.0.0.1";
        }
        else {
            serverName = args[0];
        }
        new Server(serverName);

    }

}
