package servidor;

import org.unbiquitous.network.http.connection.ServerMode;
import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.UOS;

public class Main {

	public static void main(String[] args){
		UOS uos = new UOS();
		
//		InitialProperties config = new ServerMode.Properties();
//		config.addDriver(AERDrivevr.class);
//		uos.start(config);
		
	    uos.start(new ServerMode.Properties());
	}
	
	
}
