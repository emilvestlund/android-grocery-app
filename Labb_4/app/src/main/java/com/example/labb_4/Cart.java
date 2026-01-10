package com.example.labb_4;

import android.util.Log;
import java.util.ArrayList;

public class Cart {   /*  Cart --
                       *  This class holds the logic for handling the cart's different methods.
                       *  The Cart should be able to add, remove and checkout as well as some other logic related to the cart.
                       *  This class also uses Singleton pattern to ensure we don't have duplicated instances.
                       */

    private ArrayList <Product> cartList;

    private static Cart instance; // Create a static instance which will be a reference to the cart.
    public Cart(){

        this.cartList = new ArrayList <Product>(); // Creates a new ArrayList to hold the products in the cart on instancing.
    }

    public static Cart getInstance() { // Method to get the instance of the cart or make a new once if there isn't one.

        if (instance == null) {

            instance = new Cart();
        }
        return instance;
    }

    public ArrayList <Product> getCartList(){
        return this.cartList;
    }

    public void add(Product product) {
        cartList.add(product);
        Log.i("Cart", "Product added to cart: " + product.getName() + " Cart size: " + cartList.size());
    }

    public void remove(Product product) {
        cartList.remove(product);
        Log.i("Cart", "Product removed: " + product.getName() + " Cart size: " + cartList.size());
    }

    public void checkout() {
        cartList.clear();
        updateCart(cartList);
    }
    public void updateCart (ArrayList<Product> cartList) {
        this.cartList = cartList;
    }
    public int getCartSize() {
        return cartList.size();
    }
    public double getTotalPrice() { // Method to get the total price of the cart by adding up the prices of the products in the cart.

        double totalPrice = 0;

        for (Product product : cartList) {
           totalPrice += Double.parseDouble(product.getProductPrice());
        }
        return totalPrice;
    }
}
