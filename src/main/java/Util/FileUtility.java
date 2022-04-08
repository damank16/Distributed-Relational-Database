package Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static Util.Constants.BASE_PATH_DIRECTORY;

public class FileUtility {

    public static final String log = "log";

    private String createFilePath(String... file) {
        return String.join(File.separator, file);
    }

    public void writeDataToFile(String type, String message) {
        String fileName = type + ".txt";
        String filePath = createFilePath(BASE_PATH_DIRECTORY, log , fileName);
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(filePath);
                fileWriter.write(message + "\n");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter fileWriter = new FileWriter(filePath,true);
                fileWriter.write(message + "\n");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
