package com.example.daftarak.ui.fragment.dialog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daftarak.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateNotebookDialogFragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_notebook_dialog, container, false);
    }
}