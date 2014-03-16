import java.net.*;
import java.io.*;

public class Server{
	private final static int _portNumber = 4444;
	
	public static void main(String[] args){
		try{
			new Server().startServer();
		}catch(Exception e){
			System.out.println("I/0 failure: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void startServer() throws Exception {
		DatagramSocket serverSocket = null;
		AgendaDatabase db = null;
		DatagramPacket _in = null;
		byte[] receiveData = new byte[2048];
		
		System.out.println("Initializing server...");
		try{
			db = new AgendaDatabase();
			serverSocket = new DatagramSocket(_portNumber);
		}catch(IOException e){
			System.err.println("Could not listen on port: " + _portNumber);
			System.exit(-1);
		}
		System.out.println("Server started!");
		try{
			while(true){
				_in = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(_in);
				Thread t = new Thread(new ConnectionRequestHandler(serverSocket, db, new String(_in.getData()), _in.getAddress(), _in.getPort()));
				t.start();
			}
		}catch(Exception e){
			System.err.println("Error in connection attempt.");
			e.printStackTrace();
		}
		
		serverSocket.close();
		
	}
	
	public class ConnectionRequestHandler implements Runnable {
		private DatagramSocket _socket;
		private byte[] sendData = new byte[2048];
		private DatagramPacket _out;
		private String _request = null;
		private InetAddress _ip;
		private int _port;
		private AgendaDatabase _database;
		
		public ConnectionRequestHandler(DatagramSocket socket, AgendaDatabase database, String request,InetAddress ip, int port){
			_socket = socket;
			_database = database;
			_request = request;
			_ip = ip;
			_port = port;
		}
		
		public void run(){
		
			System.out.println("New packet from: " + _ip + ":" + _port);
			try{
				String response = null;
				String request = null;
				String[] words= null;
				int i;
				if(_request != null){
					words = _request.split(" ");
					request = words[0];
					for(i = 1; !words[i].equals("END"); ++i)
						request += " " + words[i];
					response = processRequest(request) + " END ";
					sendData = response.getBytes();
					_out = new DatagramPacket(sendData, sendData.length, _ip, _port);
					synchronized (this) {
						_socket.send(_out);
					}
				}

			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		public String processRequest(String request){
			String response = null;
			String[] words = request.split(" ");
			String name = null;
			String fone = null;
			int j;
			name = words[1];
			for(j = 2; j < words.length -1; ++j)
				name += " " + words[j];
			fone = words[words.length-1];
			int op = Integer.parseInt(words[0]);
			switch(op){
				case 0:
					try{
						response = _database.getDatabase();
					}catch(Exception e){
						e.printStackTrace();
					}
					break;
				case 1:
					try{
						if(_database.search(name) == null){							
							_database.insert(new Dados(name, fone));
							response = "Registro inserido!";
						}else {
							response = "duplicate";
						}
					}catch(Exception e){
						response = "Error: Can't insert into database!";
					}
					break;
				case 2:
					try{
						if(_database.search(name) == null)
							response = "Registro inexistente!";
						else{
							_database.remove(name);
							response = "Registro deletado!";
						}
					}catch(Exception e){
						response = "Error: Registro nao deletado - " + e.getMessage();
					}
					break;
				case 3:
					try{
						Dados reg = _database.search(name);
						if(reg == null)
							response = "Nenhum registro encontrado!";
						else
							response = reg.toString();
					}catch(Exception e){
						response = "Error: can't do search";
					}
					break;
				case 4:
					response = "exit";
					break;
				case 5:
					try{
						int i = 0;
						while( _database.search(name + i) != null)
							++i;
						_database.insert(new Dados(name+i,fone));
						response = "Registro inserido com nome: " + name+i;
					}catch(Exception e){
						response = "Error: Can't insert into database!";
					}
					break;
				case 6:
					try{
						_database.insert(new Dados(name, fone));
						response = "Registro atualizado!";
					}catch(Exception e){
						response = "Error: Can't insert into database!";
					}
					break;
				default:
					System.out.println("Something strange happens!");
			}
			return response;
			
		}
		
	}
	
	
}
