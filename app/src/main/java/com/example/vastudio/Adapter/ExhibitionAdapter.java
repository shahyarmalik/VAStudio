package com.example.vastudio.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vastudio.Model.Exhibition;
import com.example.vastudio.R;

import java.util.List;

public class ExhibitionAdapter extends RecyclerView.Adapter<ExhibitionAdapter.ExhibitionViewHolder> {
    private List<Exhibition> exhibitionList;
    private Context context;

    public ExhibitionAdapter(List<Exhibition> exhibitionList,Context context) {
        this.context = context;
        this.exhibitionList = exhibitionList;
    }

    @NonNull
    @Override
    public ExhibitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_exhibition, parent, false);
        return new ExhibitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExhibitionViewHolder holder, int position) {
        Exhibition exhibition = exhibitionList.get(position);
        holder.bind(exhibition);
    }

    @Override
    public int getItemCount() {
        return exhibitionList.size();
    }

    public class ExhibitionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTextView;
        private TextView dateTextView;
        private TextView locationTextView;

        public ExhibitionViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.exhibitionNameTextView);
            dateTextView = itemView.findViewById(R.id.exhibitionDateTextView);
            locationTextView = itemView.findViewById(R.id.exhibitionLocationTextView);
            itemView.setOnClickListener(this);
        }

        public void bind(Exhibition exhibition) {
            nameTextView.setText(exhibition.getName());
            dateTextView.setText(exhibition.getDate());
            locationTextView.setText(exhibition.getArtist());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Exhibition exhibition = exhibitionList.get(position);
                // Navigate to ArtworkDetailsFragment with exhibition ID or data
                // Example: Replace current fragment with ArtworkDetailsFragment.newInstance(exhibition.getId())
            }
        }
    }
}

