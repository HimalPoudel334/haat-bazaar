package com.example.testapp.adapters;

import android.content.Context;
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

public class UserOrdersAdapter extends RecyclerView.Adapter<UserOrdersAdapter.ViewHolder> {

    private Context context;
    private List<Order> orderList;

    public UserOrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public UserOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_rv, parent, false);
        return new UserOrdersAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrdersAdapter.ViewHolder holder, int position) {
        holder.orderDateTv.setText(orderList.get(position).getOrderCreationDate());
        holder.orderProductNameTv.setText(orderList.get(position).getOrderDetails().get(0).getProduct().getName());
        holder.orderPriceTv.setText(String.valueOf(orderList.get(position).getTotalPrice()));
        holder.orderQuantityTv.setText(String.valueOf(orderList.get(position).getTotalQuantity()));
        holder.orderStatusTv.setText(orderList.get(position).getDeliveryStatus());
        holder.orderDeliveryDateTv.setText(orderList.get(position).getOrderFulfilledDate());
        holder.orderDeliveryLocationTv.setText(orderList.get(position).getDeliveryLocation());

        holder.orderCancelButton.setOnClickListener(v -> {

        });

        holder.orderDetailsButton.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderDateTv, orderProductNameTv, orderPriceTv, orderQuantityTv, orderStatusTv, orderDeliveryDateTv, orderDeliveryLocationTv;
        Button orderCancelButton, orderDetailsButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderDateTv = itemView.findViewById(R.id.order_date);
            orderProductNameTv = itemView.findViewById(R.id.order_product_name);
            orderPriceTv = itemView.findViewById(R.id.order_price);
            orderQuantityTv = itemView.findViewById(R.id.order_quantity);
            orderStatusTv = itemView.findViewById(R.id.order_status);
            orderDeliveryDateTv = itemView.findViewById(R.id.order_delivery_date);
            orderDeliveryLocationTv = itemView.findViewById(R.id.order_delivery_location);

            orderCancelButton = itemView.findViewById(R.id.order_cancel_button);
            orderDetailsButton = itemView.findViewById(R.id.order_details_button);
        }
    }
}
