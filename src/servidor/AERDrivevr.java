package servidor;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.unbiquitous.uos.core.InitialProperties;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDriver;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

//import android.widget.Toast;



//import com.amazonaws.AmazonClientException;
//import com.amazonaws.AmazonServiceException;
//import com.amazonaws.regions.Region;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.Bucket;
//import com.amazonaws.services.s3.model.GetObjectRequest;
//import com.amazonaws.services.s3.model.ListObjectsRequest;
//import com.amazonaws.services.s3.model.ObjectListing;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectSummary;


//adicionar ao pom:
//<dependency>
//<groupId>com.amazonaws</groupId>
//<artifactId>aws-java-sdk</artifactId>
//<version>1.8.3</version>
//</dependency>


public class AERDrivevr implements UosDriver{

	//acessar com [variable] = AERDrivevr.DRIVER_NAME;
	private static final String DRIVER_NAME = "uos.aerdriver";
	
	//considerando uma partida == 24h == 86400s == 86400000ms
	//acessar com [variable] = AERDrivevr.TEMPO_PARTIDA;
	private static final long TEMPO_PARTIDA = 86400000;
	
	
	private Gateway gateway;
	//private AsyncCallService messenger = null;
	private Set<Jogador> jogadores;
	
	public void destroy() {
	}

	public UpDriver getDriver() {
		
		UpDriver driver = new UpDriver(DRIVER_NAME);
		
		//adiciona servicos
		driver.addService("marcarPonto");
		driver.addService("marcarArea");
		//driver.addService("removerPonto");
		//driver.addService("removerArea");
		driver.addService("adicionarJogador");
		driver.addService("removerJogador");
		//driver.addService("listarJogadores");
		//driver.addService("pontuacaoRiscos");
		//driver.addService("pontuacaoAreas");
		
		return driver;
	}

	public List<UpDriver> getParent() {
		return null;
	}

	public void init(Gateway arg0, InitialProperties arg1, String arg2) {
		gateway = arg0;
	}
	
	//SERVICO
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
					
					checaColisoes(jogador, jogador.getListaPontos().get(jogador.getQuantPontos()-2), ponto);
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
	
	//SERVICO	
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
	
	public void removerPonto(Jogador jogador, Ponto ponto) {
				
		if(jogadores.contains(jogador.getListaPontos().contains(ponto))){
			
			List<Ponto> listaPontos = jogador.getListaPontos();
			double distancia1, distancia2;
			int i, index;
			
			if (listaPontos.indexOf(ponto) == jogador.getQuantPontos()-1) {
				listaPontos.remove(ponto);
				jogador.decrementaQuantPontos();
				return;
			}
			if (listaPontos.indexOf(ponto) == 1) {
				listaPontos.remove(0);
				jogador.decrementaQuantPontos();
				return;
			}
			
			distancia1=0;
			distancia2=0;
			
			for (i=0; i < listaPontos.indexOf(ponto)-1; i++) {
				distancia1 = distancia1 + listaPontos.get(i).distancia(listaPontos.get(i+1));
			}
			
			for (i=listaPontos.indexOf(ponto); i < jogador.getQuantPontos()-1; i++) {
				distancia2 = distancia2 + listaPontos.get(i).distancia(listaPontos.get(i+1));
			}
			
			index = listaPontos.indexOf(ponto);
			if (distancia1 > distancia2) {
				while (jogador.getQuantPontos() > index) {
					//remove ultimo da lista
					listaPontos.remove(jogador.getQuantPontos()-1);
					jogador.decrementaQuantPontos();
				}
			} else {
				while (listaPontos.get(0) != ponto) {
					listaPontos.remove(0);
					jogador.decrementaQuantPontos();
				}
			}
		}
		else{
			//TODO: MENSAGEM DE ERRO
		}
	}
	
	public void removerArea(Jogador jogador, Area area) {
		
		if(jogadores.contains(jogador.getListaPontos().contains(area))){
			//TODO: faz o que?
			//REMOVE A AREA
		}
		else{
			//MENSAGEM DE ERRO
		}
	}
	
	//SERVICO	
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
	
	//SERVICO	
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
	
	//inutilizado por enquanto
	public void listarJogadores(Call request, Response response, CallContext ctx) {
		for(Jogador j : jogadores){
			//TODO: printar na tela informacoes desejadas dos jogadores
		}
	}
	
	public void pontuacaoRiscos(Call request, Response response, CallContext ctx) {	
		List<Jogador> lista = null;
		for(Jogador j : jogadores){
			lista.add(j);
		}	
		Collections.sort(lista, new ComparadorPontuacaoRiscos());
	}
	
