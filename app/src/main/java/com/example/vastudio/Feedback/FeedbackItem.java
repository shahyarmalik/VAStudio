package com.example.vastudio.Feedback;
import android.os.Parcel;
import android.os.Parcelable;

public class FeedbackItem implements Parcelable {
    private int id;
    private String customerName;
    private String email;
    private String content;
    private boolean resolved;

    public FeedbackItem(int id, String customerName, String email, String content, boolean resolved) {
        this.id = id;
        this.customerName = customerName;
        this.email = email;
        this.content = content;
        this.resolved = resolved;
    }

    protected FeedbackItem(Parcel in) {
        id = in.readInt();
        customerName = in.readString();
        email = in.readString();
        content = in.readString();
        resolved = in.readByte() != 0;
    }

    public static final Creator<FeedbackItem> CREATOR = new Creator<FeedbackItem>() {
        @Override
        public FeedbackItem createFromParcel(Parcel in) {
            return new FeedbackItem(in);
        }

        @Override
        public FeedbackItem[] newArray(int size) {
            return new FeedbackItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(customerName);
        dest.writeString(email);
        dest.writeString(content);
        dest.writeByte((byte) (resolved ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
