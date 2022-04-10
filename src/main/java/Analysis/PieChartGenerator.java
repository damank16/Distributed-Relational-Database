package Analysis;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PieChartGenerator extends JFrame {
    public static Map<String,Integer> map = new HashMap<>();
    public static int total_queries;
    public static String title;
    public PieChartGenerator( String titles, Map<String,Integer> maps, int total,String Title ) {
        super( "Query Comparison "+Title );
        title = Title;
        map = maps;
        total_queries= total;
        setContentPane(createDemoPanel( ));
    }

    public static PieDataset createDataset( ) {
        DefaultPieDataset dataset = new DefaultPieDataset( );
        double sum = 0;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            double value = Double.parseDouble(String.valueOf(pair.getValue()));
            value = (value/total_queries) * 100;
            sum = sum+value;
            dataset.setValue((Comparable) pair.getKey(), value);
        }
        dataset.setValue("total",(100-sum));
        return dataset;
    }

    private static JFreeChart createChart( PieDataset dataset ) {
        String str = "Query Comparison"+title;
        JFreeChart chart = ChartFactory.createPieChart(
                "Query Comparison "+title,   // chart title
                dataset,          // data
                true,             // include legend
                true,
                false);

        return chart;
    }

    public static JPanel createDemoPanel( ) {
        JFreeChart chart = createChart(createDataset( ) );
        return new ChartPanel( chart );
    }

    public void show_graph(Map<String,Integer> maps, int total,String title) {
        PieChartGenerator demo = new PieChartGenerator( "Query Comparison ", maps, total,title);
        demo.setSize( 560 , 367 );
        RefineryUtilities.centerFrameOnScreen( demo );
        //demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        demo.setVisible( true );
    }
}
