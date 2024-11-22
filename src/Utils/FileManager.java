package Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class FileManager {

    private final String fileName;
    private final File file;

    public FileManager(String filePath, String fileName){
        String fullFilePath = filePath + "\\" + fileName;

        this.fileName = fileName;
        file = new File(fullFilePath);
    }


    public File getFile(){
        return file;
    }



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
