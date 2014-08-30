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
}
