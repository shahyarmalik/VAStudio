package com.example.vastudio.Feedback;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.vastudio.R;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private Context context;
    private List<Feedback> feedbackList;
    private OnFeedbackItemClickListener listener;

    public FeedbackAdapter(List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    public void setOnFeedbackItemClickListener(OnFeedbackItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedbackItem = feedbackList.get(position);
        holder.bind(feedbackItem);
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public interface OnFeedbackItemClickListener {
        void onFeedbackItemClick(int position);
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewCustomerName;
        private TextView textViewFeedbackContent;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerName = itemView.findViewById(R.id.textViewCustomerName);
            textViewFeedbackContent = itemView.findViewById(R.id.textViewFeedbackContent);
            itemView.setOnClickListener(this);
        }

        public void bind(Feedback feedbackItem) {
            textViewCustomerName.setText(feedbackItem.getType());
            textViewFeedbackContent.setText(feedbackItem.getDetails());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onFeedbackItemClick(position);
                }
            }
        }
    }
}
