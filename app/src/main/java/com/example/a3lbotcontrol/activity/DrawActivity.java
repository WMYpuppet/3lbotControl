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
        description.setText("单位/天");
        chart.setDescription(description);
//        chart.getDescription().setEnabled(true);//不设置描述文本
        chart.setTouchEnabled(true);//设置支持触控手势
        chart.setDragDecelerationFrictionCoef(0.9f);
        chart.setDragEnabled(true);//设置缩放
        chart.setScaleEnabled(true);//设置推动
        chart.setDrawGridBackground(false);//不绘制网格
        chart.setHighlightPerDragEnabled(true);
        chart.setPinchZoom(true);
        chart.animateX(500);
        xAxis = chart.getXAxis();//得到x轴
        xAxis.setTextSize(10f);
        xAxis.setTextColor(ColorTemplate.getHoloBlue());//x轴坐标字体颜色
        xAxis.setDrawGridLines(false);//x轴上每个竖线是否显示
        xAxis.setDrawAxisLine(true);//是否绘制轴线
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴下方位置（默认上方）
        xAxis.setDrawLabels(true);  //设置是否绘制X轴上的对应值(标签)
        //xAxis.setLabelCount(100, false);
        xAxis.setGranularity(1f);//设置x轴的间距
        xAxis.setAxisMinimum(0f);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(listDay));//x轴赋值
        YAxis leftAxis = chart.getAxisLeft();//y轴左边数据
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());//y轴字体颜色
        leftAxis.setAxisMaximum(220f);//Y轴最大值(遍历表格取最大值+5)
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawGridLines(true);//不显示网格线
        leftAxis.setGranularityEnabled(true);
        chart.getAxisRight().setEnabled(false);//y轴右边数据不显示

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
            set1 = new LineDataSet(values1, "时间(minute)");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());//线条颜色
            // set1.setCircleColor(ColorTemplate.getHoloBlue());//圆点颜色
            set1.setDrawCircles(false);
            set1.setValueTextColor(ColorTemplate.getHoloBlue());
            set1.setValueTextSize(10);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            set2 = new LineDataSet(values2, "步频(HZ)");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.RED);
            set2.setDrawCircles(false);
            set2.setValueTextColor(Color.RED);
            set2.setValueTextSize(10);
            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            set3 = new LineDataSet(values3, "角度(°)");
            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
            set3.setColor(Color.GRAY);
            set3.setCircleColor(Color.GRAY);
            set3.setDrawCircles(false);
            set3.setValueTextColor(Color.GRAY);
            set3.setValueTextSize(10);
            set3.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            LineData data = new LineData(set1, set2, set3);
            data.setDrawValues(true);//不在折线上绘制具体数值
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
                xAxis.setValueFormatter(new IndexAxisValueFormatter(listDay));//x轴赋值

                setData();
                chart.invalidate();
                break;
        }
    }
}
