package com.example.labb_4;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.Locale;

public class CartView extends AppCompatActivity {

    private CartListAdapter cla;
    private ListView cart_listview;
    private Cart cart;
    private Button btnCheckout;
    private static TextView displayTotal;
    private dataHandler dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnCheckout = findViewById(R.id.cart_checkout_button);
        displayTotal = findViewById(R.id.displayTotal);

        cart = Cart.getInstance();
        dh = dataHandler.getInstance();

        ArrayList<Product> savedCartList = dh.readFromFile(this);
        if (savedCartList != null && !savedCartList.isEmpty()) {
            cart.updateCart(savedCartList);
        }

        cart_listview = findViewById(R.id.cart_list);
        cla = new CartListAdapter(this, R.layout.cart_item_handler, cart.getCartList());
        cart_listview.setAdapter(cla);

        displayTotal.setText(String.format(Locale.getDefault(), "$%.2f", cart.getTotalPrice()));

        cla.update(cart.getCartList());
        Log.i("Cart", "ListAdapter notified: " + cart.getCartList().size());

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cart.getCartList().isEmpty()) {

                    int itemCount = cart.getCartSize();
                    String message = "Thank you for your purchase!\n" +
                            "You bought " + itemCount + " items for $" + String.format("%.2f", cart.getTotalPrice());

                    Toast.makeText(CartView.this, message, Toast.LENGTH_LONG).show();

                    cart.checkout();
                    dh.writeToFile(CartView.this, cart.getCartList());

                    cla.update(cart.getCartList());
                    displayTotal.setText(String.format(Locale.getDefault(), "$%.2f", cart.getTotalPrice()));
                    finish();
                } else {
                    Toast.makeText(CartView.this, "The cart is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static TextView getDisplayTotal ( ) {
        return displayTotal;
    }
}