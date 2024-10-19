package com.example.vastudio.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vastudio.PaintCanvas;
import com.example.vastudio.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Objects;

public class PaintFragment extends Fragment {
    private ImageView canvas;
    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvasBitmap;
    private Button clearButton, postButton;
    private ImageView  colorButton;

    Button eraserButton,brushButton;
    private SeekBar penSize;
    private int currentColor = Color.BLACK;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.activity_paint_canvas, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        canvas = view.findViewById(R.id.canvas);
        clearButton = view.findViewById(R.id.clear_btn);
        penSize = view.findViewById(R.id.pensize);
        postButton = view.findViewById(R.id.post);
        eraserButton = view.findViewById(R.id.erazer_btn);
        colorButton = view.findViewById(R.id.color_btn);
        brushButton = view.findViewById(R.id.brush_btn);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setColor(currentColor);
        // Set listeners and actions
        setListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        clearButton.setOnClickListener(v -> {
            canvasBitmap.drawColor(Color.WHITE);
            canvas.invalidate();
        });

        eraserButton.setOnClickListener(v -> {
            int previousColor = Color.BLACK;
            if (paint.getColor() != Color.WHITE) {
                previousColor = paint.getColor();
                paint.setColor(Color.WHITE);
            } else {
                paint.setColor(previousColor);
            }
        });

        penSize.setMax(50);
        penSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int strokeWidth = Math.max(1, progress);
                paint.setStrokeWidth(strokeWidth);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        brushButton.setOnClickListener(v -> {
            if (paint.getXfermode() == null) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            } else {
                paint.setXfermode(null);
            }
        });

        colorButton.setOnClickListener(v -> {
            ColorPickerDialogFragment colorPickerDialogFragment = new ColorPickerDialogFragment();
            colorPickerDialogFragment.setOnColorSelectedListener(selectedColor -> {
                // Handle the selected color
                updateUIWithSelectedColor(selectedColor);
             //   canvasBitmap.drawColor(selectedColor);

            });
            colorPickerDialogFragment.show(getChildFragmentManager(), "color_picker_dialog");

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
                        if (paint.getXfermode() == null) {
                            bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                            canvasBitmap = new Canvas(bitmap);
                            if (canvasBitmap!=null){
                                drawLine(canvasBitmap, paint, startX, startY,event.getX() , event.getY());
                            }
                            else{
                                Toast.makeText(getContext(), "null canva", Toast.LENGTH_SHORT).show();
                            }
                            startX = event.getX();
                            startY = event.getY();
                            canvas.invalidate();
                        }
                        break;
                }
                return true;
            }
        });

        postButton.setOnClickListener(v -> upload());
    }

    private void updateUIWithSelectedColor(int selectedColor) {
        // Update UI elements or perform actions based on the selected color
        // For example, change the color of a button or view
        colorButton.setBackgroundColor(selectedColor);
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
                    postMap.put("imageUrl", imageUrl);
                    postMap.put("publisherUid", publisherUid);

                    postsRef.child(postId).setValue(postMap);

                    Toast.makeText(getContext(), "Post created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failed upload
                    Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void drawLine(Canvas canvas, Paint paint, float startX, float startY, float endX, float endY) {
        canvas.drawLine(startX, startY, endX, endY, paint);
    }
}

