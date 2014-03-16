
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AgendaInterface extends Remote {
    public String getDatabase() throws RemoteException, IOException;
    public String insert(String name, String fone, int op) throws RemoteException, IOException;
    public String search(String name) throws RemoteException, IOException;
    public String remove(String name) throws RemoteException, IOException;
}
