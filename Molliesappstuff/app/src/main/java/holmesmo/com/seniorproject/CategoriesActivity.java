package holmesmo.com.seniorproject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mollie on 5/29/2015.
 */
//help from http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
public class CategoriesActivity extends ListActivity {
    public final static String ITEMS_MESSAGE = "com.holmesmo.seniorproject.ITEMS_MESSAGE";
    private static final String DEBUG_TAG = "HttpExample";
    Context cntxt = this;
    JSONArray categories = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> categoriesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.CATEGORIES_MESSAGE);

        categoriesList = new ArrayList<HashMap<String, String>>();

        if(message != null) {
            try {

                JSONObject jsonObj = new JSONObject(message);
                // Getting JSON Array node
                categories = jsonObj.getJSONArray("categories");

                // looping through All categories
                for (int i = 0; i < categories.length(); i++) {
                    JSONObject cat = categories.getJSONObject(i);

                    String id = cat.getString("id");
                    String name = cat.getString("name");
                    String category_url = cat.getString("category_url");


                    // tmp hashmap for single contact
                    HashMap<String, String> category = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    category.put("id", id);
                    //art.put("artist", artist);
                    category.put("name", name);
                    category.put("category_url", category_url);

                    // adding art to artwork list
                    categoriesList.add(category);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ListAdapter adapter = new SimpleAdapter(
                CategoriesActivity.this, categoriesList,
                R.layout.category_item, new String[] { "name", "category_url"},
                new int[] { R.id.name, R.id.category_url});

        setListAdapter(adapter);

        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //getJson from url for that category
                String stringUrl = categoriesList.get(position).get("category_url");
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
        });
    }

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
            Intent intent = new Intent(cntxt, ItemsActivity.class);
            intent.putExtra(ITEMS_MESSAGE, result);
            startActivity(intent);
        }
    }

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







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);
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
}
