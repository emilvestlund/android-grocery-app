package com.example.labb_4;
import java.io.Serializable;


public class Product implements Serializable {  /* Class Product --
                                                 *  This class holds the logic and attributes of the Product objects.
                                                 */



    private String name, description, company, price, category, id;



    public Product(String price, String name, String description, String company, String category, String id){


        this.name = name;
        this.price = price;
        this.description = description;
        this.company = company;
        this.category = category;
        this.id = id;

    }


    public String getCompany() { return this.company; }

    public String getName(){ return this.name; }

    public String getProductPrice(){ return this.price; }

    public String getDescription() { return this.description; }

    public String getCategory() { return this.category; }

    public String getID() { return this.id; }



}