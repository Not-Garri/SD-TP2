package Utils;

import java.util.ArrayList;

public class Credentials {

    private final String username;
    private final String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean equals(Credentials c) {
        return username.equals(c.username) && password.equals(c.password);
    }

    public String toString(){
        return "Username: " + username + " Password: " + password;
    }


    public String toFileString() {
        return (username + ";" + password);
    }

    public static Credentials[] listFromFile(String[] list) {

        Credentials[] clientList = new Credentials[list.length];

        for (int i = 0; i < list.length; i++) {
            String[] parts = list[i].split(";");
            clientList[i] = new Credentials(parts[0], parts[1]);
        }

        return clientList;
    }

}
