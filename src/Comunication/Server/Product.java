/* ------------------------------------------------ *
 *  Trabalho realizado por Rodrigo Garraio, n.23599 *
 *    3. Ano, 1. Semestre, Sistemas Distribuidos    *
 * ------------------------------------------------ */

package Comunication.Server;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h2>Product</h2>
 * Classe que instancia e realiza operacoes relativas aos produtos
 */
public class Product {

    private final String name;
    private float price;
    private String store;
    private String username;
    private String date;

    /**
     * Retorna o nome do produto
     * @return Nome do produto
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna a loja em que o produto e vendido
     * @return Loja em que o produto e vendido
     */
    public String getStore() {
        return store;
    }

    /**
     * Altera o preco do produto para o preco recebido
     * @param price Novo preco do produto
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Altera o username de quem adicionou/Atualizou o produto
     * @param username Novo username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Altera a data em que o produto foi adicionado/atualizado
     * @param date Nova data do produto
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Cria uma nova instancia de um objeto produto
     * @param name Nome do produto
     * @param price Preco do produto
     * @param store Loja em que o produto e vendido
     * @param username Username do utilizador que criou o produto
     * @param date Data em que o produto foi criado
     */
    public Product(String name, float price, String store, String username, String date) {
        this.store = store;
        this.price = price;
        this.name = name;
        this.username = username;
        this.date = date;
    }

    /**
     * Compara o nome e a loja do produto com os do produto recebido, retornando
     * @param p Produto que vai ser comparado com o produto que invocou o metodo
     * @return {@code true} se os nomes e as lojas forem iguais ou {@code false} caso contrario
     */
    public boolean compare(Product p) {
        return name.equalsIgnoreCase(p.name) && store.equalsIgnoreCase(p.store);
    }

    /**
     * Compara o nome e a loja do produto que invoca o metodo com o nome e a loja
     * passados como argumento
     * @param productName Nome que vai ser comparado ao do produto que invoca o metodo
     * @param productStore Nome da loja que vai ser comparado ao do produto que invoca o metodo
     * @return {@code true} se os nomes e as lojas forem iguais ou {@code false} caso contrario
     */
    public boolean compare(String productName, String productStore) {
        return name.equalsIgnoreCase(productName) && store.equalsIgnoreCase(productStore);
    }

    /**
     * Cria uma string com os dados do produto que invoca o metodo
     * @return Dados do produto formatados numa string
     */
    public String toString(){
        return String.format("Nome: %s - Preco: %.2f - Loja: %s - Adicionado por: %s - Data: %s", name, price, store, username, date);
    }

    /**
     * Cria uma string com os dados do produto que invoca o metodo separados por ponto e virgula para uso em ficheiros CSV
     * @return Dados do produto formatados numa string separados por ponto e virgula
     */
    public String toFileString() {
        return (name + ";" + price + ";" + store + ";" + username+ ";" + date);
    }

    /**
     * Produto que transforma uma lista de strings numa lista de produtos</p>
     * Este metodo assume que as strings contem os dados do produto separados por pontos e virgulas
     * @param list Lista de strings
     * @return Lista de produtos
     */
    public static ArrayList<Product> listFromFile(String[] list) {

        ArrayList<Product> productList = new ArrayList<>();

        for (String s : list) {
            String[] parts = s.split(";");
            productList.add(new Product(parts[0], Float.parseFloat(parts[1]), parts[2], parts[3], parts[4]));
        }

        return productList;
    }
}