	public void pontuacaoAreas(Call request, Response response, CallContext ctx) {
		List<Jogador> lista = null;
		for(Jogador j : jogadores){
			lista.add(j);
		}	
		Collections.sort(lista, new ComparadorPontuacaoAreas());
	}
	
	public void checaColisoes (Jogador jogador, Ponto novo1, Ponto novo2) {
		double distancia = novo1.distancia(novo2);
		double distvelho;
		boolean perdeu = false;
		
		//loop que passa por todos os jogadores do set
		for(Jogador j : jogadores) {
			//condicao para nao comparar o jogador consigo mesmo
			if(j != jogador){
				if (j.getQuantPontos()>1) {
					int i = 0;
					List<Ponto> listaPontos = j.getListaPontos();
					for (i=0;i<j.getQuantPontos()-1;i++) {
						if (checarInterseccaoReta(novo1, novo2, listaPontos.get(i), listaPontos.get(i+1))) {
							Random rand = new Random(System.currentTimeMillis());
							double numAleatorio = rand.nextDouble();
							
							distvelho = listaPontos.get(i).distancia(listaPontos.get(i+1));
							
							numAleatorio = numAleatorio * (distancia + distvelho);
							if (numAleatorio < distvelho) {
								//Toast.makeText(this, "Ganhou a luta",Toast.LENGTH_SHORT).show();
								removerPonto(j, listaPontos.get(i+1));
								i=0;
								//TODO avisar jogador2 q perdeu
							}
							else {
								//Toast.makeText(this, "Perdeu a luta",Toast.LENGTH_SHORT).show();
								perdeu = true;
							}
						}
					}
				}
			}
		}
		
		if (perdeu == true) {
			removerPonto(jogador, novo2);
		}
	}
	
	public double max (double a, double b) {
		if (a > b) {
			return a;
		}
		else {
			return b;
		}
	}
	
	public double min (double a, double b) {
		if (a < b) {
			return a;
		}
		else {
			return b;
		}
	}
		
	public boolean checarInterseccaoReta(Ponto novo1, Ponto novo2, Ponto velho1, Ponto velho2){	
		
		double a, b;
		
		//Teste do envelope
		if (max(novo1.getLatitude(),novo2.getLatitude()) < min(velho1.getLatitude(),velho2.getLatitude())) {
			return false;
		}
		if (min(novo1.getLatitude(),novo2.getLatitude()) > max(velho1.getLatitude(),velho2.getLatitude())) {
			return false;
		}
		if (max(novo1.getLongitude(),novo2.getLongitude()) < min(velho1.getLongitude(),velho2.getLongitude())) {
			return false;
		}
		if (min(novo1.getLongitude(),novo2.getLongitude()) > max(velho1.getLongitude(),velho2.getLongitude())) {
			return false;
		}
		
		//equacao da reta y=ax+b
		a = ((novo1.getLatitude() - novo2.getLatitude())/(novo1.getLongitude() - novo2.getLongitude()));
		b = novo1.getLatitude() - (novo1.getLongitude() * a);
		if (MesmoLado(a, b, velho1, velho2)) {
			return false;
		}
		
		a = ((velho1.getLatitude() - velho2.getLatitude())/(velho1.getLongitude() - velho2.getLongitude()));
		b = velho1.getLatitude() - (velho1.getLongitude() * a);
		if (MesmoLado(a, b, novo1, novo2)) {
			return false;
		}
		
		return true;
		
		//parte antiga que nao funcionou
		/*if(determinante(novo1, novo2, velho1, velho2) == 0.0) {
			return false; //nao ha interseccao
		}
		return true; //ha interseccao*/
		
		/*Line2D line1 = new Line2D.Float(100, 100, 200, 200);
		return linesIntersect(
				novo1.getLongitude(),
				novo1.getLatitude(),
				novo2.getLongitude(),
				novo2.getLatitude(),
				velho1.getLongitude(),
				velho1.getLatitude(),
				velho2.getLongitude(),
				velho2.getLatitude());*/
	}
	
	//retorna true se os dois pontos estao no mesmo lado da reta
	public boolean MesmoLado(double a, double b, Ponto ponto1, Ponto ponto2) {
		double x, y;

		x = ponto1.getLongitude();
		y = (-1*ponto1.getLatitude());
		if (((a*x) + y + b) > 0) {
			x = ponto2.getLongitude();
			y = (-1*ponto2.getLatitude());
			if (((a*x) + y + b) > 0) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			x = ponto2.getLongitude();
			y = (-1*ponto2.getLatitude());
			if (((a*x) + y + b) < 0) {
				return true;
			}
			else {
				return false;
			}
		}		
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
