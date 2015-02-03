package me.plan.HomePage.business;

import android.os.Debug;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import me.plan.HomePage.data.PlanListRsp;
import me.plan.comm.define.ParamDef;
import me.plan.core.LoginUser;
import me.plan.core.TLog;
import me.plan.core.network.BlkNetWorker;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by tangb4c on 2015/2/3.
 */
public class HomePageBusiness {
    ObjectMapper objectMapper = new ObjectMapper();
    private static HomePageBusiness g = new HomePageBusiness();
    public static HomePageBusiness g(){
        return g;
    }
    public void getPlanList(){

    }
}
