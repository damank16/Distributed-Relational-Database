package Analysis;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PieChartGenerator extends ApplicationFrame {
    public static Map<String,Integer> map = new HashMap<>();
    public static int total_queries;
    public PieChartGenerator( String title, Map<String,Integer> maps, int total ) {
        super( "Query Comparison" );
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
        JFreeChart chart = ChartFactory.createPieChart(
                "Query Comparison",   // chart title
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

    public void show_graph(Map<String,Integer> maps, int total) {
        PieChartGenerator demo = new PieChartGenerator( "Query Comparison", maps, total );
        demo.setSize( 560 , 367 );
        RefineryUtilities.centerFrameOnScreen( demo );
        demo.setVisible( true );
    }
}
