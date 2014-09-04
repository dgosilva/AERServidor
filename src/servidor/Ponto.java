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
	
	public double distanciaMetro(Ponto ponto){  // generally used geo measurement function
	    
		double lat1, lon1, lat2, lon2;
		lat1 = latitude;
		lon1 = longitude;
		
		lat2 = ponto.getLatitude();
		lon2 = ponto.getLongitude();
		
		double R = 6378.137; // Raio da Terra em KM
	    double dLat = (lat2 - lat1) * Math.PI / 180;
	    double dLon = (lon2 - lon1) * Math.PI / 180;
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	    Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
	    Math.sin(dLon/2) * Math.sin(dLon/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double d = R * c;
	    return d * 1000; // meters
	}
	
}
