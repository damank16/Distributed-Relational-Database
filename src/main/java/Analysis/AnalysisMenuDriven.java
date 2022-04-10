package Analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AnalysisMenuDriven {
    //DML
    String count_update = "count update";
    String count_insert = "count insert";
    String count_delete= "count delete";
    String count_select = "count select";

    //Crash Analytics
    String count_crashes = "count system crashes";

    //DDL updates
    String count_structral_updates = "count structural updates";
    //PieComponent pieComponent = new PieComponent();
    AnalysisEngine analysisEngine = new AnalysisEngine();
    FileOps fileOps = new FileOps();
    Map<String,Integer> insert_map = new HashMap<>();
    Map<String,Integer> update_map = new HashMap<>();
    Map<String,Integer> delete_map = new HashMap<>();
    Map<String,Integer> select_map = new HashMap<>();
    Map<String,Integer> count_queries = new HashMap<>();
    public void analytics_input() throws Exception
    {
        fileOps.file_reader_query();
        Scanner scanner = new Scanner(System.in);
        int choice;
        String database_name;
        List<Map<String,String>> consolidated_queries = analysisEngine.consolidated_queries;
        do {
            System.out.println("Enter the number for queries:");
            System.out.println("1. Count Updates");
            System.out.println("2. Count Inserts");
            System.out.println("3. Count Deletes");
            System.out.println("4. Count Selects");
            System.out.println("5. Count Queries");
            System.out.println("6. Count System Failures");
            System.out.println("7. Exit\n");
            choice = scanner.nextInt();
            try {
                switch (choice) {
                    case 1:
                        System.out.println("Input Database");
                        database_name = scanner.next();
                        update_map = analysisEngine.generate_update_count();
                        fileOps.file_writer("\n" + "Count Update\n" + "__________________________________________________________\n");
                        for (Map.Entry<String, Integer> entry : update_map.entrySet()) {
                            String dbKey = entry.getKey();
                            String dbKeyArray[] = dbKey.split(" ");
                            if (database_name.equalsIgnoreCase(dbKeyArray[0])) {
                                System.out.println("Total " + entry.getValue() + " updates performed on " + dbKeyArray[1]);
                                fileOps.file_writer("Total " + entry.getValue() + " updates performed on " + dbKeyArray[1] + "\n");
                            }
                        }
                        PieChartGenerator pieChartGenerator = new PieChartGenerator("Query Comparison", update_map, analysisEngine.total_query_count, "for update on db_name table_name");
                        pieChartGenerator.show_graph(update_map, analysisEngine.total_query_count, " for update on db_name table_name");
                        break;
                    case 2:
                        System.out.println("Input Database");
                        database_name = scanner.next();
                        insert_map = analysisEngine.generate_insert_count();
                        fileOps.file_writer("\n" + "Count Insert\n" + "__________________________________________________________\n");
                        for (Map.Entry<String, Integer> entry : insert_map.entrySet()) {
                            String dbKey = entry.getKey();
                            String dbKeyArray[] = dbKey.split(" ");
                            if (database_name.equalsIgnoreCase(dbKeyArray[0])) {
                                System.out.println("Total " + entry.getValue() + " inserts performed on " + dbKeyArray[1]);
                                fileOps.file_writer("Total " + entry.getValue() + " inserts performed on " + dbKeyArray[1] + "\n");
                            }
                        }
                        pieChartGenerator = new PieChartGenerator("Query Comparison", insert_map, analysisEngine.total_query_count, "for insert on db_name table_name");
                        pieChartGenerator.show_graph(insert_map, analysisEngine.total_query_count, " for insert on db_name table_name");

                        break;
                    case 3:
                        System.out.println("Input Database");
                        database_name = scanner.next();
                        delete_map = analysisEngine.generate_delete_count();
                        fileOps.file_writer("\n" + "Count Delete\n" + "__________________________________________________________\n");
                        for (Map.Entry<String, Integer> entry : delete_map.entrySet()) {
                            String dbKey = entry.getKey();
                            String dbKeyArray[] = dbKey.split(" ");
                            if (database_name.equalsIgnoreCase(dbKeyArray[0])) {
                                System.out.println("Total " + entry.getValue() + " deletes performed on " + dbKeyArray[1]);
                                fileOps.file_writer("Total " + entry.getValue() + " deletes performed on " + dbKeyArray[1] + "\n");
                            }
                        }
                        pieChartGenerator = new PieChartGenerator("Query Comparison", delete_map, analysisEngine.total_query_count, "for delete on db_name table_name");
                        pieChartGenerator.show_graph(delete_map, analysisEngine.total_query_count, " for delete on db_name table_name");
                        break;
                    case 4:
                        System.out.println("Input Database");
                        database_name = scanner.next();
                        select_map = analysisEngine.generate_select_count();
                        fileOps.file_writer("\n" + "Count Select\n" + "__________________________________________________________\n");
                        for (Map.Entry<String, Integer> entry : select_map.entrySet()) {
                            String dbKey = entry.getKey();
                            String dbKeyArray[] = dbKey.split(" ");
                            if (database_name.equalsIgnoreCase(dbKeyArray[0])) {
                                System.out.println("Total " + entry.getValue() + " selects performed on " + dbKeyArray[1]);
                                fileOps.file_writer("Total " + entry.getValue() + " selects performed on " + dbKeyArray[1] + "\n");
                            }
                        }
                        pieChartGenerator = new PieChartGenerator("Query Comparison", select_map, analysisEngine.total_query_count, "for select on db_name,table_name");
                        pieChartGenerator.show_graph(select_map, analysisEngine.total_query_count, " for select on db_name table_name");
                        break;
                    case 5:
                        count_queries = analysisEngine.count_queries_by_vm();
                        for (Map.Entry<String, Integer> entry : count_queries.entrySet()) {
                            String dbKey = entry.getKey();
                            String dbKeyArray[] = dbKey.split(" ");
                            System.out.println("User " + dbKeyArray[0] + " performed " + entry.getValue() + " queries on " + dbKeyArray[1] + "\n");
                            fileOps.file_writer("User " + dbKeyArray[0] + " performed " + entry.getValue() + " queries on " + dbKeyArray[1] + "\n");
                        }
                        break;
                    case 6:
                        fileOps.file_reader_event();
                        System.out.println("Total number of System Faliures Faced " + fileOps.faliure_count);
                        fileOps.file_writer("\nSystem Failures\n" + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
                        fileOps.file_writer("Total number of System Faliures Faced " + fileOps.faliure_count);
                        break;
                }
            }catch(Exception e){}}while (choice != 7) ;
            System.out.println("Exiting Analytics....\n");
        }

    }
