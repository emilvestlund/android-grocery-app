package com.example.labb_4;

import android.widget.Button;
import android.widget.TextView;

public class listItemHandler {


    private TextView productCompany;            // listItemHandler --
                                                // This class holds the views for the listview to be set in the adapter.
                                                // Class to return views for us to manipulate in the adapter. I use 3 different itemHandlers for 3 different views.
    private TextView productName;
    private TextView productPrice;
    private TextView productDescription;
    private Button productButton;

    public listItemHandler( TextView productCompany, TextView productName, TextView productPrice, TextView productDescription , Button productButton) {


        this.productCompany = productCompany;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productButton = productButton;

    }


    public TextView getProductCompany() { return productCompany; }

    public TextView getProductName(){
        return productName;
    }

    public TextView getProductPrice() { return productPrice; }

    public TextView getProductDescription() { return productDescription; }

    public Button getProductButton(){
        return productButton;
    }
}