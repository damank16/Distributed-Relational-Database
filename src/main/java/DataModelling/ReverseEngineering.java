package DataModelling;

import Util.FileUtility;

import java.io.*;

import static Util.Constants.BASE_PATH_DIRECTORY;

public class ReverseEngineering {

    public void readTable() {
        File directory=new File("./a/metadata");
        int fileCount = directory.list().length;
        File[] f = directory.listFiles();
        String outputFileName = "output.txt";
        File outputFile = new File(BASE_PATH_DIRECTORY + File.separator, outputFileName);
        try {
            FileWriter fileWriter = new FileWriter(outputFile);
            for(File file: f) {
                String fileName = file.getName().replaceFirst("[.][^.]+$", "");
                String tableName = "Table " + fileName + "\n";
                try (FileReader fr = new FileReader(file);
                     BufferedReader br = new BufferedReader(fr)) {
                    fileWriter.write(tableName);
                    for(String line; (line = br.readLine()) != null; ) {
                        String[] text = line.split("\\|");
                        String message = "";

//                for (int i = 0; i < text.length; i++) {
                        message = text[0] + " " + text[1] + " (" + text[2] + ")";
                        fileWriter.write(message + "\n");
//                }
                    }
                    fileWriter.write("\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
