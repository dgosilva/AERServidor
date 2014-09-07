package servidor;

import java.util.Comparator;

public class ComparadorPontuacaoAreas implements Comparator<Jogador>{
	public int compare(Jogador a, Jogador b) {

		if (a.getPontuacaoAreas() < b.getPontuacaoAreas()) {
			return -1;
		}
		if (a.getPontuacaoAreas() > b.getPontuacaoAreas()) {
			return 1;
		}
		return 0;
    }

}