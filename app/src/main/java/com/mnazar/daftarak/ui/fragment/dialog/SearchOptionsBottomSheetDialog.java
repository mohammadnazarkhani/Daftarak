package com.mnazar.daftarak.ui.fragment.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mnazar.daftarak.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SearchOptionsBottomSheetDialog extends BottomSheetDialogFragment {

    private static final String ARG_SEARCH_TITLE = "arg_search_title";
    private static final String ARG_SEARCH_BODY = "arg_search_body";

    public static final String RESULT_KEY = "search_options_result";
    public static final String RESULT_TITLE = "search_title";
    public static final String RESULT_BODY = "search_body";

    private CheckBox checkboxTitle;
    private CheckBox checkboxBody;

    public SearchOptionsBottomSheetDialog() {
        // Required empty public constructor
    }

    public static SearchOptionsBottomSheetDialog newInstance(boolean searchTitle, boolean searchBody) {
        SearchOptionsBottomSheetDialog fragment = new SearchOptionsBottomSheetDialog();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SEARCH_TITLE, searchTitle);
        args.putBoolean(ARG_SEARCH_BODY, searchBody);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_options_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkboxTitle = view.findViewById(R.id.checkbox_title);
        checkboxBody = view.findViewById(R.id.checkbox_body);

        // Load initial arguments
        Bundle args = getArguments();
        boolean searchTitle = args != null && args.getBoolean(ARG_SEARCH_TITLE, true);
        boolean searchBody = args != null && args.getBoolean(ARG_SEARCH_BODY, true);

        checkboxTitle.setChecked(searchTitle);
        checkboxBody.setChecked(searchBody);

        // Apply button click
        view.findViewById(R.id.btn_apply).setOnClickListener(v -> {
            boolean resultTitle = checkboxTitle.isChecked();
            boolean resultBody = checkboxBody.isChecked();

            Bundle result = new Bundle();
            result.putBoolean(RESULT_TITLE, resultTitle);
            result.putBoolean(RESULT_BODY, resultBody);

            getParentFragmentManager().setFragmentResult(RESULT_KEY, result);

            dismiss();
        });
    }
}
