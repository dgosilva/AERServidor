package servidor;

public class Ponto {
	private double latitude, longitude;
	
	
	//construtor de ponto
	public Ponto (double y, double x){
		this.latitude = y;
		this.longitude = x;
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
	
	
	public double distancia (Ponto p2) {
		Ponto p1 = this;
		return Math.sqrt(
            (p1.getLatitude() - p2.getLatitude()) *  (p1.getLatitude() - p2.getLatitude()) + 
            (p1.getLongitude() - p2.getLongitude()) *  (p1.getLongitude() - p2.getLongitude())
        );	
	}	
	
}
