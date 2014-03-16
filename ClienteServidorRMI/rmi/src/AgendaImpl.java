
import java.io.IOException;
import java.rmi.RemoteException;

public class AgendaImpl extends java.rmi.server.UnicastRemoteObject implements AgendaInterface {

    private AgendaDatabase db;

    public AgendaImpl() throws RemoteException, IOException {
        db = new AgendaDatabase();
    }

    @Override
    public String getDatabase() throws RemoteException, IOException {
        return db.getDatabase();
    }

    @Override
    public String insert(String name, String fone, int op) throws RemoteException, IOException {
		System.out.println("Inserindo dados...");
        if(op == 0){
            int i = 0;
            while( db.search(name + i) != null)
                ++i;
            db.insert(new Dados(name+i,fone));
            return "Registro inserido com nome: " + name+i;
        }if(op == 1){
            db.insert(new Dados(name,fone));
            return "Registro atualizado!";
        }if(op == 2){
            db.insert(new Dados(name,fone));
            return "Registro inserido com sucesso!";
        }
        return "error";
    }

    @Override
    public String search(String name) throws RemoteException, IOException {
        System.out.println("Procurando dados...");
		Dados result = db.search(name);
        if(result == null)
            return "Registro nao encontrado!";
        return result.toString();
    }

    @Override
    public String remove(String name) throws RemoteException, IOException {
		System.out.println("Removendo dados...");
         Dados result = db.search(name);
        if(result == null)
            return "Registro inexistente";
        db.remove(name);
        return "Registro removido!";
    }
}
