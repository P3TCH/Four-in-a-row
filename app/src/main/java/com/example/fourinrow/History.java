package com.example.fourinrow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class History extends AppCompatActivity {

    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final ImageView imgView = findViewById(R.id.imageView2);
        imgView.setImageResource(R.drawable.back);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadData();
        getSupportActionBar().hide();
    }


    public void showList(){
        final ListView listView = (ListView)findViewById(R.id.listView);
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.activity_column,
                new String[]{"name", "score"},
                new int[]{R.id.col_win, R.id.col_score});
        listView.setAdapter(adapter);
    }

    public void loadData(){
        Configuration config = getResources().getConfiguration();
        String filename = "FourARow.txt";
        String inputString;
        //load data from internal storage
        try {
            FileInputStream inputStream = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputString = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputString + "\n");
            }

//            Map<String, Object> map_main = new HashMap<String, Object>();
//            map_main.put("name", getResources().getString(R.string.team));
//            map_main.put("score", getResources().getString(R.string.number_of_moves));
//            list.add(map_main);

            JSONArray infile_array = new JSONArray(stringBuilder.toString());
            for (int i = infile_array.length()-1 ; i >= 0 ; i--) {
                JSONObject obj = infile_array.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", obj.getString("name"));
                map.put("score", obj.getString("score"));
                list.add(map);
            }
            //sort list score low to high
            Collections.sort(list, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    return ((String) o1.get("score")).compareTo((String) o2.get("score"));
                }
            });

            showList();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //public void loadDataExternal(){
//        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
//            try {
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
//                BufferedReader inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//                StringBuffer stringBuffer = new StringBuffer();
//                while ((inputString = inputReader.readLine()) != null) {
//                    stringBuffer.append(inputString + "\n");
//                }
//
//                Map<String, Object> map_main = new HashMap<String, Object>();
//                map_main.put("name", getResources().getString(R.string.team));
//                map_main.put("score", getResources().getString(R.string.number_of_moves));
//                list.add(map_main);
//
//                JSONArray infile_array = new JSONArray(stringBuffer.toString());
//                for (int i = infile_array.length()-1 ; i >= 0 ; i--) {
//                    JSONObject obj = infile_array.getJSONObject(i);
//                    Map<String, Object> map = new HashMap<String, Object>();
//                    map.put("name", obj.getString("name"));
//                    map.put("score", obj.getString("score"));
//                    list.add(map);
//                }
//                showList();
//
//                inputReader.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }//end if
//        }
}