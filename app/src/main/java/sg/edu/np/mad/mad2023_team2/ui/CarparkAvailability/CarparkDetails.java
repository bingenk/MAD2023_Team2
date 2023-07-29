package sg.edu.np.mad.mad2023_team2.ui.CarparkAvailability;

public class CarparkDetails {
    private  int id;
    private String area;
    private String development;
    private int availableLots;
    private double latitude;
    private double longitude;

    public CarparkDetails(int id, String area, String development, int availableLots, double latitude, double longitude) {
        this.id = id;
        this.area = area;
        this.development = development;
        this.availableLots = availableLots;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getArea() {
        return area;
    }

    public String getDevelopment() {
        return development;
    }

    public int getAvailableLots() {
        return availableLots;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }
}

