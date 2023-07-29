package sg.edu.np.mad.mad2023_team2.ui.Currency_Converter;


public class Countries {

    private String country_name;

    private String country_code;

    private String countryflag;

    public Countries(String country_name, String country_code, String countryflag) {
        this.country_name = country_name;
        this.country_code = country_code;
        this.countryflag = countryflag;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountryflag() {
        return countryflag;
    }

    public void setCountryflag(String countryflag) {
        this.countryflag = countryflag;
    }
}
