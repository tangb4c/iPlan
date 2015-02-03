package me.plan.comm.define;

/**
 * Created by tangb4c on 2015/2/1.
 */
public class ParamDef {
    public static final String CMD_NEW_PLAN = "superplan/plan/splan_plan_create.php",
        CMD_GET_PLAN_LIST = "superplan/plan/splan_plan_getlist.php";
    public static final String USERID = "id";
    public static final String OWNERID_STR = "ownerId",
        PLAN_TITLE_STR = "title",
        FINISH_DATE_INT = "finishDate",
        BACKGROUND_INDEX_INT = "backGroudNum",
        PRIVILEGE_INT = "private",
        SUBTASK_LIST = "subPlanList";



    /**
     const EnumPlanPrivatePublic = 0;
     const EnumPlanPrivateSelf = 1;
     */
        public static int getPrililegeFlagBy(Boolean isPrivate){
            return isPrivate? 1: 0;
        }
}
