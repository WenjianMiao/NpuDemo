//package com.example.npudemo;
package com.huawei.hiaidemo;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.npudemo.utils.ModelManager;
//import com.example.npudemo.bean.ModelInfo;
import com.huawei.hiaidemo.utils.ModelManager;
import com.huawei.hiaidemo.bean.ModelInfo;
import com.huawei.hiaidemo.utils.Untils;

import java.io.File;

import android.os.SystemClock;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected ModelInfo demoModelInfo = new ModelInfo();
    protected boolean useNPU  = false;
    protected boolean interfaceCompatible = true;

    protected Button btnsync = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initModels();
        copyModels();
        modelCompatibilityProcess();

    }


    private void initView() {
        //rv = (RecyclerView) findViewById(R.id.rv);
        //manager = new LinearLayoutManager(this);
        //rv.setLayoutManager(manager);
        btnsync = (Button) findViewById(R.id.btn_sync);
        btnsync.setOnClickListener(this);
    }

    private void modelCompatibilityProcess(){
        //load libhiaijni.so
        boolean isSoLoadSuccess = ModelManager.loadJNISo();
        if (isSoLoadSuccess) {
            Toast.makeText(this, "load libhiai.so success.",Toast.LENGTH_SHORT).show();
            interfaceCompatible = true;
            useNPU =
                    ModelManager.modelCompatibilityProcessFromFile(demoModelInfo.getModelSaveDir() +
                            demoModelInfo.getOnlineModel(),demoModelInfo.getModelSaveDir() +
                            demoModelInfo.getOnlineModelPara(),demoModelInfo.getFramework(),demoModelInfo.getModelSaveDir() + demoModelInfo.getOfflineModel(),demoModelInfo.isMixModel());
        }
        else {
            interfaceCompatible = false;
            Toast.makeText(this, "load libhiai.so fail.", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_sync){
            if(interfaceCompatible) {
                if(useNPU){
                    Intent intent = new Intent(MainActivity.this, SyncClassifyActivity.class );
                    intent.putExtra("modelInfo", demoModelInfo);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Model incompatibility or NO online Compiler interface or Compile model failed, Please run it on CPU", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Interface incompatibility, Please run it on CPU", Toast.LENGTH_SHORT).show();
            }
        }
        // no code for async currently

    }



    protected void initModels(){
        File dir =  getDir("models", Context.MODE_PRIVATE);
        String path = dir.getAbsolutePath() + File.separator ;
        Log.d("mytag",path);
        demoModelInfo.setModelSaveDir(path);
        demoModelInfo.setFramework("tensorflow");
        //       The DDK version which is used to generate the offlinemodel. when the version is less than "100.150.031.xxx",This parameter is mandatory.
        demoModelInfo.setOfflineModelVersion("100.150.032.001");
        demoModelInfo.setOfflineModel("InceptionV3.cambricon");
        demoModelInfo.setOfflineModelName("hiai");
        demoModelInfo.setOnlineModel("SegModel.zip");
        demoModelInfo.setOnlineModelPara("");
        demoModelInfo.setOnlineModelLabel("labels_inceptionv3.txt");
        demoModelInfo.setMixModel(true);

        demoModelInfo.setInput_Number(1);
        demoModelInfo.setInput_N(1);
        demoModelInfo.setInput_C(3);
        demoModelInfo.setInput_H(299);
        demoModelInfo.setInput_W(299);
        demoModelInfo.setOutput_Number(1);
        demoModelInfo.setOutput_N(1);
        demoModelInfo.setOutput_C(1001);
        demoModelInfo.setOutput_H(1);
        demoModelInfo.setOutput_W(1);

//        demoModelInfo.setFramework("tensorflow");
//        //       The DDK version which is used to generate the offlinemodel. when the version is less than "100.150.031.xxx",This parameter is mandatory.
//        demoModelInfo.setOfflineModelVersion("100.100.001.010");
//        demoModelInfo.setOfflineModel("InceptionV3.cambricon");
//        demoModelInfo.setOfflineModelName("InceptionV3");
//        demoModelInfo.setOnlineModel("inceptionv3.pb");
//        demoModelInfo.setOnlineModelPara("inceptionv3.txt");
//        demoModelInfo.setOnlineModelLabel("labels_tensorflow.txt");
//
//        demoModelInfo.setInput_Number(1);
//        demoModelInfo.setInput_N(1);
//        demoModelInfo.setInput_C(3);
//        demoModelInfo.setInput_H(299);
//        demoModelInfo.setInput_W(299);
//        demoModelInfo.setOutput_Number(1);
//        demoModelInfo.setOutput_N(1);
//        demoModelInfo.setOutput_C(1001);
//        demoModelInfo.setOutput_H(1);
//        demoModelInfo.setOutput_W(1);

    }

    private void copyModels(){
        AssetManager am = getAssets();
        if(!Untils.isExistModelsInAppModels(demoModelInfo.getOnlineModel(),demoModelInfo.getModelSaveDir())){
            Untils.copyModelsFromAssetToAppModels(am, demoModelInfo.getOnlineModel(),demoModelInfo.getModelSaveDir());
        }
        if(!Untils.isExistModelsInAppModels(demoModelInfo.getOnlineModelPara(),demoModelInfo.getModelSaveDir())){
            Untils.copyModelsFromAssetToAppModels(am, demoModelInfo.getOnlineModelPara(),demoModelInfo.getModelSaveDir());
        }
        if(!Untils.isExistModelsInAppModels(demoModelInfo.getOfflineModel(),demoModelInfo.getModelSaveDir())){
            Untils.copyModelsFromAssetToAppModels(am, demoModelInfo.getOfflineModel(),demoModelInfo.getModelSaveDir());
        }
    }

}