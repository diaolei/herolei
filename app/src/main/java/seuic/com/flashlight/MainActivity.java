package seuic.com.flashlight;

import android.app.Activity;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    private CameraManager manager;// 声明CameraManager对象
    private Camera mCamera = null;// 声明Camera对象
    private boolean isContinue;
    private Button mOpenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏*/
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        initUI();



		//添加多一句注解
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        }

        /*isContinue = (boolean) SharePreferanceUtil.getSpUtil().get_sp("isContinue", true);
        lightState = (boolean) SharePreferanceUtil.getSpUtil().get_sp("lightState", false);
        if(!lightState) {
            mOpenBtn.setBackgroundResource(R.color.gray);
            mOffBtn.setBackgroundResource(R.color.blue);
        }else{
            mOpenBtn.setBackgroundResource(R.color.blue);
            mOffBtn.setBackgroundResource(R.color.gray);
        }*/
    }

    private void initUI() {
        mOpenBtn = findViewById(R.id.light_btn);
        mOpenBtn.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                manager.setTorchMode("0", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        SharePreferanceUtil.getSpUtil().put_sp("isContinue",isContinue);
        SharePreferanceUtil.getSpUtil().put_sp("lightState",lightState);
    }*/

    /**
     * 手电筒控制方法
     *
     * @param lightStatus
     * @return
     */
    private void lightSwitch(final boolean lightStatus) {
        if (lightStatus) { // 关闭手电筒
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    manager.setTorchMode("0", false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
            }
        } else { // 打开手电筒
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    manager.setTorchMode("0", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                final PackageManager pm = getPackageManager();
                final FeatureInfo[] features = pm.getSystemAvailableFeatures();
                for (final FeatureInfo f : features) {
                    if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) { // 判断设备是否支持闪光灯
                        if (null == mCamera) {
                            mCamera = Camera.open();
                        }
                        final Camera.Parameters parameters = mCamera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(parameters);
                        mCamera.startPreview();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.light_btn :
                if(isContinue) {
                    lightSwitch(true);
                    mOpenBtn.setBackgroundResource(R.mipmap.black);
                    isContinue = false;
                }else{
                    lightSwitch(false);
                    mOpenBtn.setBackgroundResource(R.mipmap.liang);
                    isContinue = true;
                }

                break;
        }
    }
}
