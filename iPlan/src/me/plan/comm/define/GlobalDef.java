package me.plan.comm.define;

/**
 * Created by tangb4c on 2015/2/3.
 */
public class GlobalDef {
    public static class ErrorCode{
        public static final int PARSE_JSON_FAILED = -1001;
        public static final int generateNetCode(int statusCode){
            return -8000 - Math.abs(statusCode);
        }
    }

    public class SP {
        public static final String LOGIN = "login";
    }

    public class LOGIN {
        public static final String OPENID = "openid";

        public static final String QQTOKEN = "qqtoken";

        public static final String EXPIREDTIME = "expired_time";
    }
}
