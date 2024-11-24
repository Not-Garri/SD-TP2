/* ------------------------------------------------ *
 *  Trabalho realizado por Rodrigo Garraio, n.23599 *
 *    3. Ano, 1. Semestre, Sistemas Distribuidos    *
 * ------------------------------------------------ */

package Comunication;

import Utils.ComunicationCode;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

//Metodos que vao ser executados no servidor
public interface ServerInterface extends Remote {
    ComunicationCode login(String username, String password, ClientInterface clientInterface) throws RemoteException;
    ArrayList<String> searchProductByName(String productName) throws RemoteException;
    ArrayList<String> searchProductbyStore(String storeName) throws RemoteException;
    ArrayList<String> getProductList() throws RemoteException;
    ComunicationCode findProduct(String productName, String store) throws RemoteException;
    ComunicationCode addProduct(String name, Float price, String store, String username, String date) throws RemoteException;
    ComunicationCode updateProduct(String name, Float newPrice, String store, String username, String date) throws RemoteException;


}

