package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.ex.DbException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PathView pathview;
    private TextView tv_start;
    private TextView tv_clear;
    private EditText et_pos;
    private TestView testView;
    private TextView tv_test;
    private ListView lv;

    private String[] pointName;

    String DB_PATH = "data/data/com.example.myapplication/databases/";
    String DB_NAME = "PathDatas.db";
    private List<Routes> rlist = new ArrayList<>();
    private List<Point> plist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        pathview = findViewById(R.id.pathview);
        tv_start = findViewById(R.id.tv_start);
        tv_clear = findViewById(R.id.tv_clear);
        et_pos = findViewById(R.id.et_pos);
        testView = findViewById(R.id.testView);
        tv_test = findViewById(R.id.tv_test);
        lv = findViewById(R.id.lv);
        try {
            rlist = DButils.getDBmanager().findAll(Routes.class);
            plist = DButils.getDBmanager().findAll(Point.class);

            String a = "asd";
        } catch (DbException e) {
            e.printStackTrace();
        }
        List<FloorImg> list = new ArrayList<>();
        FloorImg img1 = new FloorImg();
        img1.setFloor(1);
        img1.setImg(R.mipmap.one_f);
        FloorImg img2 = new FloorImg();
        img2.setFloor(2);
        img2.setImg(R.mipmap.two_f);
        list.add(img1);
        list.add(img2);

        pathview.loadFloorImg(list, rlist, plist);

        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathview.gotoPoint("5号审判法庭");
            }
        });
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathview.tranFloor(Integer.parseInt(et_pos.getText().toString()));
//                testView.tarn();
            }
        });

//        writeDB();
        pointName = new String[plist.size()];
        for (int i = 0; i < plist.size(); i++) {
            pointName[i] = plist.get(i).getPointName();
        }
        lv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, pointName));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pointName = (String) adapterView.getItemAtPosition(i);
                pathview.gotoPoint(pointName);
            }
        });
    }


    //首次登录写入数据库 检查是否执行过数据库写入操作 如果是，执行题库的写入操作
    private void writeDB() {
        //开子线程写入数据库

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (new File(DB_PATH + DB_NAME).exists() == false) {
                    File dir = new File(DB_PATH);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    try {
                        InputStream is = getBaseContext().getAssets().open(DB_NAME);
                        OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);
                        }
                        os.flush();
                        os.close();
                        is.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}

