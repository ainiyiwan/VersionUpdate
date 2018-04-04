package zy.com.versionupdatetest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;

public class MainActivity extends AppCompatActivity {

    private DownloadBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void update(View view) {
        builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl("https://www.baidu.com")
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        Toast.makeText(MainActivity.this, "request successful", Toast.LENGTH_SHORT).show();
                        if (TextUtils.isEmpty(result)){
                            Toast.makeText(MainActivity.this, "已是最新版本", Toast.LENGTH_LONG).show();
                            return null;
                        }
                        return crateUIData();
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        Toast.makeText(MainActivity.this, "request failed", Toast.LENGTH_SHORT).show();

                    }
                });
        builder.setForceRedownload(true);//强制重新下载apk（无论本地是否缓存）
        builder.setShowDownloadingDialog(false);//是否显示下载中对话框
        builder.setNotificationBuilder(createCustomNotification());//设置通知栏
        builder.setCustomVersionDialogListener(createCustomDialogTwo());//设置自定义界面
        builder.excuteMission(this);//开始下载
    }

    private NotificationBuilder createCustomNotification() {
        return NotificationBuilder.create()
                .setRingtone(true)
                .setIcon(R.mipmap.ic_launcher)
                .setTicker("custom_ticker")
                .setContentTitle("custom title")
                .setContentText(getString(R.string.custom_content_text));
    }

    private CustomVersionDialogListener createCustomDialogTwo() {
        return new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
                BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_one_layout);
                TextView textView = baseDialog.findViewById(R.id.tv_msg);
                textView.setText(versionBundle.getContent());
                baseDialog.setCanceledOnTouchOutside(true);
                return baseDialog;
            }
        };

    }

    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     */
    private UIData crateUIData() {
        UIData uiData = UIData.create();
        uiData.setTitle(getString(R.string.update_title));
        uiData.setDownloadUrl("http://test-1251233192.coscd.myqcloud.com/1_1.apk");
//        uiData.setDownloadUrl("http://imtt.dd.qq.com/16891/2745D9FA71A69838FCAA1CE2459A9641.apk?fsname=com.tencent.mtt_8.3.0.4020_8304020.apk&csr=1bbd");
        uiData.setContent(getString(R.string.updatecontent));
        return uiData;
    }
}
