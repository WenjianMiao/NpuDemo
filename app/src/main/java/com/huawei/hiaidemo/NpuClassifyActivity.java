package com.huawei.hiaidemo;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.huawei.hiaidemo.bean.ModelInfo;
import com.huawei.hiaidemo.utils.Untils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.huawei.hiaidemo.utils.Constant.GALLERY_REQUEST_CODE;
import static com.huawei.hiaidemo.utils.Constant.IMAGE_CAPTURE_REQUEST_CODE;


public abstract class NpuClassifyActivity extends AppCompatActivity {

    private static final String TAG = NpuClassifyActivity.class.getSimpleName();


    //protected List<ClassifyItemModel> items;
    protected AssetManager mgr;


    protected ModelInfo modelInfo;


    protected Bitmap initClassifiedImg;


    protected float inferenceTime;

    protected float[][] outputData;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        //getSupportActionBar().hide()
        setContentView(R.layout.activity_npu_classify);

        //items = new ArrayList<>();
        mgr = getResources().getAssets();

        initView();

        modelInfo = (ModelInfo)getIntent().getSerializableExtra("modelInfo");

        preProcess();

        Log.i("QG","START loadModelFromFile  "+modelInfo.getModelSaveDir()+modelInfo.getOfflineModel());

        loadModelFromFile(modelInfo.getOfflineModelName(),modelInfo.getModelSaveDir()+modelInfo.getOfflineModel(),modelInfo.isMixModel());

        chooseImageAndClassify();

    }



    private void initView() {
        //
    }


    protected void preProcess(){
        //
    }


    protected abstract void loadModelFromFile(String offlineModelName,String offlineModelPath,boolean isMixModel);

    protected abstract void runModel(ModelInfo modelInfo, float[][] inputData);





    private void chooseImageAndClassify() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && requestCode == GALLERY_REQUEST_CODE) {

            try {
                Bitmap bitmap;
                ContentResolver resolver = getContentResolver();
                Uri originalUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                cursor.moveToFirst();
                Bitmap rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                initClassifiedImg = Bitmap.createScaledBitmap(rgba, modelInfo.getInput_W(), modelInfo.getInput_H(), true);

                float[] inputData = Untils.getPixels(modelInfo.getFramework(), initClassifiedImg, modelInfo.getInput_W(), modelInfo.getInput_H());
                float inputdatas[][] = new float[1][];
                inputdatas[0] = inputData;

                runModel(modelInfo, inputdatas);

            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }

            //Log.i("QQ","START loadModelFromFile  "+modelInfo.getModelSaveDir()+modelInfo.getOfflineModel());

        }
    }

}
