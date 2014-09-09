package servidor;

import org.unbiquitous.network.http.connection.ServerMode;
import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.UOS;

public class Main {

	public static void main(String[] args){
		UOS uos = new UOS();

		ServerMode.Properties properties = new ServerMode.Properties();
		properties.put("servidor", AERDrivevr.class.getName());

		uos.start(properties);
	}
		
	
}
