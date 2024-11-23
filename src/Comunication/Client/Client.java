package Comunication.Client;

import Comunication.ClientInterface;
import Comunication.ServerInterface;

import Utils.ComunicationCode;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements ClientInterface {

    protected Client() throws RemoteException {
        super();
    }

    //TODO: Resto dos metodos

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String option = "";


        try {

            // * Estabelece ligacao ao servidor RMI
            ServerInterface serverInterface = (ServerInterface) Naming.lookup("rmi://localhost:1234/products");

            //Cria uma instancia da classe Client (para que esta possa ser passada para a classe Server)
            Client client = new Client();

            System.out.print("Introduza o username: ");
            String username = sc.nextLine();

            System.out.print("Introduza a palavra-passe: ");
            String password = sc.nextLine();

            // ? 1. Faz login
            if(serverInterface.login(username, password, client) == ComunicationCode.LOGIN_FAILURE){
                System.out.println("Username ou palavra-passe incorretos. A terminar...");
                System.exit(-1);
            }

            System.out.println("Login correto. Bem vindo ao programa, " + username);

            while(true){
                System.out.println("+------------- Menu -------------+");
                System.out.println("- P -> Pesquisar por Produto");
                System.out.println("- L -> Pesquisar por Loja");
                System.out.println("- S -> Mostrar lista de produtos");
                System.out.println("- A -> Adicionar Produto");
                System.out.println("- U -> Atualizar Produto");
                System.out.println("- Q -> Sair do programa");
                System.out.print("-> ");
                option = sc.nextLine();

                switch(option.toUpperCase()) {
                    case "P" -> {
                        System.out.println("Pesquisar por um produto");
                        System.out.print("Indique o nome do produto por que quer pesquisar: ");
                        String query = sc.nextLine();
                        ArrayList<String> foundList = serverInterface.searchProductByName(query);
                        if(!foundList.isEmpty()) {
                            for (String p : foundList)
                                System.out.println(p);
                        } else {
                            System.out.println("Nao existe nenhum produto com o nome inserido");
                        }
                    }

                    case "L" -> {
                        System.out.println("Pesquisar por uma loja");
                        System.out.print("Indique o nome da loja por que quer pesquisar: ");
                        String query = sc.nextLine();
                        ArrayList<String> foundList = serverInterface.searchProductbyStore(query);
                        if(!foundList.isEmpty()) {
                            for (String p : foundList)
                                System.out.println(p);
                        } else {
                            System.out.println("Nao existe nenhum produto com a loja inserida");
                        }

                    }
                    case "S" -> {
                        System.out.println("Mostrar lista de produtos");

                        ArrayList<String> productList = serverInterface.getProductList();
                        for(String s : productList)
                            System.out.println(s);
                    }

                    case "A" -> {
                        float priceAsFloat;

                        System.out.println("Adicionar um produto");
                        System.out.print("Indique o nome do produto que pretende adicionar: ");
                        String newProductName = sc.nextLine();

                        System.out.print("Indique o preco do produto que pretende adicionar: ");

                        while(true) {

                            String newProductPrice = sc.nextLine();

                            try{
                                priceAsFloat = Float.parseFloat(newProductPrice);
                                break;

                            } catch (NumberFormatException e) {
                                System.out.print("Por favor, introduza um numero valido: ");
                            }
                        }


                        System.out.print("Indique a loja do produto que pretende adicionar: ");
                        String newProductStore = sc.nextLine();

                        String date = new SimpleDateFormat("yyyy/MM/dd - HH:mm").format(new java.util.Date());


                        ComunicationCode code = serverInterface.addProduct(newProductName, priceAsFloat, newProductStore, username, date);


                         if(code == ComunicationCode.OPERATION_FAILURE)
                             System.out.println("Nao foi possivel adicionar o produto a lista");
                         else
                             System.out.println("Produto adicionado com sucesso");
                    }

                    case "U" -> {

                        float priceAsFloat;

                        System.out.println("Atualizar um produto");

                        System.out.println("Indique o nome do produto que pretende atualizar");
                        String productName = sc.nextLine();

                        System.out.println("Indique a loja em que o produto se encontra");
                        String productStore = sc.nextLine();

                        System.out.println("Indique o novo preco do produto: ");
                        while(true) {

                            String newProductPrice = sc.nextLine();

                            try{
                                priceAsFloat = Float.parseFloat(newProductPrice);
                                break;

                            } catch (NumberFormatException e) {
                                System.out.print("Por favor, introduza um numero valido: ");
                            }
                        }

                        String date = new SimpleDateFormat("yyyy/MM/dd - HH:mm").format(new java.util.Date());

                        ComunicationCode c = serverInterface.updateProduct(productName, priceAsFloat, productStore, username, date);

                        if(c == ComunicationCode.OPERATION_FAILURE)
                            System.out.println("Nao foi possivel encontrar o produto a atualizar na lista");
                        else
                            System.out.println("Produto atualizado com sucesso");

                    }

                    case "Q" -> {
                        System.out.println("A terminar o programa...");
                        System.exit(0);
                    }

                }
            }





        } catch (NotBoundException e) {
            System.err.println("URL de RMI nao esta registado no servidor");
        } catch (MalformedURLException e) {
            System.err.println("URL de RMI mal formado");
        } catch (RemoteException e) {
            System.err.println("Nao foi possivel contactar o registo RMI");
        }

        // ! FECHAR O SCANNER
        sc.close();

    }

    @Override
    public void notifyClient(String notification) throws RemoteException {
        System.out.println("[SERVER]: " + notification);
    }
}
