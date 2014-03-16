package Arquivos;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import java.util.HashMap;
import ThriftGen.*;

public class Server {
	
	public static void main(String[] args) {
		try {

			AgendaHandler handler = new AgendaHandler();
			Agenda.Processor processor = new Agenda.Processor(handler);
			TServerTransport serverTransport = new TServerSocket(9090);
			TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
			//TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

			// Use this for a multithreaded server
			// server = new TThreadPoolServer(new Args(serverTransport).processor(processor));

			System.out.println("Starting server...");
			server.serve();
		} catch(Exception x) {
			x.printStackTrace();
		}
		System.out.println("done");
	}
}

