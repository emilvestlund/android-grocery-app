package com.example.labb_4;
import android.widget.ImageView;

import java.io.Serializable;


public class Category implements Serializable {

    private String category, id;



    public Category( String name,  String id ) {

        this.category = name;
        this.id = id;

    }

    public String getName(){ return this.category; }

    public String getID() { return this.id; }


}