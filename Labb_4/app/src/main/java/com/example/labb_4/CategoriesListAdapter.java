package com.example.labb_4;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class CategoriesListAdapter extends ArrayAdapter < Category > {

    private int layout;
    private List < Category > categoryList;
    private Context context;


    public CategoriesListAdapter ( Context context, int layout, List<Category> categoryList ) {

        super(context, layout, categoryList);
        this.layout = layout;
        this.categoryList = categoryList;
        this.context = context;
    }

    public void update(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }


    @Override
    public View getView ( int position, View view, ViewGroup parent ) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(layout, parent, false);

            TextView categoryName = view.findViewById(R.id.category_name);
            String categoryID = categoryList.get(position).getID();

            CategoryHandler item = new CategoryHandler(categoryName, categoryID);
            set(item, position);
            view.setTag(item);

        } else {
            CategoryHandler main = (CategoryHandler) view.getTag();
            set(main, position);
            Log.i("CategoriesListAdapter", "View set successful");
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = categoryList.get(position);
                Log.i("CategoriesListAdapter", "Category clicked: " + category.getName() + " with ID: " + category.getID());
                Intent intent = new Intent(context, CategorySelection.class);
                intent.putExtra("category", category.getName());
                intent.putExtra("categoryID", category.getID());
                context.startActivity(intent);
            }
        });

        return view;
    }

    public void set ( CategoryHandler categoryHandler, int position ) {
        Category category = categoryList.get(position);
        categoryHandler.getCategory().setText(category.getName());
        Log.i("CategoriesListAdapter", "Category name: " + category.getName());
    }
}




