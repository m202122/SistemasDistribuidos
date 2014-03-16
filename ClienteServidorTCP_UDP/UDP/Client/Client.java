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
		DatagramSocket socket = null;
		byte[] sendData = new byte[2048];
		byte[] receiveData = new byte[2048];
		DatagramPacket out = null;
		DatagramPacket in = null;
		BufferedReader stdIn = null;
		InetAddress IP = null;
		
		try{
			socket = new DatagramSocket();
			IP = InetAddress.getByName(host);
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			String fromServer = null;
			String fromUser = null;
			String[] words = null;
			int i;
			
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
						sendData = new String("0 database 0 END ").getBytes() ;
						out = new DatagramPacket(sendData, sendData.length, IP, port);
						socket.send(out);
						in = new DatagramPacket(receiveData, receiveData.length);
						socket.receive(in);
						System.out.println("DATABASE");
						fromServer = new String(in.getData());
						words = fromServer.split(" ");
						fromServer = words[0];
						for(i = 1; !words[i].equals("END"); ++i)
							fromServer += " " + words[i];
						System.out.println(fromServer);
						break;
					case 1:
						System.out.print("Insira o nome: ");
						String name = stdIn.readLine();
						System.out.print("\nInsira o telefone: ");
						String fone = stdIn.readLine();
						sendData = new String("1 " + name + " " + fone + " END ").getBytes();
						out = new DatagramPacket(sendData, sendData.length, IP, port);
						socket.send(out);
						in = new DatagramPacket(receiveData, receiveData.length);
						socket.receive(in);
						fromServer = new String(in.getData());
						words = fromServer.split(" ");
						fromServer = words[0];
						for(i = 1; !words[i].equals("END"); ++i)
							fromServer += " " + words[i];
						if(fromServer.equals("duplicate")){
							System.out.println("Ja existe um registro com esse nome, substituir? (S/N)");
							fromUser = stdIn.readLine();
							
							if(fromUser.toLowerCase().equals("n"))
								sendData = new String("5 " + name + " " + fone + " END ").getBytes();
							else
								sendData = new String("6 " + name + " " + fone + " END ").getBytes();
								
							out = new DatagramPacket(sendData, sendData.length, IP, port);
							socket.send(out);
							in = new DatagramPacket(receiveData, receiveData.length);
							socket.receive(in);
							fromServer = new String(in.getData());
							words = fromServer.split(" ");
							fromServer = words[0];
							for(i = 1; !words[i].equals("END"); ++i)
								fromServer += " " + words[i];
							System.out.println("Server: " + fromServer);
						}
						else
							System.out.println("Server: " + fromServer);
						break;
					case 2:
						System.out.print("Insira nome do registro a ser deletado: ");
						fromUser = stdIn.readLine();
						sendData = new String("2 " + fromUser + " 0 END ").getBytes();
						out = new DatagramPacket(sendData, sendData.length, IP, port);
						socket.send(out);
						in = new DatagramPacket(receiveData, receiveData.length);
						socket.receive(in);
						fromServer = new String(in.getData());
						words = fromServer.split(" ");
						fromServer = words[0];
						for(i = 1; !words[i].equals("END"); ++i)
							fromServer += " " + words[i];
						if(fromServer != null)
							System.out.println("Server: " + fromServer);
						break;
					case 3:
						System.out.print("Insira nome do registro a ser recuperado: ");
						fromUser = stdIn.readLine();
						sendData  = new String("3 " + fromUser + " 0 END ").getBytes();
						out = new DatagramPacket(sendData, sendData.length, IP, port);
						socket.send(out);
						in = new DatagramPacket(receiveData, receiveData.length);
						socket.receive(in);
						fromServer = new String(in.getData());
						words = fromServer.split(" ");
						fromServer = words[0];
						for(i = 1; !words[i].equals("END"); ++i)
							fromServer += " " + words[i];
						System.out.println("Registro - " + fromServer);
						break;
					case 4:
						sendData = new String("4 exit 0 END ").getBytes();
						out = new DatagramPacket(sendData, sendData.length, IP, port);
						socket.send(out);
						in = new DatagramPacket(receiveData, receiveData.length);
						
						socket.receive(in);
						fromServer = new String(in.getData());
						words = fromServer.split(" ");
						fromServer = words[0];
						for(i = 1; !words[i].equals("END"); ++i)
							fromServer += " " + words[i];
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
			e.printStackTrace();
			System.err.println("Couldn't read/write from the connection: " + e.getMessage());
			
			System.exit(1);
		}finally{
			stdIn.close();
		}
	}
}
