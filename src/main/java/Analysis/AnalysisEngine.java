package Analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisEngine {
    static int insert;
    static int update;
    static int create;
    static int delete;
    public static List<Map<String, String>> consolidated_queries = new ArrayList<>();
    public static int total_query_count;

    public List<Map<String, String>> generateAnalysis(ArrayList<String[]> values,String[] head) {

        for (int i = 0; i < values.size() ; i++) {
            Map<String, String> row = new HashMap();
            String[] single_value = {};
            single_value = values.get(i);
            for (int j = 0; j < head.length; j++) {
                row.put(head[j], single_value[j]);
            }
            total_query_count++;
            consolidated_queries.add(row);
        }
        return consolidated_queries;
    }
    public Map<String,Integer> generate_insert_count(){
        Map<String,Integer> insert_count = new HashMap();
        for(int i = 0; i< consolidated_queries.size();i++) {
            Map<String, String> query = new HashMap();
            query = consolidated_queries.get(i);
            if (query.get("query_type").equals(("insert"))) {
                if(!insert_count.containsKey(query.get("database_name") + " " + query.get("table_name"))) {
                    insert_count.put(query.get("database_name") + " " + query.get("table_name"), 1);
                } else {
                    insert_count.put(query.get("database_name") + " " + query.get("table_name"), insert_count.get(query.get("database_name") + " " + query.get("table_name")) + 1);
                }
            }

        }
        return insert_count;

    }
    public Map<String,Integer> generate_update_count(){
        Map<String,Integer> update_count = new HashMap();
        for(int i = 0; i< consolidated_queries.size();i++){
            Map<String,String> query = new HashMap();
            query= consolidated_queries.get(i);
            if(query.get("query_type").equals(("update"))) {
                if (!update_count.containsKey(query.get("database_name")+" "+query.get("table_name"))){
                    update_count.put(query.get("database_name")+" "+query.get("table_name"), 1);
                } else {
                    update_count.put(query.get("database_name")+" "+query.get("table_name"), update_count.get(query.get("database_name")+" "+query.get("table_name"))+1);
                }
            }

        }

        return update_count;

    }
    public Map<String,Integer> generate_delete_count(){
        Map<String,Integer> delete_count = new HashMap();
        for(int i = 0; i< consolidated_queries.size();i++){
            Map<String,String> query = new HashMap();
            query= consolidated_queries.get(i);
            if(query.get("query_type").equals(("delete"))) {
                if (!delete_count.containsKey(query.get("database_name") + " " + query.get("table_name"))) {
                    delete_count.put(query.get("database_name") + " " + query.get("table_name"), 1);
                } else {
                    delete_count.put(query.get("database_name") + " " + query.get("table_name"), delete_count.get(query.get("database_name") + " " + query.get("table_name")) + 1);
                }
            }
        }
        return delete_count;

    }
    public Map<String,Integer> generate_select_count(){
        Map<String,Integer> select_count = new HashMap();
        for(int i = 0; i< consolidated_queries.size();i++){
            Map<String,String> query = new HashMap();
            query= consolidated_queries.get(i);
            if(query.get("query_type").equals(("select"))) {
                if (!select_count.containsKey(query.get("database_name") + " " + query.get("table_name"))) {
                    select_count.put(query.get("database_name") + " " + query.get("table_name"), 1);
                } else {
                    select_count.put(query.get("database_name") + " " + query.get("table_name"), select_count.get(query.get("database_name") + " " + query.get("table_name")) + 1);
                }
            }
        }
        return select_count;

    }
    public Map<String,Integer> count_queries_by_vm()
    {
        Map<String, Integer> count_queries = new HashMap();
        for (int i = 0; i < consolidated_queries.size(); i++) {
            Map<String, String> query = new HashMap();
            query = consolidated_queries.get(i);
            if (!count_queries.containsKey(query.get("user_name") + " " + query.get("database_name"))) { //&& count_queries.containsKey(query.get("vm"))
                count_queries.put(query.get("user_name") + " " + query.get("database_name"),1);
            }
            else{
                count_queries.replace(query.get("user_name") + " " + query.get("database_name"),count_queries.get(query.get("user_name") + " " + query.get("database_name")) , count_queries.get(query.get("user_name") + " " + query.get("database_name"))+1);
            }
        }
        return count_queries;
    }
}
