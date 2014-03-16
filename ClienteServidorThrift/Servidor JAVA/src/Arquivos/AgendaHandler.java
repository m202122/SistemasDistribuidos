package Arquivos;

import ThriftGen.*;
import java.io.IOException;
import java.util.HashMap;

public class AgendaHandler implements Agenda.Iface {
	
	private AgendaDatabase db = null;
	private HashMap log;

	public AgendaHandler() {
		log = new HashMap();
		try {
			db = new AgendaDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getDatabase() {
		try {
			return db.getDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "a";
	}

	public String insert(String name, String fone, int op) {
		System.out.println("Inserindo dados...");
        if(op == 0){
            int i = 0;
            try {
				while( db.search(name + i) != null)
				    ++i;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
				db.insert(new Dados(name+i,fone));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return "Registro inserido com nome: " + name+i;
        }if(op == 1){
            try {
				db.insert(new Dados(name,fone));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return "Registro atualizado!";
        }if(op == 2){
            try {
				db.insert(new Dados(name,fone));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return "Registro inserido com sucesso!";
        }
        return "error";
	}

	public String search(String name) {
		System.out.println("Procurando dados...");
		Dados result = null;
		try {
			result = db.search(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(result == null)
            return "Registro nao encontrado!";
        return result.toString();
	}

	public String remove(String name) {
		System.out.println("Removendo dados...");
        Dados result = null;
		try {
			result = db.search(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(result == null)
            return "Registro inexistente";
        try {
			db.remove(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "Registro removido!";
	}
}
