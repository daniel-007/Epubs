package com.example.kkrzywdzinska.epubs;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected EpubNawigator nawigator;
    protected int czyCzytane;
    protected int panele;
    protected String[] ustawienia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nawigator = new EpubNawigator(1,this);

        panele = 0;
        ustawienia = new String[5];

        // wczytanie stanu
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        loadState(preferences);
        nawigator.loadViews(preferences);
        if (panele == 0) {
            czyCzytane = 0;
            Intent goToChooser = new Intent(this, WyborPliku.class);
            startActivityForResult(goToChooser, 0);
        }
    }

    protected void onResume() {
        super.onResume();
        if (panele == 0) {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            nawigator.loadViews(preferences);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        saveState(editor);
        editor.commit();
    }

    // wczytanie ksiazki
    @Override
    public void onActivityResult(int zadanykod, int wynikowykod, Intent dane) {
        if (panele == 0) {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            nawigator.loadViews(preferences);
        }

        if (wynikowykod == Activity.RESULT_OK) {
            String path = dane.getStringExtra(getString(R.string.bpath));
            Log.d("PATH:", path);
            nawigator.openBook(path, czyCzytane);
        }
    }


    // zapis stanu
    protected void saveState(SharedPreferences.Editor edytor) {
        nawigator.saveState(edytor);
    }

    protected void loadState(SharedPreferences preferences) {
        if (!nawigator.loadState(preferences))
            errorMessage(getString(R.string.error_cannotLoadState));
    }

    public void errorMessage(String message) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setCSS() {
        nawigator.changeCSS(czyCzytane, ustawienia);
    }

    public void setBackColor(String my_backColor) {
        ustawienia[1] = my_backColor;
    }

    public void setColor(String my_color) {
        ustawienia[0] = my_color;
    }


    public void setFontSize(String my_fontSize) {
        ustawienia[4] = my_fontSize;
    }

    public void setMarginLeft(String mLeft) {
        ustawienia[2] = mLeft;
    }

    public void setMarginRight(String mRight) {
        ustawienia[3] = mRight;
    }

    public void addPanel(Panel p) {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.MainLayout, p, p.getTag());
        fragmentTransaction.commit();

        panele++;
    }

    public void attachPanel(Panel p) {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        fragmentTransaction.attach(p);
        fragmentTransaction.commit();

        panele++;
    }

    public void detachPanel(Panel p) {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        fragmentTransaction.detach(p);
        fragmentTransaction.commit();

        panele--;
    }

    public void removePanelWithoutClosing(Panel p) {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        fragmentTransaction.remove(p);
        fragmentTransaction.commit();

        panele--;
    }

    public void removePanel(Panel p) {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        fragmentTransaction.remove(p);
        fragmentTransaction.commit();

        panele--;
        if (panele <= 0)
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Open:
                czyCzytane = 0;
                Intent goToChooser1 = new Intent(this, WyborPliku.class);
                goToChooser1.putExtra(getString(R.string.second),
                        getString(R.string.time));
                startActivityForResult(goToChooser1, 0);
                return true;
            case R.id.Metadata:
                if (nawigator.exactlyOneBookOpen() == true) {
                    nawigator.displayMetadata(0);
                }
                return true;

            case R.id.tableOfContents:
                if (nawigator.exactlyOneBookOpen() == true)
                    nawigator.displayTOC(0);
                return true;


            case R.id.Style:
                try {
                    if (nawigator.exactlyOneBookOpen() == true) {
                        DialogFragment newFragment = new ZmienCSSMenu();
                        newFragment.show(getFragmentManager(), "");
                        czyCzytane = 0;
                    }
                } catch (Exception e) {
                    errorMessage(getString(R.string.error_CannotChangeStyle));
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

