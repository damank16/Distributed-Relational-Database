package DataModelling;


import Logger.Log;
import exceptions.NoSuchDatabaseObject;
import session.Session;

import java.io.*;
import java.util.*;

import static Util.Constants.BASE_PATH_DIRECTORY;
import static replication.SFTP.REMOTE_HOST;

public class ReverseEngineering {
    public static final String metadata = "metadata";
    public static final String database = "database";
    Log log = Log.getLogInstance();

    private String createFilePath(String... file) {
        return String.join(File.separator, file);
    }

    private String getTableName(File file) {
        String fileName = file.getName().replaceFirst("[.][^.]+$", "");
        int metaDataIndex = fileName.lastIndexOf("_");
        return fileName.substring(0, metaDataIndex);
    }

    public String findCardinality(String databaseName, String tableName, String fkName) {
        String databaseDirectory = createFilePath(BASE_PATH_DIRECTORY, database, databaseName);
        File tableData = new File(databaseDirectory + File.separator + tableName + ".txt");
        try (FileReader fr = new FileReader(tableData); BufferedReader br = new BufferedReader(fr)) {
            int count = 0;
            int fkIndex = 0;
            List<String> fkDataList = new ArrayList<>();
            Set<String> fkSet = new HashSet<>();
            for (String line; (line = br.readLine()) != null; ) {
                String[] text = line.split("\\|");
                if(count == 0) {
                    fkIndex = Arrays.asList(text).indexOf(fkName);
                    count++;
                }else {
                    fkDataList.add(text[fkIndex]);
                    fkSet.add(text[fkIndex]);
                }
            }

            if(fkDataList.size() != fkSet.size()) {
                return "Cardinality: Many to One";
            }else {
                return "Cardinality: One to One";
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return "Cardinality: Not found";
    }

    public void readTables(String databaseName) {
        String databasePath = createFilePath(BASE_PATH_DIRECTORY, database, databaseName, metadata);
        File directory = new File(databasePath);
        if (directory.exists()) {
            File[] fileList = directory.listFiles();
            String outputFileName = "ERD.txt";
            File outputFile = new File(BASE_PATH_DIRECTORY + File.separator, outputFileName);
            try {
                FileWriter fileWriter = new FileWriter(outputFile);

                // number of files in db
                for (File file : fileList) {
                    String tableName = getTableName(file);
                    String tableInfo = "Table " + tableName + "\n";
                    try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
                        fileWriter.write(tableInfo);
                        List<String> relationList = new ArrayList();
                        List<String> fkColList = new ArrayList();
                        for (String line; (line = br.readLine()) != null; ) {
                            String[] text = line.split("\\|");
                            String message = text[0] + " " + text[1] + " (" + text[2] + ")";

                            if(line.contains("PK")) {
                                message = message + " (PK)";
                            }
                            if (line.contains("FK")) {
                                message = message + " (FK)";
                                fkColList.add(text[0]);
                                String relation = "Relation - (Column) " + text[0] + " of table " + tableName + " relates to (Column) " + text[6] + " of table: " + text[5];
                                relationList.add(relation);
                            }
                            fileWriter.write(message + "\n");
                        }
                        for (int i = 0; i < relationList.size(); i++) {
                            fileWriter.write(relationList.get(i) + "\n");
                            fileWriter.write(findCardinality(databaseName, tableName, fkColList.get(i)) + "\n");
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
        } else {
            String user = Session.getInstance().getLoggedInDBUser().getUserName();
            log.addEventLog(databaseName, REMOTE_HOST, user, "database doesn't exist");
            throw new NoSuchDatabaseObject("No such database");
        }
    }
}
