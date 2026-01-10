package com.example.labb_4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ItemDescription extends AppCompatActivity {

    private Cart cart;
    private Button btnBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_description);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView companyText = findViewById(R.id.list_item_company);
        TextView nameText = findViewById(R.id.list_item_text);
        TextView descriptionText = findViewById(R.id.list_item_description);
        TextView priceText = findViewById(R.id.list_item_price);

        btnBuy = findViewById(R.id.list_item_button);

        Intent intent = getIntent();

        if (!intent.hasExtra("product")) {
            Log.e("ItemDescription", "There was no product at the start of the activity");
            return;
        }

        Product product = (Product) intent.getSerializableExtra("product");

        if (product != null) {

            companyText.setText(product.getCompany());
            nameText.setText(product.getName());
            descriptionText.setText(product.getDescription());
            priceText.setText(String.valueOf(product.getProductPrice()));
            setTitle(product.getName());

        } else {

            Log.e("ItemDescription", "There was no product to present");

        }
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick ( View v )
            {
                cart = Cart.getInstance();
                cart.add(product);
                Log.i("ItemDescription", "Bought item: " + product.getName());
            }
        });

    }


}