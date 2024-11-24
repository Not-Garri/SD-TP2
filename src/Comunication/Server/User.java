/* ------------------------------------------------ *
 *  Trabalho realizado por Rodrigo Garraio, n.23599 *
 *    3. Ano, 1. Semestre, Sistemas Distribuidos    *
 * ------------------------------------------------ */

package Comunication.Server;

import Comunication.ClientInterface;
import Utils.Credentials;

/**
 * <h2>User</h2></p>
 * Agrupa as credencias de um utilizador com a interface de cliente que diz respeito a esse utilizador
 */
public class User {
    Credentials userCredentials;
    ClientInterface clientInterface;

    /**
     * Cria uma nova instancia do objeto User
     * @param userCredentials Credenciais utilizadas pelo utilizador
     * @param clientInterface Interface do cliente em questao
     */
    public User(Credentials userCredentials, ClientInterface clientInterface) {
        this.userCredentials = userCredentials;
        this.clientInterface = clientInterface;
    }
}
