package Analysis;

import Util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class FileOps {
    public int faliure_count;
    AnalysisEngine analysisEngine = new AnalysisEngine();
    public void file_reader_event() throws IOException {
        int faliure_counter=0;
        FileInputStream fstream = new FileInputStream(Constants.BASE_PATH_DIRECTORY+"log/events.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String record;
        br.readLine();
        record = br.readLine();
        while(record != null){
            record= record.toLowerCase(Locale.ROOT);
            if(record.contains("exception")) {
                faliure_counter++;
            }
            record = br.readLine();
        }
        faliure_count= faliure_counter;
    }
    public void file_reader_query() throws Exception{
        ArrayList<String[]> values = new ArrayList<>();
        FileInputStream fstream = new FileInputStream(Constants.BASE_PATH_DIRECTORY+"log/query.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String record;
        int i = 0;
        String[] head = br.readLine().split("\\|");
        record = br.readLine();
        while(record!= null){
            String[] value = record.split("\\|");
            values.add(value);
            record= br.readLine();
        }
        analysisEngine.generateAnalysis(values,head);

    }
    public void file_writer(String line) throws IOException {
        FileWriter fileWriter = new FileWriter(Constants.BASE_PATH_DIRECTORY+"Report.txt",true);
        fileWriter.write(line);
        fileWriter.flush();
        fileWriter.close();
    }
}
