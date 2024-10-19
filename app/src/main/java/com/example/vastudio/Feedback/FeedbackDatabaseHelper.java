package com.example.vastudio.Feedback;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class FeedbackDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "feedback_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FEEDBACK = "feedback";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_DATE = "date";

    public FeedbackDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_FEEDBACK + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CONTENT + " TEXT," +
                COLUMN_TYPE + " TEXT," +
                COLUMN_DATE + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        onCreate(db);
    }

    public long addFeedback(Feedback feedback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTENT, feedback.getDetails());
        values.put(COLUMN_TYPE, feedback.getType());
        values.put(COLUMN_DATE, feedback.getDetails());
        long id = db.insert(TABLE_FEEDBACK, null, values);
        db.close();
        return id;
    }
    @SuppressLint("Range")
    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FEEDBACK, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
               String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));

                Feedback feedback = new Feedback();
                feedback.setId(id);
                feedback.setDetails(content);
                feedback.setType(type);
                feedback.setTimestamp(date);

                feedbackList.add(feedback);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return feedbackList;
    }

    @SuppressLint("Range")
    public List<Feedback> getUserFeedback(String userType) {
        List<Feedback> userFeedbackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_TYPE + " = ?";
        String[] selectionArgs = {userType};
        Cursor cursor = db.query(TABLE_FEEDBACK, null, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
               String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
                String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));

                Feedback feedback = new Feedback();
                feedback.setId(id);
                feedback.setDetails(content);
                feedback.setType(type);
                feedback.setTimestamp(date);

                userFeedbackList.add(feedback);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return userFeedbackList;
    }
}
