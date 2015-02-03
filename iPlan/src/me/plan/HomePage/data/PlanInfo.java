package me.plan.HomePage.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import me.plan.HomePage.data.element.SubPlanList;
import me.plan.core.TLog;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tangb4c on 2015/2/1.
 */
@JsonIgnoreProperties("subPlanList")
public class PlanInfo {
    public String id;
    public String ownerId;
    public String title;
    public int finishPercent;
    public int finishDate;
    public int tryTimes;
    public String backGroudPic;
    public int state;

    private String _private;
    public int createTime;
    public ArrayList<SubPlanList> subPlanList;
    public int followNums;

    public String getPrivate() {
        return _private;
    }

    public void setPrivate(final String _private) {
        this._private = _private;
    }

    public PlanInfo(){

    }
    public int getPastDays(){
        Date now = Calendar.getInstance().getTime();
        long days = (now.getTime()/1000 - createTime)/86400;
        return (int)days;
    }
    public int getTotalDays(){
        long days = (finishDate - createTime)/86400;
        return (int)days;
    }
    public Date getRecodDate() {
        Date date = new Date(createTime * 1000L);
        TLog.i("date:%s createTime:%d", date, createTime);
        return date;
    }
}
