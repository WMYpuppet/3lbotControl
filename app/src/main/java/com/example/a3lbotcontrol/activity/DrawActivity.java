package com.example.a3lbotcontrol.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.a3lbotcontrol.R;
import com.example.a3lbotcontrol.bean.InformationHistory;
import com.example.a3lbotcontrol.gen.InformationHistoryDao;
import com.example.a3lbotcontrol.util.GreenDaoManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrawActivity extends AppCompatActivity {
    @BindView(R.id.chart_line)
    LineChart chart;
    @BindView(R.id.title)
    LinearLayout title;
    @BindView(R.id.et_year)
    EditText etYear;
    @BindView(R.id.et_month)
    EditText etMonth;
    @BindView(R.id.btn_draw)
    Button btnDraw;

    ArrayList<String> listDay ;
    ArrayList<Integer> listTime ;
    ArrayList<Integer> listStepfrequency ;
    ArrayList<Integer> listAngle ;
    int year;
    int month;
    XAxis xAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Description description = new Description();
        description.setText("??????/???");
        chart.setDescription(description);
//        chart.getDescription().setEnabled(true);//?????????????????????
        chart.setTouchEnabled(true);//????????????????????????
        chart.setDragDecelerationFrictionCoef(0.9f);
        chart.setDragEnabled(true);//????????????
        chart.setScaleEnabled(true);//????????????
        chart.setDrawGridBackground(false);//???????????????
        chart.setHighlightPerDragEnabled(true);
        chart.setPinchZoom(true);
        chart.animateX(500);
        xAxis = chart.getXAxis();//??????x???
        xAxis.setTextSize(10f);
        xAxis.setTextColor(ColorTemplate.getHoloBlue());//x?????????????????????
        xAxis.setDrawGridLines(false);//x??????????????????????????????
        xAxis.setDrawAxisLine(true);//??????????????????
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//??????x?????????????????????????????????
        xAxis.setDrawLabels(true);  //??????????????????X??????????????????(??????)
        //xAxis.setLabelCount(100, false);
        xAxis.setGranularity(1f);//??????x????????????
        xAxis.setAxisMinimum(0f);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(listDay));//x?????????
        YAxis leftAxis = chart.getAxisLeft();//y???????????????
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());//y???????????????
        leftAxis.setAxisMaximum(220f);//Y????????????(????????????????????????+5)
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawGridLines(true);//??????????????????
        leftAxis.setGranularityEnabled(true);
        chart.getAxisRight().setEnabled(false);//y????????????????????????

//        setData();
//        chart.invalidate();
    }

    private void setData() {
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();
        ArrayList<Entry> values3 = new ArrayList<>();
        for (int i = 0; i < listDay.size(); i++) {
            values1.add(new Entry(i, listTime.get(i)));
        }
        for (int i = 0; i < listDay.size(); i++) {
            values2.add(new Entry(i, listStepfrequency.get(i)));
        }
        for (int i = 0; i < listDay.size(); i++) {
            values3.add(new Entry(i, listAngle.get(i)));
        }

        LineDataSet set1, set2, set3;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) chart.getData().getDataSetByIndex(2);
            set1.setValues(values1);
            set2.setValues(values2);
            set3.setValues(values3);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values1, "??????(minute)");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());//????????????
            // set1.setCircleColor(ColorTemplate.getHoloBlue());//????????????
            set1.setDrawCircles(false);
            set1.setValueTextColor(ColorTemplate.getHoloBlue());
            set1.setValueTextSize(10);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            set2 = new LineDataSet(values2, "??????(HZ)");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.RED);
            set2.setDrawCircles(false);
            set2.setValueTextColor(Color.RED);
            set2.setValueTextSize(10);
            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            set3 = new LineDataSet(values3, "??????(??)");
            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
            set3.setColor(Color.GRAY);
            set3.setCircleColor(Color.GRAY);
            set3.setDrawCircles(false);
            set3.setValueTextColor(Color.GRAY);
            set3.setValueTextSize(10);
            set3.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            LineData data = new LineData(set1, set2, set3);
            data.setDrawValues(true);//?????????????????????????????????
            //data.setValueTextColor(Color.RED);
            //data.setValueTextSize(9f);
            chart.setData(data);
        }
    }


    public void queryWhereDayIH(int year, int month) {
        List<InformationHistory> informationHistoryList = GreenDaoManager.getInstance().getInformationHistoryDao().queryBuilder().where(InformationHistoryDao.Properties.YearIH.eq(year),
                InformationHistoryDao.Properties.MonthIH.eq(month)).build().list();
        listDay = new ArrayList<>();
        listTime = new ArrayList<>();
        listStepfrequency = new ArrayList<>();
        listAngle = new ArrayList<>();
        if (informationHistoryList != null && !informationHistoryList.isEmpty()) {
            listDay.add("0");
            listTime.add(0);
            listStepfrequency.add(0);
            listAngle.add(0);
            for (InformationHistory ih : informationHistoryList) {
                listDay.add(Integer.toString(ih.getDaylongIH()));
                listTime.add(ih.getTimeIH());
                listStepfrequency.add(ih.getStepfrequencyIH());
                listAngle.add(ih.getAngleIH());
            }
        }

    }


    @OnClick({R.id.btn_draw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_draw:
                year = Integer.parseInt(etYear.getText().toString());
                month = Integer.parseInt(etMonth.getText().toString());
                queryWhereDayIH(year, month);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(listDay));//x?????????

                setData();
                chart.invalidate();
                break;
        }
    }
}
