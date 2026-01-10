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

public class CategorySelection extends AppCompatActivity implements VolleyCallback {

    private ProductListAdapter pla;
    private Cart cart;
    private ArrayList <Product> categoryProducts;
    private ListView category_product_listview;
    private Button btnNextCategory;
    String category;
    String categoryID;



    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), ( v, insets ) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        API_Provider.getInstance(this).resetPage();

        Intent intent = getIntent();

        category = intent.getStringExtra("category");
        categoryID = intent.getStringExtra("categoryID");

        setTitle(category);

        Log.d("CategorySelection", "Category: " + category + " with ID: " + categoryID);

        cart = Cart.getInstance();
        category_product_listview = findViewById(R.id.category_products);

        btnNextCategory = findViewById(R.id.btnNextCategories);

        categoryProducts = new ArrayList <Product>();

        pla = new ProductListAdapter(this, R.layout.list_item_handler, categoryProducts);
        category_product_listview.setAdapter(pla);

        API_Provider api = API_Provider.getInstance(this);
        RequestQueue requestQueue = api.getRequestQueue();
        String url = "https://informatik-webbkurser.hotell.kau.se/WebAPI/v1/products?" + "limit=" + 10 + "&category=" + categoryID + "&page=" + 1 + "&apikey=h2s269nsMn012NASi2537bsA9dBSa2";
        api.fetchData(requestQueue, this, url);

    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    public void onSuccess ( JSONObject object ) {

        Log.i("API_RESPONSE", "Success"); // Log to check if the API call was successful.

        try {

            if(object.has("error")){
                Log.e("API", "No more products to load...");
                Toast.makeText(this, "Couldn't load any new products", Toast.LENGTH_LONG).show();
                btnNextCategory.setBackgroundTintList(getResources().getColorStateList(R.color.red));
                btnNextCategory.setClickable(false);
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
                    categoryProducts.add(product);
                    Log.d("API", "Product added to list");
                }
            }

            pla.update(categoryProducts); // Update the listview with the new data. update() Uses the notifyDataSetChanged() method to update the listview.

        } catch (Exception e) {

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
    public void openCart( View view) {
        Log.i("CategorySelection", "Opening cart");
        Intent intent = new Intent(this, CartView.class);
        startActivity(intent);
    }

    public void openCategories(View view) {
        Log.i("CategorySelection", "Opening categories");
        Intent intent = new Intent(this, CategoriesView.class);
        startActivity(intent);
    }

    public void loadNextPage ( View view) {
        Log.i("CategorySelection", "Loading next page");

        API_Provider api = API_Provider.getInstance(this);
        RequestQueue requestQueue = api.getRequestQueue();
        String url = "https://informatik-webbkurser.hotell.kau.se/WebAPI/v1/products?" + "limit=" + 10 + "&category=" + categoryID + "&page=" + api.nextCategoryPage() + "&apikey=h2s269nsMn012NASi2537bsA9dBSa2";
        api.fetchData(requestQueue, this, url);

        Log.i("CategorySelection", url);
    }
}