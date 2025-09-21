package com.example.testapp.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.ProductRating;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductReviewListAdapter extends RecyclerView.Adapter<ProductReviewListAdapter.ViewHolder> {

    private final List<ProductRating> ratings;

    public ProductReviewListAdapter(List<ProductRating> ratings) {
        this.ratings = ratings;
    }

    @NonNull
    @Override
    public ProductReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_review_item, parent, false);
        return new ProductReviewListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductReviewListAdapter.ViewHolder holder, int position) {
        holder.initialsTv.setText(ratings.get(position).getInitials());
        holder.fullNameTv.setText(ratings.get(position).getUserName());

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        try {
            String timestamp = ratings.get(position).getCreatedAt();

            // Check if edited (updatedAt different from createdAt)
            if(ratings.get(position).getUpdatedAt() != null &&
                    !ratings.get(position).getUpdatedAt().equals(ratings.get(position).getCreatedAt())) {
                holder.editedTv.setVisibility(View.VISIBLE);
                holder.editedTv.setText(R.string.edited);
                timestamp = ratings.get(position).getUpdatedAt(); // Use updated time
            } else {
                holder.editedTv.setVisibility(View.GONE);
            }

            Date date = inputFormat.parse(timestamp);
            if (date != null) {
                String timeAgo = DateUtils.getRelativeTimeSpanString(
                        date.getTime(),
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS
                ).toString();
                holder.createdAtTv.setText(timeAgo);
            } else {
                holder.createdAtTv.setText(timestamp);
            }

        } catch (ParseException e) {
            holder.createdAtTv.setText(ratings.get(position).getCreatedAt());
        }
        holder.reviewTv.setText(ratings.get(position).getReview());
        holder.ratingBar.setRating((float) ratings.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView initialsTv, fullNameTv, createdAtTv, reviewTv, editedTv;
        private final RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initialsTv = itemView.findViewById(R.id.initials_tv);
            fullNameTv = itemView.findViewById(R.id.full_name_tv);
            createdAtTv = itemView.findViewById(R.id.created_at_tv);
            reviewTv = itemView.findViewById(R.id.review_tv);
            editedTv = itemView.findViewById(R.id.edited_tv);
            ratingBar = itemView.findViewById(R.id.individual_rating);
        }
    }
}
