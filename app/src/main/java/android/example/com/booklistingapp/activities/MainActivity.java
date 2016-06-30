package android.example.com.booklistingapp.activities;

import android.content.Context;
import android.example.com.booklistingapp.R;

import android.example.com.booklistingapp.adapters.BookAdapter;
import android.example.com.booklistingapp.models.Book;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String DEFAULT_QUERY = "android";
    private ListView lvBooks;
    private BookAdapter bookAdapter;
    private ProgressBar progressBar;
    String searchQuery = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the views
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        lvBooks = (ListView) findViewById(R.id.lvBooks);

        //Creates the BookAdapter and assign it to the ListView
        ArrayList<Book> aBooks = new ArrayList<>();
        bookAdapter = new BookAdapter(this, aBooks);
        lvBooks.setAdapter(bookAdapter);

        //fetch default Books
        fetchBooks(DEFAULT_QUERY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                fetchBooks(query);
                searchQuery = query;
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // Set activity title to search query
                MainActivity.this.setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    // Executes a call to the Google Books API, parses the results and
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks(String query) {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new BookClient().execute(query);

        } else {
            Snackbar.make(lvBooks, "No connection", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    /**
     * Google Books API client to send network requests
     */
    public class BookClient extends AsyncTask<String, Void, JSONObject> {

        private static final String API_BASE_URL = "https://www.googleapis.com/";

        @Override
        protected JSONObject doInBackground(String... query) {

            try {
                return downloadUrl(API_BASE_URL + "books/v1/volumes?q=" + query[0]);
            } catch (IOException e) {
                return null;
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(JSONObject result) {

            if (result != null) {
                try {

                    int totalItems = result.getInt("totalItems");

                    if(totalItems !=0){
                        // Get the docs json array
                        JSONArray docs = result.getJSONArray("items");
                        // Parse json array into array of model objects
                        final ArrayList<Book> books = Book.fromJson(docs);
                        // Remove all books from the adapter
                        bookAdapter.clear();
                        // Load model objects into the adapter
                        for (Book book : books) {
                            bookAdapter.add(book); // add book through the adapter
                        }
                        bookAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(ProgressBar.GONE);
                    }
                    else{
                        Snackbar.make(lvBooks, "No results found for " + searchQuery, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private JSONObject downloadUrl(String myurl) throws IOException {
            InputStream is = null;

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
                is = conn.getInputStream();

                return readIt(is);

            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        private JSONObject readIt(InputStream is) {

            JSONObject jsonObject = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;

            try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

                jsonObject = new JSONObject(sb.toString());

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

    }

}
