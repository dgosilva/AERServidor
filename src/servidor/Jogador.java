package servidor;


import java.util.ArrayList;
import java.util.List;

public class Jogador {
	
	String nome;
	
	//acessar com: x = Jogador.MAX_PONTOS;
	public static final int MAX_PONTOS = 10;
	public static final int MAX_AREAS = 10;

	//int do tipo 0xff223344
	//ff é a transparencia. 22 red, 33 green, 44 blue
	private int cor;
	private double pontuacaoRiscos;
	private double pontuacaoAreas;
	private int quantPontos;
	private int quantAreas;
	private List<Ponto> listaPontos = new ArrayList<Ponto>();
	private List<Area> listaAreas = new ArrayList<Area>();
	
	
	//construtor
	public Jogador (String nome, int cor) {
		this.nome = nome;
		this.cor = cor;
		pontuacaoRiscos = 0;
		pontuacaoAreas = 0;
		quantPontos = 0;
	}
	
	public void setNome (String nome) {
		this.nome = nome;
	}
	public String getNome () {
		return nome;
	}
	
	public void setCor (int cor) {
		this.cor = cor;
	}
	public int getCor () {
		return cor;
	}
	
	public void setPontuacaoRiscos (double pontuacao) {
		this.pontuacaoRiscos = pontuacao;
	}
	public void adicionaPontuacaoRiscos (double pontos) {
		pontuacaoRiscos = pontuacaoRiscos + pontos;
	}
	public double getPontuacaoRiscos () {
		return pontuacaoRiscos;
	}
	public void atualizaPontuacaoRiscos() {
		int i;
		double pontos = 0;
		
		for (i=0;i<getQuantPontos()-1;i++) {
			pontos = pontos + listaPontos.get(i).distanciaMetro(listaPontos.get(i+1));
		}
		setPontuacaoRiscos (pontos);
	}
	
	public void setPontuacaoAreas (double pontuacao) {
		this.pontuacaoAreas = pontuacao;
	}
	public void adicionaPontuacaoAreas (double pontos) {
		pontuacaoAreas = pontuacaoAreas + pontos;
	}
	public double getPontuacaoAreas () {
		return pontuacaoAreas;
	}
	
	public void setListaPontos (List<Ponto> listaPontos) {
		this.listaPontos = listaPontos;
	} 
	public double adicionaPonto (Ponto ponto) {
		double distancia = 0;
		if (listaPontos.size()>0) {
			distancia = ponto.distanciaMetro(listaPontos.get(listaPontos.size()-1));
			adicionaPontuacaoRiscos (distancia);
		}
		
		listaPontos.add(ponto);
		quantPontos ++;
		
		return distancia;
	}
	public List<Ponto> getListaPontos () {
		return listaPontos;
	}
	//funcao nao checa se nao existe nenhum ponto, precisa tomar cuidado com isso
	public Ponto getUltimoPonto() {
		return listaPontos.get(listaPontos.size()-1);
	}
	
	public void setListaAreas (List<Area> listaAreas) {
		this.listaAreas = listaAreas;
	} 
	public void adicionaArea (Area area) {
				
		listaAreas.add(area);
		quantAreas ++;
	}
	public List<Area> getListaAreas () {
		return listaAreas;
	}
	
	public void setQuantPontos (int n) {
		quantPontos = n;
	}
	public void incrementaQuantPontos () {
		quantPontos ++;
	}
	public void decrementaQuantPontos () {
		quantPontos --;
	}
	public int getQuantPontos () {
		return quantPontos;
	}
	
	public void setQuantAreas (int n) {
		quantAreas = n;
	}
	public void incrementaQuantAreas () {
		quantAreas ++;
	}
	public void decrementaQuantAreas () {
		quantAreas --;
	}
	public int getQuantAreas () {
		return quantAreas;
	}
	
	public boolean atingiuMaxPontos () {
		if (getQuantPontos() >= MAX_PONTOS) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean atingiuMaxAreas () {
		if (getQuantAreas() >= MAX_AREAS) {
			return true;
		} else {
			return false;
		}
	}
	
	public int contidoEmAlgumaArea(Ponto ponto) {
		int i, indice;
		double max = 0;
		
		indice = -1;
		
		for (i=0; i < listaAreas.size(); i++) {
			if (listaAreas.get(i).estaContido(ponto)) {
				if (listaAreas.get(i).getRaio() > max) {
					max = listaAreas.get(i).getRaio();
					indice = i;
				}
			}
		}
		
		return indice;
	}
}
