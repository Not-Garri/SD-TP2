/* ------------------------------------------------ *
 *  Trabalho realizado por Rodrigo Garraio, n.23599 *
 *    3. Ano, 1. Semestre, Sistemas Distribuidos    *
 * ------------------------------------------------ */

package Comunication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void notifyClient(String notification) throws RemoteException;

}
