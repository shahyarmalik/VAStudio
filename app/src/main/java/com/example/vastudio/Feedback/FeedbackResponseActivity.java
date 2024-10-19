package com.example.vastudio.Feedback;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vastudio.R;


public class FeedbackResponseActivity extends AppCompatActivity {

    private EditText editTextResponseMessage;
    private Button buttonSendResponse;
    private FeedbackItem selectedFeedback; // Assume FeedbackItem is a model class for feedback items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_response);

        // Initialize views
        editTextResponseMessage = findViewById(R.id.editTextResponseMessage);
        buttonSendResponse = findViewById(R.id.buttonSendResponse);

        // Get selected feedback item from intent or other data source
        selectedFeedback = getIntent().getParcelableExtra("selectedFeedback");

        // Display feedback details in EditText (or TextView)
        if (selectedFeedback != null) {
            String feedbackContent = selectedFeedback.getContent(); // Example: Get feedback content
            editTextResponseMessage.setText("Your feedback: " + feedbackContent);
        }

        // Set click listener for the send response button
        buttonSendResponse.setOnClickListener(v -> {
            String responseMessage = editTextResponseMessage.getText().toString().trim();
            if (!responseMessage.isEmpty()) {
                // Code to send response message to customer (e.g., using APIs, database updates)
                FeedbackDatabaseHelper databaseHelper=new FeedbackDatabaseHelper(this);
         //       databaseHelper.sendResponseToCustomer(selectedFeedback, responseMessage);
            } else {
                // Show error message or prompt user to enter a response message
                Toast.makeText(FeedbackResponseActivity.this, "Please enter a response message", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
