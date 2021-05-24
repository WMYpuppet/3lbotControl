package com.example.a3lbotcontrol.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 作者：Created by Administrator on 2021/3/10.
 * 邮箱：
 */

@Entity
public class InformationHistory {
    @Id(autoincrement = true)
    private Long id;
    private int yearIH;
    private int monthIH;
    private int daylongIH;
    private int timeIH;
    private int StepfrequencyIH;
    private int angleIH;
    @Generated(hash = 172796403)
    public InformationHistory(Long id, int yearIH, int monthIH, int daylongIH,
            int timeIH, int StepfrequencyIH, int angleIH) {
        this.id = id;
        this.yearIH = yearIH;
        this.monthIH = monthIH;
        this.daylongIH = daylongIH;
        this.timeIH = timeIH;
        this.StepfrequencyIH = StepfrequencyIH;
        this.angleIH = angleIH;
    }
    public  InformationHistory(int yearIH, int monthIH, int daylongIH,
                               int timeIH, int StepfrequencyIH, int angleIH){
        this.yearIH = yearIH;
        this.monthIH = monthIH;
        this.daylongIH = daylongIH;
        this.timeIH = timeIH;
        this.StepfrequencyIH = StepfrequencyIH;
        this.angleIH = angleIH;
    }
    @Generated(hash = 1871105338)
    public InformationHistory() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getYearIH() {
        return this.yearIH;
    }
    public void setYearIH(int yearIH) {
        this.yearIH = yearIH;
    }
    public int getMonthIH() {
        return this.monthIH;
    }
    public void setMonthIH(int monthIH) {
        this.monthIH = monthIH;
    }
    public int getDaylongIH() {
        return this.daylongIH;
    }
    public void setDaylongIH(int daylongIH) {
        this.daylongIH = daylongIH;
    }
    public int getTimeIH() {
        return this.timeIH;
    }
    public void setTimeIH(int timeIH) {
        this.timeIH = timeIH;
    }
    public int getStepfrequencyIH() {
        return this.StepfrequencyIH;
    }
    public void setStepfrequencyIH(int StepfrequencyIH) {
        this.StepfrequencyIH = StepfrequencyIH;
    }
    public int getAngleIH() {
        return this.angleIH;
    }
    public void setAngleIH(int angleIH) {
        this.angleIH = angleIH;
    }

}
