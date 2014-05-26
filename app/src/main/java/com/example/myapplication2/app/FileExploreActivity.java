package com.example.myapplication2.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileExploreActivity extends ActionBarActivity {
    ListView listView;
    TextView textView;
    File currentParent;
    File[] currentFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explore);
        listView = (ListView)findViewById(R.id.list);
        textView = (TextView)findViewById(R.id.path);
        File root = new File("/mnt/sdcard");
        if(root.exists())
        {
            currentParent = root;
            currentFiles = root.listFiles();
            inflateListView(currentFiles);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(currentFiles[position].isFile()) return;
                File[] tmp = currentFiles[position].listFiles();
                if(tmp  == null || tmp.length == 0)
                {
                    Toast.makeText(this,"当前路径下面没有文件或不可访问！",Toast.LENGTH_LONG).show();
                }
                else
                {
                    currentParent = currentFiles[position];
                    inflateListView(tmp);
                }
            }
        });

        Button parent = (Button)findViewById(R.id.parent);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    if(currentParent.getCanonicalPath().equals("/mnt/sdcard"))
                    {
                        currentParent = currentParent.getParentFile();
                        currentFiles = currentParent.listFiles();
                        inflateListView(currentFiles);
                    }
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        });
    }

    private void  inflateListView(File[] files)
    {
        List<Map<String,Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < files.length; i++) {
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("fileName",files[i].getName());
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItems,R.layout.abc_list_menu_item_layout,"fileName",R.id.list);
        listView.setAdapter(simpleAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.file_explore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
