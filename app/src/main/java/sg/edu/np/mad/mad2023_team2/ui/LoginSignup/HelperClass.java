package sg.edu.np.mad.mad2023_team2.ui.LoginSignup;

public class HelperClass {

    String email, username, password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HelperClass(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public HelperClass() {
    }

}