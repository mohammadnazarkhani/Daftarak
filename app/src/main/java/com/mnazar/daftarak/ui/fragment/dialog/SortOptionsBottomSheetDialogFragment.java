package com.mnazar.daftarak.ui.fragment.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mnazar.daftarak.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SortOptionsBottomSheetDialogFragment extends BottomSheetDialogFragment {
    public static final String REQUEST_KEY_SORT_OPTIONS = "request_key_sort_options";
    public static final String RESULT_KEY_SORT_BY = "result_sort_by";
    public static final String RESULT_KEY_ASCENDING = "result_sort_ascending";

    private static final String ARG_SORT_BY = "arg_sort_by";
    private static final String ARG_ASCENDING = "arg_ascending";

    private RadioGroup rgSortBy, rgSortOrder;

    public static SortOptionsBottomSheetDialogFragment newInstance(String sortBy, boolean ascending) {
        SortOptionsBottomSheetDialogFragment fragment = new SortOptionsBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SORT_BY, sortBy);
        args.putBoolean(ARG_ASCENDING, ascending);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sort_options_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rgSortBy = view.findViewById(R.id.rg_sort_by);
        rgSortOrder = view.findViewById(R.id.rg_sort_order);

        Bundle args = getArguments();
        String sortBy = args != null ? args.getString(ARG_SORT_BY, "date") : "date";
        boolean ascending = args != null && args.getBoolean(ARG_ASCENDING, true);

        if ("title".equals(sortBy)) {
            rgSortBy.check(R.id.rb_sort_title);
        } else {
            rgSortBy.check(R.id.rb_sort_date);
        }

        rgSortOrder.check(ascending ? R.id.rb_ascending : R.id.rb_descending);

        view.findViewById(R.id.btn_apply_sort).setOnClickListener(v -> {
            String selectedSortBy = (rgSortBy.getCheckedRadioButtonId() == R.id.rb_sort_title) ? "title" : "date";
            boolean isAscending = rgSortOrder.getCheckedRadioButtonId() != R.id.rb_descending;

            Bundle result = new Bundle();
            result.putString(RESULT_KEY_SORT_BY, selectedSortBy);
            result.putBoolean(RESULT_KEY_ASCENDING, isAscending);

            getParentFragmentManager().setFragmentResult(REQUEST_KEY_SORT_OPTIONS, result);

            dismiss();
        });
    }
}
