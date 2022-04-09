package DataModelling;


import Logger.Log;
import exceptions.NoSuchDatabaseObject;
import session.Session;

import java.io.*;

import static Util.Constants.BASE_PATH_DIRECTORY;
import static replication.SFTP.REMOTE_HOST;

public class ReverseEngineering {
    Log log = Log.getLogInstance();
    public static final String metadata = "metadata";
    public static final String database = "database";

    private String createFilePath(String... file) {
        return String.join(File.separator, file);
    }

    public void readTables(String databaseName) {
        String databasePath = createFilePath(BASE_PATH_DIRECTORY, database, databaseName, metadata);
        File directory=new File(databasePath);
        if(directory.exists()) {
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
        }else {
            String user = Session.getInstance().getLoggedInDBUser().getUserName();
//            log.addEventLog(databaseName, "", true, REMOTE_HOST, user);
            throw  new NoSuchDatabaseObject("No such database");
        }
    }
}
