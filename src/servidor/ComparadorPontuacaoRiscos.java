package servidor;

import java.util.Comparator;

public class ComparadorPontuacaoRiscos implements Comparator<Jogador>{
	public int compare(Jogador a, Jogador b) {

		if (a.getPontuacaoRiscos() < b.getPontuacaoRiscos()) {
			return -1;
		}
		if (a.getPontuacaoRiscos() > b.getPontuacaoRiscos()) {
			return 1;
		}
		return 0;
    }

}
