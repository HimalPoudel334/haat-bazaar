package com.example.testapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.ProductDetailActivity;
import com.example.testapp.R;
import com.example.testapp.interfaces.ProductAPI;
import com.example.testapp.models.Cart;
import com.example.testapp.network.RetrofitClient;
import com.example.testapp.responses.ProductResponses;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<Cart> cartList;
    private List<Cart> backupCartList;
    private final Context cartContext;
    private final OnCheckBoxStateChangeListener checkBoxStateChangeListener;
    private final List<Cart> selectedCarts;

    public CartRecyclerViewAdapter(List<Cart> cartList, Context cartContext, OnCheckBoxStateChangeListener checkBoxStateChangeListener) {
        this.cartList = cartList;
        this.cartContext = cartContext;
        this.checkBoxStateChangeListener = checkBoxStateChangeListener;
        this.selectedCarts = new ArrayList<>();
        Log.d("Cart const", "CartRecyclerViewAdapter: cartList size: "+this.cartList.size());

    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setBackupCartList(List<Cart> cartList) {
        this.backupCartList = cartList;
        Log.d("Cart", "setBackupCartList: "+backupCartList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart currentCart = cartList.get(position);
        if(Objects.equals(currentCart.getProductName(), "Mango")) {
            currentCart.setImage(RetrofitClient.BASE_URL+"/"+currentCart.getImage());
        }
        holder.productName.setText(currentCart.getProductName());
        holder.productName.setOnClickListener(v -> {
            Retrofit retrofit = RetrofitClient.getClient();
            ProductAPI productAPI = retrofit.create(ProductAPI.class);
            productAPI.getProduct(currentCart.getProductId()).enqueue(new Callback<ProductResponses.SingleProductResponse>() {
                @Override
                public void onResponse(Call<ProductResponses.SingleProductResponse> call, Response<ProductResponses.SingleProductResponse> response) {
                    if(response.body() != null && response.isSuccessful()) {
                        Intent intent = new Intent(cartContext, ProductDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("selectedProduct", response.body().getProduct());
                        intent.putExtras(bundle);
                        cartContext.startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<ProductResponses.SingleProductResponse> call, Throwable t) {

                }
            });
        });
        holder.productPrice.setText(String.format("%s", currentCart.getPrice()));
        holder.createdOn.setText(String.format("%s %s", "On", currentCart.getCreatedOn()));
        Glide.with(cartContext)
                .load(currentCart.getImage())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.cartProductImage);

        holder.cartCheckBox.setChecked(currentCart.isChecked());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox cartCheckbox = (CheckBox) v;
                if(cartCheckbox.isChecked()) {
                    cartList.get(pos).setChecked(true);
                    selectedCarts.add(cartList.get(pos));
                }
                else if(!cartCheckbox.isChecked()) {
                    cartList.get(pos).setChecked(false);
                    selectedCarts.remove(cartList.get(pos));
                }
                checkBoxStateChangeListener.onCheckBoxStateChanged(selectedCarts);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Log.d("Cart adapter", "performFiltering: "+charSequence);

                final List<Cart> filteredCarts = new ArrayList<>();
                if(charSequence.toString().isEmpty()) {
                    filteredCarts.addAll(backupCartList);
                }
                else {
                    for(Cart c : cartList) {
                        if(c.getProductName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filteredCarts.add(c);
                        }
                    }
                    Log.d("Filtered Cart", "performFiltering: "+filteredCarts.size());
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCarts;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<Cart> gotFilteredList = (List<Cart>) filterResults.values;
                Log.d("Cart", "publishResults: "+gotFilteredList.size());
                // Check if the filtered list size matches the current list size
                if (cartList.size() != gotFilteredList.size() || !new HashSet<>(cartList).containsAll(gotFilteredList)) {
                    notifyItemRangeRemoved(0, cartList.size());
                    cartList.clear();
                    cartList.addAll(gotFilteredList);
                    //notifyItemRangeChanged(0, gotFilteredList.size());
                    notifyItemRangeInserted(0, gotFilteredList.size());
                }

            }
        };
    }

    public interface OnCheckBoxStateChangeListener {
        void onCheckBoxStateChanged(List<Cart> selectedCart);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName, productPrice, quantity, createdOn;
        ImageView cartProductImage, quantityPlus, quantityMinus;
        CheckBox cartCheckBox;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //get the views from the cart_item resource
            productName = itemView.findViewById(R.id.cart_product_title);
            productPrice = itemView.findViewById(R.id.cart_product_price);
            quantity = itemView.findViewById(R.id.quantity_tv);
            createdOn = itemView.findViewById(R.id.cart_created_date);
            cartProductImage = itemView.findViewById(R.id.cart_product_image_view);
            quantityPlus = itemView.findViewById(R.id.quantity_plus);
            quantityMinus = itemView.findViewById(R.id.quantity_minus);
            cartCheckBox = itemView.findViewById(R.id.cart_item_checkbox);
            cartCheckBox.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View v, int pos);
    }
}

