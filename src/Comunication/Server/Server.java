package Comunication.Server;

import Comunication.ClientInterface;
import Comunication.ServerInterface;
import Utils.ComunicationCode;
import Utils.Credentials;
import Utils.FileManager;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Server extends UnicastRemoteObject implements ServerInterface {

    private static final String PATH = System.getProperty("user.dir") + "\\src\\Files\\";
    private static final String CREDENTIALS_FILE_NAME = "credentials.txt";
    private static final String PRODUCTS_FILE_NAME = "products.txt";

    static FileManager credentialsManager = new FileManager(PATH, CREDENTIALS_FILE_NAME);
    static FileManager productManager = new FileManager(PATH, PRODUCTS_FILE_NAME);

    static Credentials[] validCredentials;
    static ArrayList<User> loggedUsers = new ArrayList<>();

    static ArrayList<Product> productList = Product.listFromFile(productManager.getListFromFile());

    static Semaphore listSemaphore = new Semaphore(1);

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        validCredentials = Credentials.listFromFile(credentialsManager.getListFromFile());

        System.out.println("PRODUCT LIST:");
        for (int i = 0; i < productList.size(); i++)
            System.out.println((i + 1) + ": " + productList.get(i));


        try {

            Server server = new Server();
            LocateRegistry.createRegistry(1234);
            Naming.rebind("rmi://localhost:1234/products", server);
            System.out.println("A aguardar login...");


        } catch (RemoteException e) {
            System.err.println("Erro ao criar servidor RMI");

        } catch (MalformedURLException e) {
            System.err.println("URL para o RMI esta mal formado");
        }

        // ! FECHAR O SCANNER
        sc.close();

    }

    protected Server() throws RemoteException {
        super();
    }

    private void writeListToFile() {

        productManager.writeToFile(productList.getFirst().toFileString());

        for (int i = 1; i < productList.size(); i++) {
            productManager.appendToFile(productList.get(i).toFileString());
        }
    }

    @Override
    public ComunicationCode login(String username, String password, ClientInterface clientInterface) throws RemoteException {
        Credentials userCredentials = new Credentials(username, password);

        for (Credentials c : validCredentials) {
            if(c.equals(userCredentials)) {
                System.out.println("Utilizador " + username + " estabeleceu ligacao ao servidor");
                loggedUsers.add(new User(userCredentials, clientInterface));

                return ComunicationCode.LOGIN_SUCCESS;
            }
        }

        return ComunicationCode.LOGIN_FAILURE;
    }

    @Override
    public ArrayList<String> searchProductByName(String productName) throws RemoteException{

        ArrayList<String> foundProducts = new ArrayList<>();

        try{
            listSemaphore.acquire();
        } catch (InterruptedException e) {
            //ignored
        }

        for (Product p : productList) {
            if (p.getName().toUpperCase().contains(productName.toUpperCase()))
                foundProducts.add(p.toString());
        }

        listSemaphore.release();

        return foundProducts;
    }

    @Override
    public ArrayList<String> searchProductbyStore(String storeName) throws RemoteException {

        ArrayList<String> foundProducts = new ArrayList<>();

        try{
            listSemaphore.acquire();
        } catch (InterruptedException e) {
            //Ignored
        }

        for (Product p : productList) {
            if (p.getStore().equalsIgnoreCase(storeName))
                foundProducts.add(p.toString());
        }

        listSemaphore.release();
        return foundProducts;
    }

    @Override
    public ArrayList<String> getProductList() throws RemoteException {
        ArrayList<String> listAsString = new ArrayList<>();

        try{
            listSemaphore.acquire();
        } catch (InterruptedException e) {
            //ignored
        }

        for(Product p : productList)
            listAsString.add(p.toString());

        listSemaphore.release();
        return listAsString;
    }

    @Override
    public ComunicationCode findProduct(String productName, String store) throws RemoteException {

        try{
            listSemaphore.acquire();
        } catch (InterruptedException e) {
            //ignored
        }

        for(Product p : productList) {
            if(p.compare(productName, store)) {
                listSemaphore.release();
                return ComunicationCode.PRODUCT_FOUND;
            }
        }

        listSemaphore.release();
        return ComunicationCode.PRODUCT_NOT_FOUND;
    }

    @Override
    public ComunicationCode addProduct(String name, Float price, String store, String username, String date) {

        Product newProduct = new Product(name, price, store, username, date);

        try{
            listSemaphore.acquire();

        } catch (InterruptedException e) {
            //ignored
        }

        //Ve se o produto a adicionar ja existe na lista
        for(Product p : productList) {

            //Se o produto existir
            if(newProduct.compare(p)) {
                listSemaphore.release();

                //Indica que a operacao falhou
                return ComunicationCode.OPERATION_FAILURE;
            }
        }

        productList.add(newProduct);
        productManager.appendToFile(newProduct.toFileString());
        listSemaphore.release();

        return ComunicationCode.OPERATION_SUCCESS;
    }

    @Override
    public ComunicationCode updateProduct(String name, Float newPrice, String store, String username, String date) {


        try {
            listSemaphore.acquire();
        } catch (InterruptedException e) {
            //Ignored
        }

        for (Product p : productList) {
            //Ve se o nome do produto e da loja sao iguais aos fornecidos pelo utilizador
            if(p.compare(name, store)) {
                p.setPrice(newPrice);
                p.setUsername(username);
                p.setDate(date);
                writeListToFile();
                listSemaphore.release();

                Iterator<User> userIterator = loggedUsers.iterator();

                while(userIterator.hasNext()) {
                    User user = userIterator.next();

                    try {
                        user.clientInterface.notifyClient("O preco do produto " + name + "foi alterado para " + newPrice + "€ pelo user " + username);
                    } catch (RemoteException e) {
                        System.out.println("Nao foi possivel comunicar com o utilizador " + user.userCredentials.getUsername());
                        userIterator.remove();
                    }
                }

                return ComunicationCode.OPERATION_SUCCESS;
            }
        }

        listSemaphore.release();
        return ComunicationCode.OPERATION_FAILURE;

    }


}
