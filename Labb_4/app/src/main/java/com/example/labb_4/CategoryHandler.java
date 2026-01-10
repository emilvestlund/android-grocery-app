package com.example.labb_4;

import android.widget.TextView;


public class CategoryHandler {

    private TextView categoryName;
    private String categoryID;


    public CategoryHandler( TextView categoryName, String categoryID) {

       this.categoryName = categoryName;
       this.categoryID = categoryID;

    }


    public TextView getCategory() { return categoryName; }

    public String getCategoryID() { return categoryID; }

}

