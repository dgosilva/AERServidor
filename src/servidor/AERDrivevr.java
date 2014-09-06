package servidor;

import java.util.List;
import java.util.Set;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

//import android.widget.Toast;

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

	//acessar com [variable] = AERDrivevr.DRIVER_NAME;
	private static final String DRIVER_NAME = "uos.aerdriver";
	
	//considerando uma partida == 24h == 86400s == 86400000ms
	//acessar com [variable] = AERDrivevr.TEMPO_PARTIDA;
	private static final long TEMPO_PARTIDA = 86400000;
	
	
	private Gateway gateway;
	private Set<Jogador> jogadores;
	
	public void destroy() {
	}

	public UpDriver getDriver() {
		
		UpDriver driver = new UpDriver(DRIVER_NAME);
		
		//adiciona servicos
		driver.addService("marcarPonto");
		driver.addService("marcarArea");
		driver.addService("removerPonto");
		driver.addService("removerArea");
		driver.addService("adicionarJogador");
		driver.addService("removerJogador");
		driver.addService("listarJogadores");
		driver.addService("pontuacaoRiscos");
		driver.addService("pontuacaoAreas");
		
		return driver;
	}

	public List<UpDriver> getParent() {
		return null;
	}

	public void init(Gateway arg0, InitialProperties arg1, String arg2) {
	}

	public void marcarPonto(Call request, Response response, CallContext ctx) {
		int erro = 0;
		
		//checa se o jogador ja se encontra na lista de jogadores
		Jogador jogador = (Jogador) request.getParameter("jogador");
		if( jogadores.contains(jogador) == false ){
			erro = 1;
		}
		
		if(erro == 0){
			Ponto ponto = (Ponto) request.getParameter("ponto");
			
			//Fazer o call para o servidor a partir daqui
			if (jogador.getQuantPontos()>=1) {
				if (ponto.distancia(jogador.getUltimoPonto()) < 0.001) {
					//Toast.makeText(this, "Não pode criar um ponto muito próximo do anterior!",Toast.LENGTH_SHORT).show();
					erro = 2;
				} else {
					//remove jogador do set, atualiza e devolve para o set
					jogadores.remove(jogador);
					jogador.adicionaPonto(ponto);
					jogadores.add(jogador);
					
					//TODO como checar colisoes?
					//checaColisoes(jogador.getListaPontos().get(jogador.getQuantPontos()-2), ponto);
					//Toast.makeText(this, "Sua pontuacao:" + jogador.getPontuacaoRiscos(),Toast.LENGTH_SHORT).show();
				}
			} else {
				jogadores.remove(jogador);
				jogador.adicionaPonto(ponto);
				jogadores.add(jogador);
			}
		}
		
		
		//sucesso
		if(erro == 0){
			response.addParameter("result", "sucesso");
		}
		else{
			//jogador nao existe
			if(erro == 1){
				response.addParameter("result", "falha1");
			}
			//ponto muito proximo a outro
			else{
				response.addParameter("result", "falha2");
			}
		}
	}
	
	public void marcarArea(Call request, Response response, CallContext ctx) {
		
		//erro == 1 -> jogador nao existe
		//erro == 2 -> maximo de areas atingido
		int erro = 0;
		
		//checa se o jogador ja se encontra na lista de jogadores
		Jogador jogador = (Jogador) request.getParameter("jogador");
		if( jogadores.contains(jogador) == false ){
			erro = 1;
		}
		
		if(erro == 0){
			
			Double longitude = (Double) request.getParameter("longitude");
			Double latitude = (Double) request.getParameter("latidute");
			int indexArea;
			
			//mudar para if esta dentro de uma de suas areas
			//dentro do if: areaExistente =
			Ponto ponto = new Ponto(latitude, longitude);
			indexArea = jogador.contidoEmAlgumaArea(ponto);
			if (indexArea >=0) {
				jogador.getListaAreas().get(indexArea).aumentaRaio(50);
			} else {
				if (jogador.atingiuMaxAreas()) {
					erro = 2;
					//Toast.makeText(this, "Você já usou todas suas areas",Toast.LENGTH_SHORT).show();
				} else {
					Area area = new Area (latitude, longitude);
					
					//remove jogador do set, atualiza e devolve para o set
					jogadores.remove(jogador);					
					jogador.adicionaArea(area);
					jogadores.add(jogador);
				}
			} 
			
		}		
		
		//sucesso
		if(erro == 0){
			response.addParameter("result", "sucesso");
		}
		else{
			//jogador nao existente
			if(erro == 1){
				response.addParameter("result", "falha1");
			}
			//limite de areas ja alcancado
			else{
				response.addParameter("result", "falha2");
			}
		}
	}
	
	public void removerPonto(Call request, Response response, CallContext ctx) {
		Jogador jogador = (Jogador) request.getParameter("jogador");
		Ponto ponto = (Ponto) request.getParameter("ponto");
		
		if(jogadores.contains(jogador.getListaPontos().contains(ponto))){
			//TODO: faz o que?
			response.addParameter("result", "sucesso");
		}
		else{
			response.addParameter("result", "falha");
		}
	}
	
	public void removerArea(Call request, Response response, CallContext ctx) {
		Jogador jogador = (Jogador) request.getParameter("jogador");
		Area area = (Area) request.getParameter("area");
		
		if(jogadores.contains(jogador.getListaPontos().contains(area))){
			//TODO: faz o que?
			response.addParameter("result", "sucesso");
		}
		else{
			response.addParameter("result", "falha");
		}
	}
	
	public void adicionarJogador(Call request, Response response, CallContext ctx) {
		
		Jogador jogador = (Jogador) request.getParameter("jogador");
		
		//se o jogador ja nao existir, crie
		if( jogadores.contains(jogador) == false ){
			jogadores.add(jogador);
			response.addParameter("result", "sucesso");
		}
		else{
			response.addParameter("result", "falha");
		}
	}
	
	public void removerJogador(Call request, Response response, CallContext ctx) {
		Jogador jogador = (Jogador) request.getParameter("jogador");

		//se o jogador existir, remova
		if( jogadores.contains(jogador) == true ){
			jogadores.remove(jogador);
			response.addParameter("result", "sucesso");
		}
		else{
			response.addParameter("result", "falha");
		}
	}
	
	public void listarJogadores(Call request, Response response, CallContext ctx) {
		for(Jogador j : jogadores){
			//TODO: printar na tela informacoes desejadas dos jogadores
		}
	}
	
	public void pontuacaoRiscos(Call request, Response response, CallContext ctx) {
		//TODO
	}
	
	public void pontuacaoAreas(Call request, Response response, CallContext ctx) {
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
