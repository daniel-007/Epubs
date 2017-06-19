package com.example.kkrzywdzinska.epubs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class WyborPliku extends AppCompatActivity {
    private static final String TAG = "FileChooser";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    static List<File> epubs;
    static List<String> names;
    ArrayAdapter<String> adapter;
    static File selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_chooser_layout);

        // Android 6.0 i późniejszy wymaga żądania uprawnień podczas pierwszego uruchomienia
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                findBooks();
            }
        } else {
            findBooks();
        }
    }
    
    private void findBooks() {
        if ((epubs == null) || (epubs.size() == 0)) {
            File startDir = Environment.getExternalStorageDirectory();
            //File startDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            //File startDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            Log.i(TAG, "startDir = " + startDir.toString());
            epubs = epubList(startDir);
        }

        ListView list = (ListView) findViewById(R.id.fileListView);
        names = fileNames(epubs);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView,
                                    int position, long itemId) {
                selected = epubs.get(position);
                Intent resultIntent = new Intent();

                resultIntent.putExtra("bpath", selected.getAbsolutePath());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        list.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    findBooks();
                } else {

                    Toast.makeText(this, "Potrzeba praw odczytu do znalezienia książek", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private List<String> fileNames(List<File> files){

        List<String> res = new ArrayList<String>();
        for (int i = 0; i < files.size(); i++) {
           res.add(files.get(i).getName().replace(".epub", ""));
        }
        return res;
    }

    private List<File> epubList(File dir) {
        List<File> res = new ArrayList<File>();
        if (dir.isDirectory()) {
            File[] f = dir.listFiles();
            if (f != null) {
                for (int i = 0; i < f.length; i++) {
                    if (f[i].isDirectory()) {
                        res.addAll(epubList(f[i]));
                    } else {
                        String lowerCasedName = f[i].getName().toLowerCase();
                        if (lowerCasedName.endsWith(".epub")) {
                            res.add(f[i]);
                        }
                    }
                }
            }
        }
        return res;
    }

    private void refreshList() throws Exception {
        epubs = epubList(Environment.getExternalStorageDirectory());
        names.clear();
        names.addAll(fileNames(epubs));
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_chooser, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                try {
                    refreshList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}

