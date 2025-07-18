package com.example.testapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.PaymentDetailActivity;
import com.example.testapp.R;
import com.example.testapp.models.Payment;

import java.util.List;
import java.util.Locale;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.ViewHolder> {

    private final List<Payment> payments;
    private final Context context;
    public PaymentListAdapter(Context context, List<Payment> payments) {
        this.payments = payments;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_list_item, parent, false);
        return new PaymentListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentListAdapter.ViewHolder holder, int position) {
        Payment payment = payments.get(position);
        holder.orderId.setText(String.format(Locale.ENGLISH, "Order Id: %s", payment.getOrderId()));
        holder.customerName.setText(String.format(Locale.ENGLISH, "Customer Name: %s", payment.getUserId()));
        holder.paymentStatus.setText(String.format(Locale.ENGLISH, "Payment Status: %s", payment.getStatus()));
        holder.paymentAmount.setText(String.format(Locale.ENGLISH, "Payment Amount: %.2f", payment.getAmount()));
        holder.paymentMethod.setText(String.format(Locale.ENGLISH, "Payment Method: %s", payment.getPaymentMethod()));

        holder.detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, PaymentDetailActivity.class);
            intent.putExtra("payment", payment);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView orderId, customerName, paymentStatus, paymentAmount, paymentMethod;
        private final Button detailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.order_id_tv);
            customerName = itemView.findViewById(R.id.customer_name_tv);
            paymentStatus = itemView.findViewById(R.id.payment_status_tv);
            paymentAmount = itemView.findViewById(R.id.payment_amount_tv);
            paymentMethod = itemView.findViewById(R.id.payment_method_tv);
            detailsButton = itemView.findViewById(R.id.details_button);

        }
    }
}
