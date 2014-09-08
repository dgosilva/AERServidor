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
		
		ServerMode.Properties properties = new ServerMode.Properties();
		properties.put("servidor", AERDrivevr.class.getName());
//		properties.put("ubiquitos.websocket.messageBufferSize", 2 * 1024 * 1024); // 2 mb pra garantir
		
//	    uos.start(new ServerMode.Properties());
		uos.start(properties);
	}
		
	
}
