package servidor;

public class Area {
	private double latitude, longitude;
	private double raio;
	
	
	//construtor de area
	public Area (double y, double x){
		this.latitude = y;
		this.longitude = x;
		raio = 300;
	}
	
	//get e set da latitude
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double y) {
		latitude = y;
	}
	
	//get e set da longitude
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double x) {
		longitude = x;
	}
	
	public void setLatLng(double lat, double lng) {
		latitude = lat;
		longitude = lng;
	}
	
	//get e set do raio
	public double getRaio () {
		return raio;
	}
	public void setRaio (double raio) {
		this.raio = raio;
	}
	public void aumentaRaio (double aumenta) {
		raio = raio + aumenta;
	}
	
	//retorna true se a Area deve ser destruida porque eh igual ou menor que o quanto se deve diminuir.
	public boolean diminuiRaio (double diminui) {
		if ( (raio-diminui) > 50 ) {
			raio = raio - diminui;
			return false;
		} else {
			return true;
		}
	}
	
	public boolean estaContido(Ponto ponto) {
		
		Ponto area = new Ponto(latitude, longitude);
		
		if (area.distanciaMetro(ponto) < raio) {
			return true;
		} else {
			return false;
		}
	}
	
	public double interseccaoArea(Area area) {
		
		Ponto centro1 = new Ponto(latitude, longitude);
		Ponto centro2 = new Ponto(area.getLatitude() , area.getLongitude());
		
		Double r = raio;
		Double R = area.getRaio();
		Double d = centro1.distanciaMetro(centro2) ;
		if(R < r){
		    // swap
		    r = area.getRaio();
		    R = raio;
		}
		Double part1 = r*r*Math.acos((d*d + r*r - R*R)/(2*d*r));
		Double part2 = R*R*Math.acos((d*d + R*R - r*r)/(2*d*R));
		Double part3 = 0.5*Math.sqrt((-d+r+R)*(d+r-R)*(d-r+R)*(d+r+R));

		Double intersectionArea = part1 + part2 - part3;
		
		return intersectionArea;
	}
}
