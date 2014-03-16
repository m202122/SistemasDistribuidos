
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

import java.io.IOException;
import java.util.Properties;

public class AgendaDatabase {
	private RecordManager recman;
    private HTree hashtable;
	private FastIterator iter;
	
	public AgendaDatabase() throws IOException{
		recman = RecordManagerFactory.createRecordManager( "agendaDB", new Properties() );
		long recid = recman.getNamedObject( "agenda");
		if(recid != 0){
			System.out.println("Loading DB...");
			hashtable = HTree.load(recman, recid);
		}else{
			System.out.println("Creating new DB...");
			hashtable = HTree.createInstance(recman);
			recman.setNamedObject("agenda", hashtable.getRecid());
		}
	}
	
	public void showDatabase() throws IOException{
		System.out.println("DATABASE: ");
		iter = hashtable.keys();
		String name = (String) iter.next();
		while( name != null){
			System.out.print( " " + name);
			name = (String) iter.next();
		}
		System.out.println();
		
	}
	
	public synchronized String getDatabase() throws IOException{
		iter = hashtable.keys();
		String db = "";
		String name = (String) iter.next();
		while( name != null){
			db +=  " " + "\'"+ name + "\'";
			name = (String) iter.next();
		}
		return db;
	}
	
	public synchronized void insert(Dados data) throws IOException{
		hashtable.put(data.getName(),data);
		recman.commit();
	}
	
	public synchronized Dados search(String name) throws IOException{
		return (Dados)hashtable.get(name);
	}
	
	public synchronized void remove(String name) throws IOException {
		hashtable.remove(name);
		recman.commit();
	}
	
}

