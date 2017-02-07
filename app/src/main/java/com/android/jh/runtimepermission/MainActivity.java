package com.android.jh.runtimepermission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //버전 체크해서 마시멜로우(6.0)보다 낮으면 런타임 권한 체크를 하지않는다.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermssion();
        } else {
            loadData();
        }
    }
    private final int REQ_CODE = 100;
    //권한 체크

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermssion() {
        // 1.1 런타임 권한체크
        if( checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                )
                {
            // 1.2 요청할 권한 목록 작성
            String permArr[] = {Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.CALL_PHONE
                    , Manifest.permission.READ_CONTACTS
            };
            // 1.3 시스템에 권한요청
            requestPermissions(permArr, REQ_CODE);
        } else{
            loadData();
        }
    }



    //권한체크 후 콜백< 사용자가 확인후 시스템이 호출하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_CODE){
            //배열에 넘긴 런타임 권한을 체크해서 승인이 됐으면
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                loadData();
            } else {
                Toast.makeText(this,"권한을 사용하지 않으시면 프로그램을 실행시킬수 없습니다",Toast.LENGTH_SHORT).show();
                // 선택 1.종료, 2. 권한체크 다시물어보기
                checkPermssion();
            }
        }
    }

    //데이터 읽어오기(시스템 실행하기)
    private void loadData() {
        Toast.makeText(this,"프로그램을 실행합니다",Toast.LENGTH_SHORT).show();
        //데이터를 불러와 초기화
        DataLoader loader = new DataLoader(this);
        loader.load();
        ArrayList<Contact> datas = loader.get();
        //RecycleView 세팅
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler);
        RecyclerAdapter adapter = new RecyclerAdapter(datas,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
