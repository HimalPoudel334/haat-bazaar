package com.example.testapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.Shipment;

import java.util.List;
import java.util.Locale;

public class ShipmentListAdapter extends RecyclerView.Adapter<ShipmentListAdapter.ViewHolder> {

    private List<Shipment> shipments;
    private final Context context;
    public ShipmentListAdapter(Context context, List<Shipment> shipments) {
        this.shipments = shipments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shipment_list_item, parent, false);
        return new ShipmentListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shipment shipment = shipments.get(position);
        holder.deliveryLocationTv.setText(String.format(Locale.ENGLISH, "Location: %s",shipment.getLocation()));
        holder.shpDateTv.setText(String.format(Locale.ENGLISH, "Ship Date: %s", shipment.getShipDate()));
        holder.deliveryAssignedTv.setText(String.format(Locale.ENGLISH, "Assigned to: %s", shipment.getAssignedUserName()));
        holder.statusTv.setText(String.format(Locale.ENGLISH, "Status: %s", shipment.getStatus()));

        holder.editButton.setOnClickListener(v -> {
            Toast.makeText(context,"Edit button clicked", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView deliveryLocationTv, shpDateTv, deliveryAssignedTv, statusTv;
        private Button editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deliveryLocationTv = itemView.findViewById(R.id.delivery_location_tv);
            shpDateTv = itemView.findViewById(R.id.ship_date_tv);
            deliveryAssignedTv = itemView.findViewById(R.id.delivery_assigned_tv);
            statusTv = itemView.findViewById(R.id.status_tv);
            editButton = itemView.findViewById(R.id.edit);
        }
    }
}
