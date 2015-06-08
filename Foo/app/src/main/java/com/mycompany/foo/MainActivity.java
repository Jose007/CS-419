package com.mycompany.foo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

//help from http://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
public class MainActivity extends AppCompatActivity {

    public final static String CATEGORIES_MESSAGE = "com.mycompany.foo.CATEGORIES_MESSAGE";
    Context cntxt = this;
    private static final String DEBUG_TAG = "HttpExample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     //   if (id == R.id.action_settings) {
    //        return true;
    //    }
        switch (item.getItemId()){
            case R.id.map:
                Intent mapIntent = new Intent(this, MapsActivity.class);
                startActivity(mapIntent);
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }


    //This function handles the action when a button is clicked
    //It checks for connection and then begins an AsyncTask in the background
    //The AsyncTask runs the code to call the http request
    public void getCategoriesHandler(View view) {
        // Gets the URL from the UI's text field.
        String stringUrl = "http://web.engr.oregonstate.edu/~holmesmo/SeniorProject/WebAPI/categories.php";
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //This is the AsyncTask
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            //getTextResponse.setText("No network connection available.");
        }
    }


    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(cntxt, CategoriesActivity.class);
            intent.putExtra(CATEGORIES_MESSAGE, result);
            startActivity(intent);
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 10000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public void getPDF1(View view) {
        // Do something in response to button
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://site.republicservices.com/site/corvallis-or/en/documents/corvallisrecycledepot.pdf"));
        startActivity(browserIntent);
    }

    public void getPDF2(View view) {
        // Do something in response to button
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://site.republicservices.com/site/corvallis-or/en/documents/detailedrecyclingguide.pdf"));
        startActivity(browserIntent);
    }


}