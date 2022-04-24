public class Cidade {

    private double latitude;
    private double longitude;
    private String nome;
    public static final double R = 6372.8; // raio da Terra em km

    public Cidade(String n, double lat, double lon) {
        latitude = lat;
        longitude = lon;
        nome = n;
    }

    public String getNome() {
        return nome;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double distancia(Cidade outra) {

        double lat1 = latitude;
        double lon1 = longitude;
        double lat2 = outra.getLatitude();
        double lon2 = outra.getLongitude();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

}
