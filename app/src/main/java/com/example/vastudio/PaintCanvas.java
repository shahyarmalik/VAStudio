package com.example.vastudio;

import static android.graphics.Color.BLACK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class PaintCanvas extends AppCompatActivity {
    private ImageView canvas;
    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvasBitmap;
    private Button brush_btn, Eraser_btn, post_btn;
    private ImageView Color_btn;
    private SeekBar PenSize;
    private ColorPickerView colorPickerView;
    private int currentColor = BLACK;

    private int currentColorIndex = 0;
    private boolean colorPickerVisible = false;
    String artistName,art_title,des,price;

    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_canvas);

        colorPickerView = findViewById(R.id.colorPickerView1);
        colorPickerView.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();
        artistName=intent.getStringExtra("artist");
        art_title=intent.getStringExtra("title");
        des=intent.getStringExtra("description");
        price=intent.getStringExtra("price");

        canvas = findViewById(R.id.canvas);
        Button clearButton = findViewById(R.id.clear_btn);
        PenSize = findViewById(R.id.pensize);
        post_btn = findViewById(R.id.post);
        FirebaseApp.initializeApp(this);
        Eraser_btn = findViewById(R.id.erazer_btn); // Change Button to ImageButton
        Color_btn = findViewById(R.id.color_btn);
        brush_btn = findViewById(R.id.brush_btn); // Change Button to ImageButton
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);

        Eraser_btn.setOnClickListener(v -> {
            int previousColor = Color.BLACK;
            if (paint.getColor()!= Color.WHITE) {
                previousColor = paint.getColor();
                paint.setColor(Color.WHITE);
            } else {
                paint.setColor(previousColor);
            }
        });
        PenSize.setMax(50);

        PenSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int strokeWidth = Math.max(1, progress);
                paint.setStrokeWidth(strokeWidth);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        clearButton.setOnClickListener(v -> {
            canvasBitmap.drawColor(Color.WHITE);
            canvas.invalidate();
        });

        brush_btn.setOnClickListener(v -> {
            if (paint.getXfermode() == null) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            } else {
                paint.setXfermode(null);
            }
        });

        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                // Handle the selected color
                int selectedColor = envelope.getColor();
                updateUIWithSelectedColor(selectedColor);
            }
        });
        Color_btn.setOnClickListener(v -> {

            if (colorPickerVisible) {
                colorPickerView.setVisibility(View.GONE);
            } else {
                colorPickerView.setVisibility(View.VISIBLE);
            }
            colorPickerVisible = !colorPickerVisible;
//            ColorPickerDialogFragment colorPickerDialogFragment = new ColorPickerDialogFragment();
//            colorPickerDialogFragment.setOnColorSelectedListener(selectedColor -> {
//                // Handle the selected color
//                updateUIWithSelectedColor(selectedColor);
//
//                //   canvasBitmap.drawColor(selectedColor);
//
//            });



      //      colorPickerDialogFragment.show(getChildFragmentManager(), "color_picker_dialog");

        });


        canvas.setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();

                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (paint.getXfermode() == null) { // Add this condition
                            drawLine(canvasBitmap, paint, startX, startY, event.getX(), event.getY());
                            startX = event.getX();
                            startY = event.getY();
                            canvas.invalidate();
                        }
                        break;
                }
                return true;
            }
        });
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();

            }
        });
    }

    private void updateUIWithSelectedColor(int selectedColor) {
        paint.setColor(selectedColor);
    }


    private void upload() {
        bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasBitmap = new Canvas(bitmap);
        canvas.draw(canvasBitmap);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        // Get a reference to the storage service
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file you want to upload
        StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".png");

        // Upload the file
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Get the uploaded image URL
                    String imageUrl = task.getResult().toString();

                    // Create a new Post in the Firebase Realtime Database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference postsRef = database.getReference("posts");

                    String postId = postsRef.push().getKey();
                    String publisherUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    HashMap<String, Object> postMap = new HashMap<>();
                    postMap.put("postId", postId);
                    postMap.put("publisherUid", publisherUid);
                    postMap.put("imageUrl", imageUrl);
                    postMap.put("title", art_title);
                    postMap.put("artistName", artistName);
                    postMap.put("description", des);
                    postMap.put("price", price);
                    postMap.put("bid", "0");

                    postsRef.child(postId).setValue(postMap);

                    try {
                        sendCanvasPostNotification();
                    }
                    catch (Exception e){
                        Log.e("notification",e.getMessage());
                    }


                    Toast.makeText(PaintCanvas.this, "Post created successfully", Toast.LENGTH_SHORT).show();

                } else {
                    // Handle failed upload
                    Toast.makeText(PaintCanvas.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void sendCanvasPostNotification() {
        // Construct the notification message
        Map<String, String> data = new HashMap<>();
        data.put("title", "New Canvas Posted");
        data.put("body", "Check out the latest canvas!");

        // Send notification to the "new_canvas_posts" topic
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder("new_canvas_posts")
                .setData(data)
                .build());
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Width and height must be greater than 0");
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            canvasBitmap = new Canvas(bitmap);
            canvas.setImageBitmap(bitmap);
        }
    }

    private void drawLine(Canvas canvas, Paint paint, float startX, float startY, float endX, float endY) {
        canvas.drawLine(startX, startY, endX, endY, paint);
    }
}