/* ------------------------------------------------ *
 *  Trabalho realizado por Rodrigo Garraio, n.23599 *
 *    3. Ano, 1. Semestre, Sistemas Distribuidos    *
 * ------------------------------------------------ */

package Utils;

import java.util.ArrayList;

/**
 * <h2>Credentials</h2></p>
 * Classe que agrupa o usernames e as passwords de acesso validas
 */
public class Credentials {

    private final String username;
    private final String password;

    /**
     * Cria um novo objeto Credentials
     * @param username Username a ser guardado
     * @param password Password respetiva ao username
     */
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Retorna o username de um objeto Credentials
     * @return Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Compara as credenciais de acesso do objeto que invoca o metodo com as do objeto passado como argumento
     * @param c Credenciais a serem comparadas
     * @return {@code true} se as credenciais forem iguais, {@code false} caso contrario
     */
    public boolean equals(Credentials c) {
        return username.equals(c.username) && password.equals(c.password);
    }

    /**
     * Retorna as credenciais de acesso na forma de ums String
     * @return Username e password numa so string
     */
    public String toString(){
        return "Username: " + username + " Password: " + password;
    }

    /**
     * Cria uma string com o username e a password separados por ponto e virgula</p>
     * Para uso em ficheiros CSV
     * @return String com o username e a password separados com um ponto e virgula
     */
    public String toFileString() {
        return (username + ";" + password);
    }

    /**
     * Cria um array de credenciais com base na lista de strings recebida.</p>
     * Este metodo assume que as strings recebidas tem o username e a password separados por ponto e virgula
     * @param list Lista de strings a ser transformada
     * @return Array de credenciais validas
     */
    public static Credentials[] listFromFile(String[] list) {

        Credentials[] clientList = new Credentials[list.length];

        for (int i = 0; i < list.length; i++) {
            String[] parts = list[i].split(";");
            clientList[i] = new Credentials(parts[0], parts[1]);
        }

        return clientList;
    }

}
