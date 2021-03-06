package me.plan.HomePage.UI;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.soundcloud.android.crop.Crop;
import me.plan.HomePage.business.HomePageBusiness;
import me.plan.HomePage.data.BaseRsp;
import me.plan.HomePage.data.element.PlanInfo;
import me.plan.HomePage.data.PlanListRsp;
import me.plan.R;
import me.plan.comm.Utils;
import me.plan.comm.Widget.AutoBgButton;
import me.plan.comm.base.CommonActivity;
import me.plan.comm.busi.OperationCallback;
import me.plan.comm.define.ParamDef;
import me.plan.core.Global;
import me.plan.core.LoginUser;
import me.plan.core.TLog;
import me.plan.core.network.BlkNetWorker;
import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends CommonActivity implements View.OnClickListener {
    private static final int REQUEST_CROP = 1, REQUEST_TAKEPICTURE = 2;
    NavMenuWidget mNavMenu;
    ViewPager mCardListPager;
    CardListPagerAdapter mCardAdapter;
    String mTakedPicturePath;
    CameraTakePic mCameraTake;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.plan_main_list);
        if (savedInstanceState != null) {
            mTakedPicturePath = savedInstanceState.getString("mTakedPicturePath");
        }
//        Debug.waitForDebugger();
        initData();
        initUI();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!LoginUser.g().isHasLogined()) {
            LoginUser.g().setHasLogined(true);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, CommConst.REQUEST_QQ_LOGIN);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!Utils.isEmpty(mTakedPicturePath))
            outState.putString("mTakedPicturePath", mTakedPicturePath);
    }

    private void initData() {
        RequestParams requestParams = new RequestParams();
        requestParams.add(ParamDef.USERID, LoginUser.g().getUserId());
        BlkNetWorker.g().get(ParamDef.CMD_GET_PLAN_LIST, requestParams, new TextHttpResponseHandler() {

            @Override
            public void onFailure(final int statusCode, final Header[] headers, final String responseString, final Throwable throwable) {
                TLog.e("拉取列表失败, statusCode:%d response:%s exception:%s", statusCode, responseString, throwable);
            }

            @Override
            public void onSuccess(final int statusCode, final Header[] headers, final String responseString) {
                try {
//                    Debug.waitForDebugger();
                    PlanListRsp rsp = Global.objectMapper.readValue(responseString, PlanListRsp.class);
                    if (rsp.data == null)
                        mCardAdapter.planInfos.clear();
                    else
                        mCardAdapter.planInfos = rsp.data;

                    mCardAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initUI() {
        //init common
        mLeftTitleBtn.setBackgroundResource(R.drawable.nav_ic_tab_default);
//        mLeftTitleBtn.setBackgroundResource(R.drawable.back_icon);
        mLeftTitleBtn.setOnClickListener(this);
        mRightTitleBtn.setBackgroundResource(R.drawable.nav_ic_add_default);
        mRightTitleBtn.setOnClickListener(this);

        mNavMenu = new NavMenuWidget(this);

        mCardListPager = (ViewPager) findViewById(R.id.cardlist);
        mCardAdapter = new CardListPagerAdapter();
        mCardListPager.setAdapter(mCardAdapter);
        mCardListPager.setOffscreenPageLimit(3);
        mCardListPager.setPageMargin(Utils.dimen2px(R.dimen.main_cardlist_gap));
        TextView camera = (TextView) findViewById(R.id.camera);
        camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.left_btn:
            mNavMenu.showMenu();
            break;
        case R.id.right_btn:
            gotoAddPlan();
            break;
        case R.id.card_item_cover:
            gotoPlanDetail();
            break;
        case R.id.btn_ic_more:
            showIcMoreDialog();
            break;
        case R.id.camera:
            if (mCameraTake == null) {
                mCameraTake = new CameraTakePic();
            }
            mCameraTake.dispatchTakePictureIntent(this, getCurrentPlanInfo(), null);
            break;
        default:
        }
    }

    private void gotoPlanDetail() {
        Intent intent = new Intent(this, PlanDetailActivity.class);
        PlanInfo planInfo = getCurrentPlanInfo();
        intent.putExtra(CommConst.INTENT_PLAN_ID, planInfo.id);
        startActivity(intent);
    }

    private void gotoAddPlan() {
        Intent intent = new Intent(this, NewPlanActivity.class);
        startActivity(intent);
    }

    private void showIcMoreDialog() {
        final Dialog dialog = new Dialog(this, R.style.popupDialog);

        dialog.setContentView(R.layout.card_operation_dialog);
        //full screen
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        dialog.setCanceledOnTouchOutside(true);
        View dialogClose = dialog.findViewById(R.id.card_dialog_close);
        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        View dialogEdit = dialog.findViewById(R.id.card_dialog_edit);
        dialogEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPlanActivity.startPlanForResult(MainActivity.this, true, getCurrentPlanInfo());
                dialog.dismiss();
            }
        });
        View dialogFinish = dialog.findViewById(R.id.card_dialog_finish);
        dialogFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageBusiness.g().changePlanState(getCurrentPlanInfo(), PlanInfo.State.FINISH, baseRspOperationCallback);
                dialog.dismiss();
            }
        });
        View dialogDiscard = dialog.findViewById(R.id.card_dialog_abandon);
        dialogDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomePageBusiness.g().changePlanState(getCurrentPlanInfo(), PlanInfo.State.DISCARD, baseRspOperationCallback);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void photoPicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    /**
     * 拍照
     */
