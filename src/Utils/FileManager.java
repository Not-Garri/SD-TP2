/* ------------------------------------------------ *
 *  Trabalho realizado por Rodrigo Garraio, n.23599 *
 *    3. Ano, 1. Semestre, Sistemas Distribuidos    *
 * ------------------------------------------------ */

package Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * <h2>FileManager</h2>
 * Objeto que gere o ficheiro com que e criado. Isso inclui operacoes
 * de escrita no ficheiro e de leitura do ficheiro
 */
public class FileManager {

    private final String fileName;
    private final File file;

    /**
     * Cria um novo objeto FileManager
     * @param filePath Caminho ate ao ficheiro
     * @param fileName Nome do ficheiro
     */
    public FileManager(String filePath, String fileName){
        //Cria o caminho completo ao ficheiro, concatenando o nome do ficheiro ao final do caminho
        String fullFilePath = filePath + "\\" + fileName;

        this.fileName = fileName;

        //Cria um novo objeto File com base no caminho completo para o ficheiro
        file = new File(fullFilePath);
    }

    /**
     * Retorna o ficheiro que o FileManager gere
     * @return Ficheiro gerido pelo FileManager
     */
    public File getFile(){
        return file;
    }

    /**
     * Escreve a linha passada como argumento no ficheiro, substituindo os conteudos atuais do ficheiro
     * @param string Linha a ser escrita no ficheiro
     */
    public void writeToFile(String string){
        try
        (
            //Apaga tudo o que estiver no ficheiro quando for escrever
            FileWriter fileWriter = new FileWriter(file)
        ) {

            fileWriter.write(string);

        } catch (IOException e) {
            System.err.println("Erro ao abrir o ficheiro");
        }
    }

    /**
     * Coloca a linha passada no final do ficheiro sem substituir os conteudos atuais do ficheiro
     * @param string Linha a ser colocada no ficheiro
     */
    public void appendToFile(String string) {

        try
        (
            FileWriter fileWriter = new FileWriter(file, true)
        ) {

            fileWriter.write("\n" + string);

        } catch (IOException e) {
            System.err.println("Erro ao abrir o ficheiro");
        }
    }

    /**
     * Devolve uma lista de todas as linhas do ficheiro
     * @return Lista de linhas do ficheito
     */
    public String[] getListFromFile() {

        ArrayList<String> linesList = new ArrayList<>();

        try
        (
            Scanner fileReader = new Scanner(file);
        ) {

            while(fileReader.hasNextLine())
                linesList.add(fileReader.nextLine());

            return Arrays.copyOf(linesList.toArray(), linesList.size(), String[].class);


        } catch (FileNotFoundException e) {
            System.err.println("Erro ao abrir o ficheiro \" " + fileName + "\" para leitura");
            return null;
        }
    }
}
