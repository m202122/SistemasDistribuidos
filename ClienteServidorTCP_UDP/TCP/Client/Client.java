import java.io.*;
import java.net.*;

public class Client{
	

	public static void main(String[] args){
		if(args.length < 2){
			System.out.println("Usage: Client <host> <port>");		
			System.exit(-1);
		}
		try{
			new Client().startClient(args[0],Integer.parseInt(args[1]));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void startClient(String host, int port) throws IOException{
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		BufferedReader stdIn = null;
		
		try{
			socket = new Socket(host, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer = null;
			String fromUser = null;
			
			while(true){
				
				System.out.print("\u001b[2J");
				System.out.println("[=====> AGENDA <=====]");
				System.out.println("O que deseja Fazer? \n");
				System.out.println("0-Imprimir todos os registros");
				System.out.println("1-Armazenar/Atualizar");
				System.out.println("2-Remover");
				System.out.println("3-Recuperar");
				System.out.println("4-Sair\n");
				System.out.print("Opcao: ");
				fromUser = stdIn.readLine();
				System.out.print("\u001b[2J");
				
				switch(Integer.parseInt(fromUser)){
					case 0:
						out.println("0 database 0");
						System.out.println("DATABASE");
						fromServer = in.readLine();
						System.out.println(fromServer);
						break;
					case 1:
						System.out.print("Insira o nome: ");
						String name = stdIn.readLine();
						System.out.print("\nInsira o telefone: ");
						String fone = stdIn.readLine();
						out.println("1 " + name + " " + fone);
						fromServer = in.readLine();
						if(fromServer.equals("duplicate")){
							System.out.println("Ja existe um registro com esse nome, substituir? (S/N)");
							fromUser = stdIn.readLine();
							if(fromUser.toLowerCase().equals("n"))
								out.println("5 " + name + " " + fone);
							else
								out.println("6 " + name + " " + fone);
							fromServer = in.readLine();
							System.out.println("Server: " + fromServer);
						}
						else
							System.out.println("Server: " + fromServer);
						break;
					case 2:
						System.out.print("Insira nome do registro a ser deletado: ");
						fromUser = stdIn.readLine();
						out.println("2 " + fromUser + " 0");
						fromServer = in.readLine();
						if(fromServer != null)
							System.out.println("Server: " + fromServer);
						break;
					case 3:
						System.out.print("Insira nome do registro a ser recuperado: ");
						fromUser = stdIn.readLine();
						out.println("3 " + fromUser + " 0");
						fromServer = in.readLine();
						System.out.println("Registro - " + fromServer);
						break;
					case 4:
						out.println("4 exit 0");
						fromServer = in.readLine();
						break;
					default:
					System.out.println("Opcao invalida!");
					
				}
				
				if(fromServer.equals("exit")){
					System.out.println("Fechando do cliente...");
					break;
				}
					
				System.out.println("Pressione ENTER para continuar...");
				stdIn.readLine();
			
			}
			
			
		}catch(UnknownHostException e){
			System.err.println("Connot find the host: " + host);
			System.exit(1);
		}catch(IOException e){
			System.err.println("Couldn't read/write from the connection: " + e.getMessage());
			System.exit(1);
		}finally{
			out.close();
			in.close();
			stdIn.close();
			socket.close();
		}
	}
}
