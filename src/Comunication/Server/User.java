package Comunication.Server;

import Comunication.ClientInterface;
import Utils.Credentials;

public class User {
    Credentials userCredentials;
    ClientInterface clientInterface;

    public User(Credentials userCredentials, ClientInterface clientInterface) {
        this.userCredentials = userCredentials;
        this.clientInterface = clientInterface;
    }
}
