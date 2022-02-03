package com.huawei.hiaidemo;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.huawei.hiaidemo.bean.ModelInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.huawei.hiaidemo.utils.Constant.GALLERY_REQUEST_CODE;
import static com.huawei.hiaidemo.utils.Constant.IMAGE_CAPTURE_REQUEST_CODE;


public abstract class NpuClassifyActivity extends AppCompatActivity {

    //protected List<ClassifyItemModel> items;
    protected AssetManager mgr;


    protected ModelInfo modelInfo;

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

    }



    private void initView() {
        //
    }


    protected void preProcess(){
        //
    }


    protected abstract void loadModelFromFile(String offlineModelName,String offlineModelPath,boolean isMixModel);

}
