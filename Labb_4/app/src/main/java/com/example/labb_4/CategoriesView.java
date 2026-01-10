package com.example.labb_4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesView extends AppCompatActivity implements VolleyCallback { // Class to handle the categories view displaying all the available categories.
                                                                                    // The logic of the class is quite similar to the MainActivity class.
                                                                                    // It uses a different adapter, layout and URL to make an API Call to get categories instead of products.

    private CategoriesListAdapter categoriesListAdapter;
    private ListView categories_listview;
    private ArrayList <Category> categoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categories_listview = findViewById(R.id.categories_list_view);

        categoryList = new ArrayList <Category>();
        categoriesListAdapter = new CategoriesListAdapter(this, R.layout.categories_item_handler, categoryList);
        categories_listview.setAdapter(categoriesListAdapter);

        API_Provider api = API_Provider.getInstance(this);
        RequestQueue requestQueue = api.getRequestQueue();
        String url = api.getCategoryURL();
        api.fetchData(requestQueue, this, url);

    }


    @Override
    public void onSuccess(JSONObject object) {

        Log.i("API_CATEGORY_RESPONSE", "Success");

        try {
            JSONArray productsArray = object.getJSONArray("categories");

            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject productObject = productsArray.getJSONObject(i);

                String id = productObject.getString("id");
                String name = productObject.getString("name");

                Log.d("API", "Name: " + name);
                Log.d("API", "ID: " + id);

                Category category = new Category(name, id);
                categoryList.add(category);
                Log.d("API", "Category added to list" + "List size: " + categoryList.size());

            }

            categoriesListAdapter.update(categoryList);
            Log.d("CategoriesView", "Adapter notified, data updated");

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(this, "Error [500] - Server Error, Failed to load categories.", Toast.LENGTH_LONG).show();
            Log.e("API_RESPONSE", "Error parsing JSON", e);

        }

    }

    @Override
    public void onFailure(Exception e) {

        Toast.makeText(this, "Error [500] - Server Error, Failed to load categories from server. Please verify your internet connection." , Toast.LENGTH_LONG).show();
        Log.e("API_CATEGORY_RESPONSE", "Failure");

    }
}