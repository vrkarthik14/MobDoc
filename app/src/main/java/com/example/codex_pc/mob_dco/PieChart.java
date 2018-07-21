package com.example.codex_pc.mob_dco;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class PieChart extends AppCompatActivity {

    private com.github.mikephil.charting.charts.PieChart pieChart;
    private static String Tag = "MainActivity";
    List<PieEntry1> pieEntry1s = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_pie_chart );
        Bundle bundle = getIntent().getBundleExtra("bundle");
        pieEntry1s = bundle.getParcelableArrayList("mlist");
        Log.i("qwasdf",pieEntry1s.get(0).getXdata());

        Log.d( Tag,"started creating chart" );
        pieChart = ( com.github.mikephil.charting.charts.PieChart )findViewById( R.id.piechart );

        List<PieEntry> pieEntries = new ArrayList<>(  );
        for (int i=0;i<4;i++){
            pieEntries.add( new PieEntry( pieEntry1s.get(i).getYdata() ,pieEntry1s.get(i).getXdata()) );
        }
        PieDataSet dataSet = new PieDataSet( pieEntries,"Summary : Total Calories" );
//       dataSet.setColors(  );
        ArrayList<Integer> colors = new ArrayList<>(  );
        colors.add( Color.rgb(253, 0, 18  ) );
        colors.add( Color.rgb( 159, 0, 255 ));
        colors.add( Color.rgb(73, 144, 6) );
        colors.add( Color.rgb( 0, 0, 0) );
        dataSet.setColors(colors );
        dataSet.setSliceSpace( 4 );
        dataSet.setValueTextSize( 15 );
        dataSet.setValueTextColor( Color.WHITE );

        PieData data = new PieData( dataSet );

        pieChart.setData( data);
        pieChart.animateY( 1000 );
        pieChart.setCenterText( "Calorie Breakdown" );
        Legend legend = pieChart.getLegend();
        legend.setForm( Legend.LegendForm.CIRCLE );
        legend.setPosition( Legend.LegendPosition.LEFT_OF_CHART );

    }
}
