package com.example.vastudio.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.vastudio.R;
import com.skydoves.colorpickerview.ColorPickerView;

public class ColorPickerDialogFragment extends DialogFragment {
    private ColorPickerView colorPickerView;
    private OnColorSelectedListener colorSelectedListener;

    public interface OnColorSelectedListener {
        void onColorSelected(int color);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_color_picker_dialog, null);
        colorPickerView = view.findViewById(R.id.colorPickerView);

        builder.setView(view)
                .setTitle("Select Color")
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedColor = colorPickerView.getColor();
                    if (colorSelectedListener != null) {
                        colorSelectedListener.onColorSelected(selectedColor);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        colorSelectedListener = listener;
    }
}
