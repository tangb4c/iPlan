package me.plan.HomePage.UI;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.soundcloud.android.crop.Crop;
import me.plan.HomePage.business.HomePageBusiness;
import me.plan.HomePage.data.element.FeedItem;
import me.plan.HomePage.data.element.PlanInfo;
import me.plan.comm.busi.OperationCallback;
import me.plan.core.TLog;
import me.plan.core.UI.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by blake on 3/30/15.
 */
public class CameraTakePic {
    private static final int REQUEST_CROP = 1, REQUEST_TAKEPICTURE = 2;

    Activity activity;
    String mTakedPicturePath;
    PlanInfo mPlanInfo;
    Uri mOutputPicture;
    OperationCallback<FeedItem> callback;
    public boolean dealOnActivityResult(int requestCode, int resultCode, Intent intent){
        //拍照返回
        if (requestCode == REQUEST_TAKEPICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                File savedPic = new File(mTakedPicturePath);
                crop(Uri.fromFile(savedPic));
                return true;
            }
        }
        //from android crop
        if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            appendPlan(mOutputPicture);
        }
        return false;
    }
    public void dispatchTakePictureIntent(Activity a, PlanInfo planInfo, OperationCallback<FeedItem> callback) {
        this.activity = a;
        mPlanInfo = planInfo;
        this.callback = callback;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File tmpPhotoFile = null;
            try {
                tmpPhotoFile = createTmpFile("iplan_");
                mTakedPicturePath = tmpPhotoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpPhotoFile));
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKEPICTURE);
            } catch (IOException e) {
                TLog.e(e.toString());
            }
        } else {
            ToastUtil.show(activity, "手机里没有找到可用的拍照app");
        }
    }
    private File createTmpFile(final String prefix) throws IOException {
        SimpleDateFormat sp = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = prefix + sp.format(new Date());
        File storeDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storeDir.exists())
            storeDir.mkdirs();
        File tmp = File.createTempFile(fileName, ".jpg", storeDir);
        return tmp;
    }

    private void crop(Uri photoUri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setData(photoUri);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, REQUEST_CROP);
        try {
            Crop crop = new Crop(photoUri);
            File outputCropFile = createTmpFile("iplan_cropped_");
            mOutputPicture = Uri.fromFile(outputCropFile);
            crop.output(mOutputPicture).asSquare().start(activity);
        } catch (IOException e) {
            TLog.e("crop failed. %s", e.toString());
        }
    }
    protected void appendPlan(Uri picUri) {
        try {
            HomePageBusiness.g().appendNewPlan(mPlanInfo.id, "^_^", picUri.getPath(), callback);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
