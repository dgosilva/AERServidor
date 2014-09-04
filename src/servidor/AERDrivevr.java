package servidor;

import java.util.List;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;


public class AERDrivevr implements UosDriver{

	public void destroy() {
	}

	public UpDriver getDriver() {
		UpDriver driver = new UpDriver("uos.aerdriver");
		driver.addService("marcarPonto");
		driver.addService("marcarArea");
		return driver;
	}

	public List<UpDriver> getParent() {
		return null;
	}

	public void init(Gateway arg0, InitialProperties arg1, String arg2) {
	}

	public void marcarPonto(Call request, Response response, CallContext ctx){
		int erro = 0;
		Double lon = (Double) request.getParameter("longitude");
		Double lat = (Double) request.getParameter("latidute");
		
		//existe um ponto marcado pelo mesmo jogador no mesmo lugar?
			//adiciona ponto para a lista
		
		if(erro == 0)
			response.addParameter("result", "sucesso");
		else
			response.addParameter("result", "falha");
	}
	
	public void marcarArea(Call request, Response response, CallContext ctx){
		int erro = 0;
		Double lon = (Double) request.getParameter("longitude");
		Double lat = (Double) request.getParameter("latidute");
		
		//est dentro de uma area existente?
			//sim: aumenta raio
			//nao: cria area 
		
		if(erro == 0)
			response.addParameter("result", "sucesso");
		else
			response.addParameter("result", "falha");
	}
	
	public void removerPonto(Call request, Response response, CallContext ctx){
		//TODO
	}
	
	
	
//	void client(){
//		Call marcarPonto = new Call("uos.aerdriver","marcarPonto");
//		marcarPonto.addParameter("longitude", 17.4567);
//		marcarPonto.addParameter("latitude", 45.4567);
//		Response r = gateway.callService(target, marcarPonto);
//		String result = r.getResponseString("result");
//		if(result == "win"){
//			// parabeniza jogador
//		}
//	}
	
}
