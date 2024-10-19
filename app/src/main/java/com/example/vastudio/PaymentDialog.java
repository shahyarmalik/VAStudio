package com.example.vastudio;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PaymentDialog extends Dialog {
    private EditText editTextCardNumber, editTextExpiryDate, editTextCVV;
    private Button buttonSubmitPayment;
    private TextView textViewPrice;
    String price = "", bidid = "";

    Context context;

    public PaymentDialog(Context context, String price, String bidid) {
        super(context);
        this.price = price;
        this.bidid = bidid;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_dialog_layout);

        // Initialize views
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        editTextCVV = findViewById(R.id.editTextCVV);
        buttonSubmitPayment = findViewById(R.id.buttonSubmitPayment);

        // Set dummy card information for testing
        editTextCardNumber.setText("1234 5678 9012 3456");
        editTextExpiryDate.setText("12/25");
        editTextCVV.setText("123");
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewPrice.setText("Price: " + price);
        // Handle button click
        buttonSubmitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Payment Billing Client library")
                        .setMessage("Are you sure you want to Submit Payment?")
                        .setPositiveButton("Payout", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bids").child(bidid);
                                //  Log.e("id",post.getBidId());
                                // Update the status field of the bid to "confirm"
                                HashMap<String, Object> updateMap = new HashMap<>();
                                updateMap.put("status", "pay");
                                databaseReference.updateChildren(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                dialog.dismiss();
                                dismiss();
                            }


                        })
                        .setNegativeButton("Cancel", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        })
                        .show();
                // Handle payment submission
                //    dismiss();
            }
        });
    }
}

