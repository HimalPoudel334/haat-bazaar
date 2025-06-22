package com.example.testapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.OrderDetailActivity;
import com.example.testapp.R;
import com.example.testapp.models.Order;
import com.example.testapp.responses.OrderResponses;

import java.util.List;
import java.util.Locale;

public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.ViewHolder> {

    private List<OrderResponses.AllOrder> newOrderList;
    private Context context;

    public OrdersListAdapter(List<OrderResponses.AllOrder> newOrderList, Context context) {
        this.newOrderList = newOrderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list_item, parent, false);
        return new OrdersListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersListAdapter.ViewHolder holder, int position) {

        Glide.with(context)
                .load(newOrderList.get(position).getProductImage())
                .into(holder.orderProductImage);

        holder.orderDateTv.setText(String.format(Locale.ENGLISH, "On: %s", newOrderList.get(position).getCreatedOn()));
        holder.orderProductNameTv.setText(newOrderList.get(position).getProductName());
        holder.orderDeliveryDateTv.setText(String.format(Locale.ENGLISH, "Delivery Date: %s", newOrderList.get(position).getFulfilledOn()));
        holder.orderDeliveryLocationTv.setText(String.format(Locale.ENGLISH, "Delivery Location: %s", newOrderList.get(position).getDeliveryLocation()));
        holder.orderStatusTv.setText(String.format(Locale.ENGLISH, "Status: %s", newOrderList.get(position).getStatus()));
        holder.orderQuantityTv.setText(String.format(Locale.ENGLISH, "Quantity: %s %s", newOrderList.get(position).getQuantity(), newOrderList.get(position).getUnit()));

        holder.orderDeliveryChargeTv.setText(String.format(Locale.ENGLISH, "Delivery Charge: Rs. %.2f", newOrderList.get(position).getDeliveryCharge()));
        holder.orderPriceTv.setText(String.format(Locale.ENGLISH, "Order Total: Rs. %.2f", newOrderList.get(position).getTotalPrice()));
        holder.totalAmountTv.setText(String.format(Locale.ENGLISH, "Total Amount: Rs. %.2f", newOrderList.get(position).getTotalPrice() + newOrderList.get(position).getDeliveryCharge()));

        holder.orderDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderId", newOrderList.get(position).getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return newOrderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderDateTv, orderProductNameTv, orderPriceTv, orderQuantityTv, orderStatusTv, orderDeliveryDateTv, orderDeliveryLocationTv, orderDeliveryChargeTv, totalAmountTv;
        Button orderDetailsButton;
        ImageView orderProductImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderDateTv = itemView.findViewById(R.id.order_date);
            orderProductNameTv = itemView.findViewById(R.id.order_product_name);
            orderPriceTv = itemView.findViewById(R.id.order_price);
            orderQuantityTv = itemView.findViewById(R.id.order_quantity);
            orderStatusTv = itemView.findViewById(R.id.order_status);
            orderDeliveryDateTv = itemView.findViewById(R.id.order_delivery_date);
            orderDeliveryLocationTv = itemView.findViewById(R.id.order_delivery_location);
            orderDeliveryChargeTv = itemView.findViewById(R.id.delivery_charge);
            totalAmountTv = itemView.findViewById(R.id.total_amount);

            orderDetailsButton = itemView.findViewById(R.id.order_details_button);

            orderProductImage = itemView.findViewById(R.id.order_product_image);
        }
    }
}
