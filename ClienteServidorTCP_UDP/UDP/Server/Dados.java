import java.io.Serializable;

public class Dados implements Serializable{

	private String name;
	private String fone;	

	public Dados(String name, String fone){
		this.name = name;
		this.fone = fone;
	}

	public String getName(){
		return name;	
	}
	
	public String getFone(){
		return fone;
	}

	public String toString(){
		return "Name: " + name + " Fone: " + fone;
	}
}
