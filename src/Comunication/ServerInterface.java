package Comunication;

import Utils.ComunicationCode;

import javax.xml.stream.events.Comment;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

//Metodos que vao ser executados no servidor
public interface ServerInterface extends Remote {

    //TODO: Validar Login (basicamente metodo login que recebe o username e a password)
    /*
    TODO: Funcionalidades de produtos:
     - Listar todos os produtos
     - Atualizar preco de um dado produto (o que deve alterar o ficheiro)
     - adicionar produtos (o que deve alterar o ficheiro)
     - Notificar todos os clientes de que um produto foi alterado (maybe client-side, idk)
     */

    ComunicationCode login(String username, String password, ClientInterface clientInterface) throws RemoteException;
    ArrayList<String> searchProductByName(String productName) throws RemoteException;
    ArrayList<String> searchProductbyStore(String storeName) throws RemoteException;
    ArrayList<String> getProductList() throws RemoteException;
    ComunicationCode addProduct(String name, Float price, String store, String username, String date) throws RemoteException;
    ComunicationCode updateProduct(String name, Float price, String store, String username, String date) throws RemoteException;


}

