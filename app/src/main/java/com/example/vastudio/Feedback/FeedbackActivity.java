package com.example.vastudio.Feedback;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import com.example.vastudio.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class FeedbackActivity extends AppCompatActivity {

    private RadioGroup radioGroupFeedbackType;
    private EditText editTextFeedbackText;
    private Button buttonSubmitFeedback;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize views
        radioGroupFeedbackType = findViewById(R.id.radioGroupFeedbackType);
        editTextFeedbackText = findViewById(R.id.editTextFeedbackText);
        buttonSubmitFeedback = findViewById(R.id.buttonSubmitFeedback);

        // Set click listener for submit button
        buttonSubmitFeedback.setOnClickListener(v -> {
            // Get selected feedback type
            int selectedFeedbackId = radioGroupFeedbackType.getCheckedRadioButtonId();
            RadioButton selectedFeedbackButton = findViewById(selectedFeedbackId);
            String feedbackType = selectedFeedbackButton.getText().toString();

            // Get feedback text
            String feedbackText = editTextFeedbackText.getText().toString().trim();

            // Validate feedback text
            if (feedbackText.isEmpty()) {
                Toast.makeText(this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
                return;
            }

            // Process the feedback (e.g., send it to a database or API)
            FeedbackDatabaseHelper feedbackDatabaseHelper=new FeedbackDatabaseHelper(this);
            String id= UUID.randomUUID().toString();
            // Get current timestamp in milliseconds
            long timestamp = System.currentTimeMillis();
            // Convert milliseconds to a formatted date/time string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String date1=sdf.format(new Date(timestamp));

            Feedback feedback=new Feedback(id,feedbackType,feedbackText,date1);

            Long res=feedbackDatabaseHelper.addFeedback(feedback);
            if (res!=-1) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processFeedback(String feedbackType, String feedbackText) {
        // Placeholder method for processing feedback
        // You can implement logic to handle the feedback submission (e.g., send to server)
        // For demonstration purposes, display a toast message


        Toast.makeText(this, "Feedback Type: " + feedbackType + "\nFeedback Text: " + feedbackText, Toast.LENGTH_LONG).show();
    }
}
