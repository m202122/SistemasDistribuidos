import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    public static void main(String[] args) throws IOException {
        String serverName;
        BufferedReader stdIn;
        String fromUser;

        if (args.length == 0)
            serverName = "127.0.0.1";
        else
            serverName = args[0];

        String rmiName = "rmi://" + serverName + "/AgendaService";
        System.out.println ("Connecting to: " + rmiName+"\n\n\n\n\n\n\n\n\n");


        try {
            AgendaInterface a = (AgendaInterface) Naming.lookup(rmiName);
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            boolean flag = true;

            while(flag){
				System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("[=====> AGENDA <=====]");
                System.out.println("O que deseja Fazer? \n");
                System.out.println("0-Imprimir todos os registros");
                System.out.println("1-Armazenar/Atualizar");
                System.out.println("2-Remover");
                System.out.println("3-Recuperar");
                System.out.println("4-Sair\n");
                System.out.print("Opcao: ");
                fromUser = stdIn.readLine();

                switch(Integer.parseInt(fromUser)){
                    case 0:
                        System.out.println("DATABASE");
                        System.out.println(a.getDatabase());
                        break;
                    case 1:
                        System.out.print("Insira o nome: ");
                        String name = stdIn.readLine();
                        System.out.print("\nInsira o telefone: ");
                        String fone = stdIn.readLine();
                        if(a.search(name).equals("Registro nao encontrado!")){
                            System.out.println(a.insert(name,fone,2));
                        }else{
                            System.out.println("Ja existe um registro com esse nome, substituir? (S/N)");
                            fromUser = stdIn.readLine();
                            if(fromUser.toLowerCase().equals("s"))
                                System.out.println(a.insert(name,fone,1));
                            else
                                System.out.println(a.insert(name,fone,0));
                        }
                        break;
                    case 2:
                        System.out.print("Insira nome do registro a ser deletado: ");
                        fromUser = stdIn.readLine();
                        System.out.println(a.remove(fromUser));
                        break;
                    case 3:
                        System.out.print("Insira nome do registro a ser recuperado: ");
                        fromUser = stdIn.readLine();
                        System.out.println("Registro> " + a.search(fromUser));
                        break;
                    case 4:
                        flag = false;
						System.out.println("Fechando do cliente...");
                        break;
                    default:
                        System.out.println("Opcao invalida!");

                }

                System.out.println("\nPressione ENTER para continuar...");
                stdIn.readLine();

            }


        }catch (MalformedURLException murle) {
            System.out.println();
            System.out.println("MalformedURLException");
            System.out.println(murle);
        }
        catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        }
        catch (NotBoundException nbe) {
            System.out.println();
            System.out.println("NotBoundException");
            System.out.println(nbe);
        }
        catch (java.lang.ArithmeticException ae) {
            System.out.println();
            System.out.println("java.lang.ArithmeticException");
            System.out.println(ae);
        }
    }

}
