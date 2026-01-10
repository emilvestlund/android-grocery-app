package com.example.labb_4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartListAdapter extends ArrayAdapter<Product> {

    private int layout;
    private List<Product> cartList;
    private Context context;
    private TextView displayTotal;
    private dataHandler dh;

    public CartListAdapter(Context context, int layout, List<Product> cartList) {
        super(context, layout, cartList);

        this.layout = layout;
        this.cartList = cartList;
        this.context = context;

    }

    public void update(ArrayList<Product> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(layout, parent, false);

            TextView company = view.findViewById(R.id.list_item_company);
            TextView name = view.findViewById(R.id.list_item_text);
            TextView price = view.findViewById(R.id.list_item_price);
            TextView desc = view.findViewById(R.id.list_item_description);
            Button button = view.findViewById(R.id.list_item_button_remove);

            listItemHandler item = new listItemHandler(company, name, price, desc, button);
            set(item, position);
            view.setTag(item);

        } else {
            listItemHandler main = (listItemHandler) view.getTag();
            set(main, position);
        }

        return view;
    }

    public void set(listItemHandler liHandler, int position) {
        Product product = cartList.get(position);

        liHandler.getProductCompany().setText(product.getCompany());
        liHandler.getProductName().setText(product.getName());

        liHandler.getProductPrice().setText(product.getProductPrice());


        liHandler.getProductButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        if (position == -1) {
                            Log.e("REMOVE", "No item selected.");
                            return;
                        }

                        Product product = cartList.get(position);
                        Cart.getInstance().remove(product);
                        dh = dataHandler.getInstance();

                        dh.writeToFile(context, Cart.getInstance().getCartList());

                        displayTotal = CartView.getDisplayTotal();
                        displayTotal.setText(String.format(Locale.getDefault(), "$%.2f", Cart.getInstance().getTotalPrice()));


                    update(Cart.getInstance().getCartList());

                        Log.i("CART", "Removed item: " + product.getName());
                        Log.i("CART", "Cart size: " + Cart.getInstance().getCartList().size());

                } catch (Exception e) {
                    Log.e("BUTTON CLICK", "Failed to handle button click", e);
                }
            }
        });

    }
}
