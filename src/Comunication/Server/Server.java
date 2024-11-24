/* ------------------------------------------------ *
 *  Trabalho realizado por Rodrigo Garraio, n.23599 *
 *    3. Ano, 1. Semestre, Sistemas Distribuidos    *
 * ------------------------------------------------ */

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
import java.util.concurrent.Semaphore;

public class Server extends UnicastRemoteObject implements ServerInterface {

    //Diretorio onde sao guardados os ficheiros de produtos e credenciais validas
    private static final String PATH = System.getProperty("user.dir") + "\\src\\Files\\";

    //Nome do ficheiro que guarda as credenciais validas
    private static final String CREDENTIALS_FILE_NAME = "credentials.txt";

    //Nome do ficheiro que guarda os produtos existentes
    private static final String PRODUCTS_FILE_NAME = "products.txt";

    //Objeto que gere o ficheiro de credenciais
    static FileManager credentialsManager = new FileManager(PATH, CREDENTIALS_FILE_NAME);

    //Objeto que gere o ficheiro de produtos
    static FileManager productManager = new FileManager(PATH, PRODUCTS_FILE_NAME);

    //Lista de credenciais de acesso validas
    static Credentials[] validCredentials;

    //Lista de utilizadores atualmente conectados ao sistema
    static ArrayList<User> loggedUsers = new ArrayList<>();

    //Lista de produtos existentes
    static ArrayList<Product> productList = Product.listFromFile(productManager.getListFromFile());

    //Semaforo de controlo de acesso a lista e ao ficheiro de produtos
    static Semaphore listSemaphore = new Semaphore(1);

    public static void main(String[] args) {

        validCredentials = Credentials.listFromFile(credentialsManager.getListFromFile());

        try {

            //Cria uma nova instancia do servidor
            Server server = new Server();

            //Cria registo RMi
            LocateRegistry.createRegistry(1234);

            //Mapeia o registo RMI para o dominio introduzido
            Naming.rebind("rmi://localhost:1234/products", server);
            System.out.println("A aguardar login...");


        } catch (RemoteException e) {
            System.err.println("Erro ao criar servidor RMI");

        } catch (MalformedURLException e) {
            System.err.println("URL para o RMI esta mal formado");
        }
    }

    protected Server() throws RemoteException {
        super();
    }

    /**
     * Escreve a lista de produtos na sua totalidade para o ficheiro de produtos, substituindo os seus conteudos atuais
     */
    private void writeListToFile() {

        //Substitui os conteudos do ficheiro com o primeiro produto na lista
        productManager.writeToFile(productList.getFirst().toFileString());

        //Coloca o resto dos produtos na lista nno ficheiro
        for (int i = 1; i < productList.size(); i++) {
            productManager.appendToFile(productList.get(i).toFileString());
        }
    }

    /**
     * Autentica um novo utilizador</p>
     * Cada utilizador tem uma tentativa de login, se esta tentativa falhar, o login falha
     * @param username Username com que o utilizador tenta fazer login
     * @param password Password com que o utilizador tenta fazer login
     * @param clientInterface Interface que permite usar metodos no registo RMI do cliente
     * @return {@code ComunicationCode.LOGIN_SUCCESS} se o login for feito com sucesso ou
     * {@code ComunicationCode.LOGIN_FAILED} se o login falhar
     * @throws RemoteException Caso nao seja possivel contactar o registo RMI
     */
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

    /**
     * Procura por produtos na lista de produtos que contenham a string {@code productName}
     * @param productName Query que vai ser pesquisada na lista de produto
     * @return Lista de produtos encontrados. Se nao forem encontrados produtos, esta lista fica vazia
     * @throws RemoteException Caso nao seja possivel contactar o registo RMI
     */
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

    /**
     * Procura por produtos na lista de produtos que sejam vendidos na loja {@code storeName}
     * @param storeName Nome da loja pelo qual se vai pesquisar na lista
     * @return Lista de produtos encontrados. Se nao forem encontrados produtos, esta lista fica vazia
     * @throws RemoteException Caso nao seja possivel contactar o registo RMI
     */
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

    /**
     * Converte a lista de produtos para uma lista de strings
     * @return Lista de produtos na forma de strings
     * @throws RemoteException Caso nao seja possivel contactar o registo RMI
     */
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

    /**
     * Tenta encontrar um produto que tenha o nome introduzido e que seja vendido na loja introduzida
     * @param productName Nome do produto que se quer encontrar
     * @param store Loja em que o produto que se quer encontrar e vendido
     * @return {@code ComunicationCode.LOGIN_SUCCESS} se o produto for encontrado ou
     * {@code ComunicationCode.LOGIN_FAILED} caso contrario
     * @throws RemoteException Caso nao seja possivel contactar o registo RMI
     */
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

    /**
     * Tenta adicionar um novo produto a lista de produtos. Esta operacao falha caso ja exista um produto com esse nome a ser vendido na mesma loja
     * @param name Nome do novo produto
     * @param price Preco do novo produto
     * @param store Loja em que o novo produto e vendido
     * @param username Username do utilizador que adicionou o produto
     * @param date Data em que o produto foi introduzido
     * @return {@code ComunicationCode.OPERATION_SUCCESS} se o produto for adicionado com sucesso ou
     * {@code ComunicationCode.OPERATION_FAILED} caso contrario
     * @throws RemoteException Caso nao seja possivel contactar o registo RMI
     */
    @Override
    public ComunicationCode addProduct(String name, Float price, String store, String username, String date) throws RemoteException {

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

    /**
     * Tenta atualizar o preco do produto introduzido. Esta operacao falha caso nao exista nenhum produto com o nome introduzido
     * a ser vendido na loja introduzida
     * @param name Nome do produto
     * @param newPrice Novo preco do produto
     * @param store Loja em que o produto e vendido
     * @param username Username do utilizador que esta a atualizar o produto
     * @param date Data em que o produto foi atualizado
     * @return {@code ComunicationCode.OPERATION_SUCCESS} se o produto for encontrado ou
     * {@code ComunicationCode.OPERATION_FAILED} caso contrario
     * @throws RemoteException Caso nao seja possivel contactar o registo RMI
     */
    @Override
    public ComunicationCode updateProduct(String name, Float newPrice, String store, String username, String date) throws RemoteException{


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
                        user.clientInterface.notifyClient("O preco do produto " + name + " foi alterado para " + newPrice + "€ pelo user " + username);
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
