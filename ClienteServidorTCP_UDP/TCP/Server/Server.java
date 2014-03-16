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
		
		ServerSocket serverSocket = null;
		AgendaDatabase db = null;
		
		System.out.println("Initializing server...");
		try{
			db = new AgendaDatabase();
			serverSocket = new ServerSocket(_portNumber);
		}catch(IOException e){
			System.err.println("Could not listen on port: " + _portNumber);
			System.exit(-1);
		}
		System.out.println("Server started!");
		try{
			while(true){
				
				Socket clientSocket = serverSocket.accept();
				System.out.println("Accepted connection: " + clientSocket);
				Thread t = new Thread(new ConnectionRequestHandler(clientSocket,db));
				t.start();
			}
		}catch(Exception e){
			System.err.println("Error in connection attempt.");
		}
		serverSocket.close();
		
	}
	
	public class ConnectionRequestHandler implements Runnable {
		private Socket _socket = null;
		private PrintWriter _out = null;
		private BufferedReader _in = null;
		private AgendaDatabase _database = null;
		
		public ConnectionRequestHandler(Socket socket, AgendaDatabase database){
			_socket = socket;
			_database = database;
		}
		
		public void run(){
		
			System.out.println("Client connected to socket: " + _socket.toString());
			
			try{
				
				_out = new PrintWriter(_socket.getOutputStream(), true);
				_in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));

				String request;
				while(( request = _in.readLine()) != null){
					String response = processRequest(request);
					_out.println(response);
					if(response.equals("exit"))
						break;
				}

				
				
			}catch(IOException e){
				e.printStackTrace();
			}
			finally {
				try{
					_out.close();
					_in.close();
					_socket.close();
				}catch(Exception e){
					System.out.println("Couldn't close I/O streams");
				}
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
