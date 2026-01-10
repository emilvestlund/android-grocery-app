package com.example.labb_4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.RequestQueue;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements VolleyCallback {

                                                                /*  Class MainActivity --
                                                                *   This class is the starting activity of the application
                                                                *   the activity fetches 10 products from the API from page 1
                                                                *   and sets them in a listview.
                                                                *   The API call is made by the API_Provider() class.
                                                                *   This activity also holds methods to open the Cart and Categories activities.
                                                                *   The class also implements the VolleyCallback interface to handle the API response.
                                                                *   As long as we promise to implement the onSuccess and onFailure methods,
                                                                *   as VolleyCallBack is an interface.
                                                                *
                                                                */
    private ProductListAdapter pla; // Reference to the ProductListAdapter class
    private ArrayList < Product > products; // List to hold product objects
    private ListView productList; // Listview which is being inflated by the adapter in this activity
    private Button nextPage;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); //Set layout for this activity
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), ( v, insets ) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nextPage = findViewById(R.id.btnNext);

        productList = findViewById(R.id.productList); // Get the listview from the layout
        products = new ArrayList < Product >(); // Create a new list of product objects
        pla = new ProductListAdapter(this, R.layout.list_item_handler, products); // pla takes the context, layout (how the listview should look) and the list which will populate the listview.
        productList.setAdapter(pla);    // Listview method to adapt the listview to the adapter.

        API_Provider api = API_Provider.getInstance(this); // Create a new API_Provider object by using singleton pattern to make sure we don't have duplicated instances. getInstance() returns an existing instance if it exists.
        RequestQueue requestQueue = api.getRequestQueue(); // Get the request queue from the API_Provider object.
        String url = api.getURL(); // Get the URL from the API_Provider object. This method returns an URL which will get 10 products from page 1 from the products array.
        api.fetchData(requestQueue, this, url); // fetchData() takes a requestQueue, volleyCallback and a URL as parameters.
        // The callback is used to handle the API response. It will return a JSONObject to our onSuccess method or JSONError to our onFailure method.


    }
    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    public void onSuccess ( JSONObject object ) {

        Log.i("API_RESPONSE", "Success"); // Log to check if the API call was successful.

        try {

            if(object.has("error")){
                Log.e("API", "No more products to load...");
                Toast.makeText(this, "Couldn't load any new products", Toast.LENGTH_LONG).show();
                nextPage.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                nextPage.setClickable(false);
                return;
            }

            JSONArray productsArray = object.getJSONArray("products"); // Get the products array from the JSON object.

            for (int i = 0; i < productsArray.length(); i++) { // We iterate over the array to get the product objects and assign the object's data to strings.
                JSONObject productObject = productsArray.getJSONObject(i);

                // Assign the data received to strings, a product is then made for each iteration and added to the list. The list will then be used to populate the listview.
                String name = productObject.getString("name");
                String description = productObject.getString("description");
                String price = productObject.getString("price");
                String company = productObject.getString("company");
                String category = productObject.getString("category");
                String id = productObject.getString("id");

                Log.d("API", "Name: " + name);
                Log.d("API", "Description: " + description);
                Log.d("API", "Price: " + price);
                Log.d("API", "Company: " + company);
                Log.d("API", "Category: " + category);
                Log.d("API", "ID: " + id);

                // Create a new product object and add it to the list for each iteration.
                Product product = new Product(price, name, description, company, category, id);

                // Check if the product is already in the list. If it is not, add it to the list.
                if(!pla.isDuplicate(product)) {
                    products.add(product);
                    Log.d("API", "Product added to list");
                }
            }

            pla.update(products); // Update the listview with the new data. update() Uses the notifyDataSetChanged() method to update the listview.

        } catch (Exception e) {
            // Error handling if we fail to get data from the API, we as devs get the actual Exception information
            //The user gets a 'user-friendly' version of the message basically saying we failed to load the inventory, it's not the users fault.
            e.printStackTrace();
            Toast.makeText(this, "Error, Failed to load products, try again later...", Toast.LENGTH_LONG).show();
            Log.e("API", "Error parsing JSON", e);
        }
    }

    @Override
    public void onFailure ( Exception e ) {

        Log.e("API_RESPONSE", "Failure", e);
        Toast.makeText(this, "Error [500] - Server Error, Failed to load the inventory, verify your internet connection and try again.", Toast.LENGTH_LONG).show();

    }

    public void openCart ( View view ) { // Method to open the Cart activity when the button is clicked by sending an intent.
        Intent intent = new Intent(this, CartView.class);
        startActivity(intent);
    }

    public void openCategories ( View view ) { // Method to open the Categories activity when the button is clicked by sending an intent.

        Intent intent = new Intent(this, CategoriesView.class);
        startActivity(intent);
    }

    public void loadMore ( View view ) { // Method to load more products when the button is clicked, missed this.
        Log.i("API", "Loading more products");

        API_Provider api = API_Provider.getInstance(this);
        RequestQueue requestQueue = api.getRequestQueue();
        String url = api.getNextPageURL();
        api.fetchData(requestQueue, this, url);
    }
}