package ca.sandytran.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //AsyncTask < DOWNLOAD LOCATION, PROGRESS BAR, RESULT >
    private class DownloadData extends AsyncTask<String, Void, String> {
        private String mFileContents; //stores data we get from XML file

        //String... variable number of arguments
        //do in background - run without blocking
        @Override
        protected String doInBackground(String... params) {
            mFileContents = downloadXMLFile(params[0]);
            if(mFileContents == null){
                Log.d("DownloadData", "Error downloading");
            }
            return mFileContents;
        }

        private String downloadXMFile(String urlPath) {
            //temporary buffer used to store XML file
            StringBuilder tempBuffer = new StringBuilder();

            //TRY CATCH block in case there is an error while trying to download e.g. you go
            // offline while downloading, apple's site is down

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection)  url.openConnection();
                int response = connection.getResponseCode();
                Log.d("DownloadData", "The response code was " + response);
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                char[] inputBuffer = new char[500]; //read file 500 chars at a time

                while(true){
                    charRead = isr.read(inputBuffer);
                    if(charRead <= 0){
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }

                return tempBuffer.toString();

            } catch(IOException e){
                Log.d("DownloadData", "IO Exception reading data: " + e.getMessage());
            }

        }
    }
}
