package com.example.teddy.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ArrayList<String> listContents = new ArrayList<>();
    ArrayAdapter<String> adapter;
    File savedMessages;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.view_line, R.id.firstLine, listContents);
        lv = (ListView) findViewById(R.id.notes);

        lv.setAdapter(adapter);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listContents.remove(position);
                adapter.notifyDataSetChanged();
            }
        };

        lv.setOnItemClickListener(onItemClickListener);


        savedMessages = new File(getFilesDir(), getString(R.string.fileName));
        FileInputStream fileInputStream = null;
        if (savedMessages.exists()) {
            try {
                fileInputStream = new FileInputStream(savedMessages);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                int i = 0;
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    listContents.add(str);
                    i++;
                    adapter.notifyDataSetChanged();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        FileOutputStream fileOutputStream = null;
        try {
            savedMessages = new File(getFilesDir(), getString(R.string.fileName));
            if (!savedMessages.exists()) {
                savedMessages.createNewFile();
            }
            fileOutputStream = new FileOutputStream(savedMessages);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String item;
            int i = 0;
            while (i < adapter.getCount()) {
                item = adapter.getItem(i);
                bufferedWriter.write(item + '\n');
                i++;
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    

    public void sendMessage(View view) {
        EditText editTxt = (EditText) findViewById(R.id.edit_message);
        listContents.add(editTxt.getText().toString());
        adapter.notifyDataSetChanged();
        editTxt.setText("");
    }


}
