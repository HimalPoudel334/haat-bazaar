package com.example.testapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.models.OrderItem;

import java.util.List;

public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.ViewHolder> {

    private List<OrderItem> orderItems;
    private Context context;

    public OrderItemListAdapter(List<OrderItem> orderItems, Context context) {
        this.orderItems = orderItems;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_list_item, parent, false);
        return new OrderItemListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemListAdapter.ViewHolder holder, int position) {
        Glide.with(context)
            .load(orderItems.get(position).getProduct().getImage())
            .centerCrop()
            .into(holder.imageView);

        holder.productNameTv.setText(String.format("Product: %s", orderItems.get(position).getProduct().getName()));
        holder.priceTv.setText(String.format("Price: Rs %s", orderItems.get(position).getProduct().getPrice()));
        holder.quantityTv.setText(String.format("Quantity: %s %s", orderItems.get(position).getQuantity(), orderItems.get(position).getProduct().getUnit()));
        holder.discountTv.setText(String.format("Discount: Rs %s", orderItems.get(position).getDiscount()));
        holder.amountTv.setText(String.format("Total: \nRs %s", orderItems.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView productNameTv, priceTv, quantityTv, discountTv, amountTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            productNameTv = itemView.findViewById(R.id.product_name_tv);
            priceTv = itemView.findViewById(R.id.price_tv);
            quantityTv = itemView.findViewById(R.id.quantity_tv);
            discountTv = itemView.findViewById(R.id.discount_tv);
            amountTv = itemView.findViewById(R.id.total_tv);
        }
    }
}
