package servidor;

import org.unbiquitous.network.http.connection.ServerMode;
import org.unbiquitous.uos.core.UOS;

public class Main {

	public static void main(String[] args){
		UOS uos = new UOS();
	    uos.start(new ServerMode.Properties());
	}
	
	
}
