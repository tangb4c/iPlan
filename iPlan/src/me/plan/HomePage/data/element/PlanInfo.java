package me.plan.HomePage.data.element;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import me.plan.HomePage.data.element.SubPlanList;
import me.plan.R;
import me.plan.core.Global;
import me.plan.core.TLog;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

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
    public int getCoverUrl(){
        int imgId = 0;
        int r = Global.random.nextInt(3);
        if(r == 0)
            imgId = R.drawable.cover1;
        else if(r == 1)
            imgId = R.drawable.cover2;
        else
            imgId = R.drawable.cover3;
        return imgId;
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
    public class State{
//        const EnumPlanStateIng = 0; //进行中
//        const EnumPlanStateFinish = 1; //完成
//        const EnumPlanStateFail = 2; //放弃
        public static final int DOING = 0, FINISH = 1, DISCARD = 2;
    }
    /**
     * 返回记录日期
     * @return ex: 2014-03-11
     */
    public String getRecordDateString() {
        final SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(getRecodDate());
    }
}
