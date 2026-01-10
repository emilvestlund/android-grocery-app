package com.example.labb_4;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends ArrayAdapter<Product> {
                                                                    /*  ProductListAdapter --
                                                                     *  This class is used to handle the adapter for the listview.
                                                                     *  I use this class in the CategorySelection and MainActivity classes.
                                                                     *  The class inflates the listview and gets/sets the views.
                                                                     *  As well as handles GUI interaction logic of the listview.
                                                                     *  The class extends ArrayAdapter meaning it acts like the base class ArrayAdapter.
                                                                     *  We therefore have to call the super class constructor in the constructor as well as pass the context, layout and products in the constructor.
                                                                     */

    private int layout; // Layout to inflate, holds the ID of the layout.
    private List<Product> products; // List to hold products
    private Context context;    // Context to use for inflating the layout, this let's the adapter know what context to use. (Where we are in the app)
    private dataHandler dh; // DataHandler to use for writing to file.

    public ProductListAdapter(Context context, int layout, List<Product> products) {
        super(context, layout, products);

        this.layout = layout;
        this.products = products;
        this.context = context;

    }

    public void update(ArrayList<Product> newList) { // Method to update the listview with new products and notify the adapter of change
        this.products = newList;
        notifyDataSetChanged();
    }
    // Method to get the view for the listview. getView is a core method of any adapter class.
    // We know that it's a super class's method when we use @Override, this let's to change the method and still have the class call it's "own" method when needed.
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(layout, parent, false);

            TextView company = view.findViewById(R.id.list_item_company);
            TextView name = view.findViewById(R.id.list_item_text);
            TextView price = view.findViewById(R.id.list_item_price);
            TextView desc = view.findViewById(R.id.list_item_description);
            Button button = view.findViewById(R.id.list_item_button);

            // List item handler to hold the views.
            listItemHandler item = new listItemHandler(company, name, price, desc, button);
            // Set the views in the listview. What items and what position.
            set(item, position);
            view.setTag(item);

        } else {
            // Get the list item handler from the view if the view isn't null.
            listItemHandler main = (listItemHandler) view.getTag();
            set(main, position);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override // Listener for when a user clicks the name of a product.
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDescription.class); // Create an intent to a view that shows the description of the product.
                Product selectedProduct = products.get(position); // Get the product that was clicked.
                intent.putExtra("product", selectedProduct); // Put the product in the intent.
                context.startActivity(intent); // Start the activity from wherever it was clicked, Context is good to pass when we want the method to be dynamic from any context and the method requires context.
            }
        });

        // Return the view.
        return view;
    }
    // Method to set the views in the listview.
    public void set(listItemHandler liHandler, int position) {
        Product product = products.get(position); // Get the product from the list at a specific position.

        // LiHandler is used here to set the views that we obtained in the getView method.
        liHandler.getProductCompany().setText(product.getCompany());
        liHandler.getProductName().setText(product.getName());
        liHandler.getProductPrice().setText(product.getProductPrice());
        liHandler.getProductDescription().setText(product.getDescription());

        // ClickListener to handle button clicks on the "Buy" buttons, executes purchase logic by accessing methods from Cart and dataHandler classes.
        liHandler.getProductButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (position == -1) { // If the click failed, we log an error. This is to log any out of bounds errors.
                        Log.e("BUY", "No item selected.");
                        return; // Return as the method can't continue.
                    }

                    Product boughtItem = products.get(position); // Else get the product which was clicked.
                    Cart.getInstance().add(boughtItem); // Store it in the Cart's list.

                    dh = dataHandler.getInstance(); // Get existing dataHandler instance if it exists, otherwise create a new instance.
                    dh.writeToFile(context, Cart.getInstance().getCartList()); // Write the cartList to the file.

                    Log.i("BUY", "Bought item: " + boughtItem.getName());

                } catch (Exception e) {
                    Toast.makeText(context, "Error - Failed to get selected product, please try again...", Toast.LENGTH_LONG).show();
                    Log.wtf("BUTTON CLICK", "Failed to handle button click", e);
                }
            }
        });
    }
    public boolean isDuplicate ( Product product ) {
        for (Product p : products) {
            if (p.getID().equals(product.getID())) {
                Log.e("API", "Duplicate product found: " + product.getName());
                return true;
            } else if (p.getName().equals(product.getName()) && !p.getID().equals(product.getID()) && p.getCompany().equals(product.getCompany()))
                {
                    return true;
                }
        }
        return false;
    }
}
