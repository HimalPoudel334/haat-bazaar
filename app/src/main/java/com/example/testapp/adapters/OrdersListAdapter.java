package com.example.testapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.Order;

import java.util.List;

public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.ViewHolder> {

    private List<Order> newOrderList;
    private Context context;

    public OrdersListAdapter(List<Order> newOrderList, Context context) {
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
        holder.orderDateTv.setText(newOrderList.get(position).getOrderCreationDate());
        holder.orderProductNameTv.setText(newOrderList.get(position).getOrderItems().get(0).getProduct().getName());
        holder.orderPriceTv.setText(String.valueOf(newOrderList.get(position).getTotalPrice()));
        holder.orderQuantityTv.setText(String.valueOf(newOrderList.get(position).getTotalQuantity()));

        holder.orderStatusTv.setText(newOrderList.get(position).getDeliveryStatus());
        holder.orderStatusTv.setBackgroundColor(Color.parseColor(newOrderList.get(position).getOrderStatusBgColor()));

        holder.orderDeliveryDateTv.setText(newOrderList.get(position).getOrderFulfilledDate());
        holder.orderDeliveryLocationTv.setText(newOrderList.get(position).getDeliveryLocation());

        holder.OrderItemsButton.setOnClickListener(v -> {

        });

        holder.itemView.setOnClickListener(v -> Log.d("User Order", "onBindViewHolder: clicked" ));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderDateTv, orderProductNameTv, orderPriceTv, orderQuantityTv, orderStatusTv, orderDeliveryDateTv, orderDeliveryLocationTv;
        Button OrderItemsButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderDateTv = itemView.findViewById(R.id.order_date);
            orderProductNameTv = itemView.findViewById(R.id.order_product_name);
            orderPriceTv = itemView.findViewById(R.id.order_price);
            orderQuantityTv = itemView.findViewById(R.id.order_quantity);
            orderStatusTv = itemView.findViewById(R.id.order_status);
            orderDeliveryDateTv = itemView.findViewById(R.id.order_delivery_date);
            orderDeliveryLocationTv = itemView.findViewById(R.id.order_delivery_location);

            OrderItemsButton = itemView.findViewById(R.id.order_details_button);
        }
    }
}
