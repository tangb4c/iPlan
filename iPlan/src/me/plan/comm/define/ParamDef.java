package me.plan.comm.define;

/**
 * Created by tangb4c on 2015/2/1.
 */
public class ParamDef {
    public static final String VERSION = "version", VERSIONCODE = "2.2.2";
    public static final String UID = "uid", UKEY = "ukey";
    public static final String CMD_NEW_PLAN = "superplan/plan/splan_plan_create.php",
            CMD_GET_PLAN_LIST = "superplan/plan/splan_plan_getlist.php",
            CMD_UPDATE_PLAN = "superplan/plan/splan_plan_update.php",
            CMD_SET_PLAN_STATE = "superplan/plan/splan_plan_set_state.php";
    public static final String USERID = "id";
    public static final String OWNERID_STR = "ownerId",
            PLAN_TITLE_STR = "title",
            FINISH_DATE_INT = "finishDate",
            BACKGROUND_INDEX_INT = "backGroudNum",
            PRIVILEGE_INT = "private",
            SUBTASK_LIST = "subPlanList",
            STATE = "state";

    //登录时，用openid换取uid,ukey
    //request:
    //http://182.254.167.228/superplan/man/splan_get_uid.php?version=2.2.2&loginType=qq&openid=23B65D6923A5F94E220559E833E03791&token=E2D53905B0916E0E42910973E1DEBE36
    //return:
    //{"ret":0,"msg":"ok","data":{"uid":100001可能是电话号码，是否拨号?,"ukey":"ukey550699d80fe386.32449233"}}
    public static final String CMD_GET_UID = "superplan/man/splan_get_uid.php";
    public static final String LOGIN_TYPE = "loginType",
            OPENID = "openid",
            TOKEN = "token";
    public static final String CMD_UPLOAD_PIC = "superplan/pic/splan_pic_upload.php";
    public static final String CMD_APPEND_FEED = "superplan/feeds/splan_feeds_create.php";
    //picurl=pic54c11680890673.07817346&content=hello,content&planId=100003_1421940352
    public static final String PICURL = "picurl",
            CONTENT = "content",
            PLANID = "planId";
    public static final String CMD_GET_PLAN_FEEDLIST = "superplan/feeds/splan_feeds_getlist.php";
    public static final String ID = "id",
            ATTACHINFO = "attachInfo";
    /**
     * const EnumPlanPrivatePublic = 0;
     * const EnumPlanPrivateSelf = 1;
     */
    public static int getPrililegeFlagBy(Boolean isPrivate) {
        return isPrivate ? 1 : 0;
    }
}
