package com.example.vastudio.Feedback;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vastudio.R;

import java.util.List;

public class FeedbackListFragment extends Fragment {

    private Spinner spinnerCategoryFilter, spinnerPriorityFilter;
    private RecyclerView recyclerViewFeedbackList;
    private FeedbackAdapter feedbackAdapter;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_feedback_list, container, false);

        // Initialize views
//        spinnerCategoryFilter = view.findViewById(R.id.spinnerCategoryFilter);
//        spinnerPriorityFilter = view.findViewById(R.id.spinnerPriorityFilter);
        recyclerViewFeedbackList = view.findViewById(R.id.recyclerViewFeedbackList);

        // Initialize RecyclerView and adapter
        recyclerViewFeedbackList.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedbackAdapter = new FeedbackAdapter(getFeedbackList());
        recyclerViewFeedbackList.setAdapter(feedbackAdapter);

        // Set up category and priority filters (populate spinners as needed)
        setupCategoryFilter();
        setupPriorityFilter();

        return view;
    }

    private void setupCategoryFilter() {
        // Implement category filter setup here
    }

    private void setupPriorityFilter() {
        // Implement priority filter setup here
    }

    private List<Feedback> getFeedbackList() {
        FeedbackDatabaseHelper feedbackDatabaseHelper = new FeedbackDatabaseHelper(getActivity());
        return feedbackDatabaseHelper.getAllFeedback();
    }
}
