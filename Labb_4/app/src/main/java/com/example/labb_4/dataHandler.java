package com.example.labb_4;

import android.content.Context;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class dataHandler { // Class to handle writing and reading to file. Taken from code example from the Git repo.

    public static dataHandler instance;
    private final String FILENAME = "products.txt";

    public static dataHandler getInstance() {

        if (instance == null) {

            instance = new dataHandler();
        }
        return instance;
    }

    public void writeToFile(Context context, ArrayList<Product> list){
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);

            oos.close();
            fos.close();

        } catch(Exception e){

            Log.e("Error write file", e.toString());
        }
    }

    public ArrayList<Product> readFromFile(Context context){
        ArrayList<Product> cart = new ArrayList<Product>();


        try {

            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            cart = (ArrayList<Product>) ois.readObject();

            ois.close();
            fis.close();


        }  catch(Exception e) {
            Log.e("Error read file", e.toString());
        }

        return cart;
    }


}