//    private void takePicture() {
///*        SimpleDateFormat dateFormat = new SimpleDateFormat("iplan_yyyy_MM_dd.jpg");
//        String imageFilePath = dcim.getAbsolutePath()+dateFormat.format(Calendar.getInstance());
//        File file = new File(imageFilePath); //创建一个文件
//        Uri imageUri = Uri.fromFile(file);*/
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////这里我们插入一条数据，ContentValues是我们希望这条记录被创建时包含的数据信息
////这些数据的名称已经作为常量在MediaStore.Images.Media中,有的存储在MediaStore.MediaColumn中了
////ContentValues values = new ContentValues();
//        ContentValues values = new ContentValues(3);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
//        String fileName = "iplan_" + dateFormat.format(new Date()) + ".jpg";
//        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
//        values.put(MediaStore.Images.Media.DESCRIPTION, "iplan app image");
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//        mPictureUri = MainActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureUri);
//        TLog.i("extra_output:%s", mPictureUri.toString());
//        //这样就将文件的存储方式和uri指定到了Camera应用中
//        //由于我们需要调用完Camera后，可以返回Camera获取到的图片，
//        //所以，我们使用startActivityForResult来启动Camera
//        startActivityForResult(intent, REQUEST_TAKEPICTURE);
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        TLog.i("requestCode:%d resultCode:%d intent:%s", requestCode, resultCode, Utils.intentToString(intent));

        if (mCameraTake != null)
            mCameraTake.dealOnActivityResult(requestCode, resultCode, intent);
        if(requestCode == CommConst.REQUEST_MODIFY_PLAN && resultCode == RESULT_OK){
            onCreate(null);
        }
    }

    //TODO check exception for all
    protected PlanInfo getCurrentPlanInfo() {
        return mCardAdapter.getItem(mCardListPager.getCurrentItem());
    }

    OperationCallback<BaseRsp> baseRspOperationCallback = new OperationCallback<BaseRsp>() {
        @Override
        public void onSucc(BaseRsp item) {
            onCreate(null);
        }
    };
    class CardListPagerAdapter extends PagerAdapter {
        ArrayList<PlanInfo> planInfos = new ArrayList<PlanInfo>();

        public CardListPagerAdapter() {
//            TLog.tag();
/*            for(int i = 0; i < 5; ++i){
                PlanInfo planInfo = new PlanInfo();
                planInfos.add(planInfo);
            }*/
        }

        @Override
        public int getCount() {
//            TLog.i("planInfos null:%b", planInfos == null);
            return planInfos.size();
        }

        public PlanInfo getItem(int index) throws IndexOutOfBoundsException {
            return planInfos.get(index);
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
            PlanInfo planInfo = planInfos.get(position);
            final View rootView = getLayoutInflater().inflate(R.layout.plan_item, null);


            setImageViewById(rootView, R.id.card_item_cover, planInfo.getCoverUrl(), MainActivity.this);
            setTextViewById(rootView, R.id.card_item_recodtime, new SimpleDateFormat("最后记录:yyyy-MM-dd").format(planInfo.getRecodDate()));
            setTextViewById(rootView, R.id.card_item_title, planInfo.title);
            setTextViewById(rootView, R.id.card_item_brief, String.format("已留下%d个努力瞬间", planInfo.tryTimes));
            setTextViewById(rootView, R.id.card_item_pastdays, planInfo.getPastDays() + "");
            setTextViewById(rootView, R.id.card_item_totaldays, "/" + planInfo.getTotalDays());
            AutoBgButton btnIcMore = (AutoBgButton) rootView.findViewById(R.id.btn_ic_more);
            btnIcMore.setOnClickListener(MainActivity.this);
            ViewPager v = (ViewPager) container;
            v.addView(rootView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager v = (ViewPager) container;
            v.removeView((View) object);
        }
    }
}
