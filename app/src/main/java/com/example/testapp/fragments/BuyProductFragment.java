package com.example.testapp.fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.models.Product;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyProductFragment extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Product product;

    public BuyProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.close_fragment_icon).setOnClickListener(v -> {
            this.dismiss();
        });

        ImageView productImage = view.findViewById(R.id.product_about_to_buy_iv);
        Glide.with(getContext())
                .load(product.getProductImage())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(productImage);

        TextView productPrice = view.findViewById(R.id.product_about_to_buy_price_tv);
        productPrice.setText(String.format("%s per %s", product.getProductPrice(), product.getProductUnit()));

        TextView quantityTextView = view.findViewById(R.id.quantity_tv);

        ImageView quantityPlus = view.findViewById(R.id.quantity_plus);
        quantityPlus.setOnClickListener(v -> updateQuantity(quantityTextView, 0.5));

        ImageView quantityMinus = view.findViewById(R.id.quantity_minus);
        quantityMinus.setOnClickListener(v -> updateQuantity(quantityTextView, -0.5));

        TextView productPreviousPrice = view.findViewById(R.id.product_about_to_buy_prev_price);
        productPreviousPrice.setText(String.format("%s %s", product.getProductPreviousPrice(), product.getProductUnit()));
        productPreviousPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        Button cancelButton = view.findViewById(R.id.order_cancel_button);
        cancelButton.setOnClickListener(v -> this.dismiss());

        Button placeOrderButton = view.findViewById(R.id.place_order_button);
        placeOrderButton.setOnClickListener(v -> Toast.makeText(getContext(), "Order placed", Toast.LENGTH_SHORT).show());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param product The product about to be bought.
     * @return A new instance of fragment BuyProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyProductFragment newInstance(Product product) {
        BuyProductFragment fragment = new BuyProductFragment();
        Bundle args = new Bundle();
        args.putParcelable("productAboutToBuy", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getParcelable("productAboutToBuy");
            Log.d("Fragment", "onCreate: product is " + product.getProductName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_product, container, false);
    }

    private void updateQuantity(TextView quantityTV, double change) {
        double quantity = Double.parseDouble(quantityTV.getText().toString().split(" ")[0]);
        quantity += change;
        if (quantity > 0) {
            quantityTV.setText(String.format("%s %s", quantity, product.getProductUnit()));
        }
    }
}