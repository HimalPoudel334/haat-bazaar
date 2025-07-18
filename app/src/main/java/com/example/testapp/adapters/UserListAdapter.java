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
import com.example.testapp.models.User;

import java.util.List;
import java.util.Locale;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private final List<User> users;
    private final Context context;
    public UserListAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        return new UserListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.usernameTv.setText(String.format(Locale.ENGLISH, "Name: %s", user.getFullName()));
        holder.emailTv.setText(String.format(Locale.ENGLISH, "Email: %s", user.getEmail()));
        holder.phoneNoTv.setText(String.format(Locale.ENGLISH, "Phone No: %s", user.getPhoneNumber()));
        holder.locationTv.setText(String.format(Locale.ENGLISH, "Location: %s", user.getLocation()));
        holder.userTypeTv.setText(String.format(Locale.ENGLISH, "User Type: %s", user.getUserType()));

        holder.detailsButton.setOnClickListener(v -> Toast.makeText(context, "Details button clicked", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView usernameTv, emailTv, phoneNoTv, locationTv, userTypeTv;
        private final Button detailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameTv = itemView.findViewById(R.id.user_name_tv);
            emailTv = itemView.findViewById(R.id.email_tv);
            phoneNoTv = itemView.findViewById(R.id.phone_no_tv);
            locationTv = itemView.findViewById(R.id.location_tv);
            userTypeTv = itemView.findViewById(R.id.user_type_tv);

            detailsButton = itemView.findViewById(R.id.details_button);

        }
    }
}
