package com.huawei.hiaidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huawei.hiaidemo.bean.ModelInfo;
import com.huawei.hiaidemo.utils.ModelManager;

import static com.huawei.hiaidemo.utils.Constant.AI_OK;


public class SyncClassifyActivity extends NpuClassifyActivity {

    private static final String TAG = SyncClassifyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_classify);

    }


    @Override
    protected void loadModelFromFile(String offlineModelName, String offlineModelPath, boolean isMixModel) {
        int ret = ModelManager.loadModelFromFileSync(offlineModelName,offlineModelPath,isMixModel);
        if (AI_OK == ret) {
            Toast.makeText(SyncClassifyActivity.this,
                    "load model success.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SyncClassifyActivity.this,
                    "load model fail.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void runModel(ModelInfo modelInfo, float[][] inputData) {
        long start = System.currentTimeMillis();
        outputData = ModelManager.runModelSync(modelInfo, inputData);
        long end = System.currentTimeMillis();
        inferenceTime = end - start;
        if(outputData == null){
            Log.e(TAG,"runModelSync fail ,outputData is null");
            return;
        }
        Log.i(TAG, "runModel outputdata length : " + outputData.length + "/inferenceTime = "+inferenceTime);
    }
}



/*
public class SyncClassifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_classify);
    }
}
*/
