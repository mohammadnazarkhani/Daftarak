package com.example.daftarak.ui.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.daftarak.R;


public class SearchToolbar extends LinearLayout {

    private EditText searchInput;
    private ImageButton btnSearchOptions, btnSortType, btnSortOrder;

    private OnSearchListener listener;

    public SearchToolbar(Context context) {
        super(context);
        init(context);
    }

    public SearchToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_search_toolbar, this, true);

        searchInput = findViewById(R.id.search_input);
        btnSearchOptions = findViewById(R.id.btn_search_options);
        btnSortType = findViewById(R.id.btn_sort_type);
        btnSortOrder = findViewById(R.id.btn_sort_order);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (listener != null) listener.onSearchTextChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                if (listener != null) listener.onSearchConfirmed(searchInput.getText().toString());
                return true;
            }
            return false;
        });

        btnSearchOptions.setOnClickListener(v -> {
            if (listener != null) listener.onSearchOptionsClick();
        });

        btnSortType.setOnClickListener(v -> {
            if (listener != null) listener.onSortTypeClick();
        });

        btnSortOrder.setOnClickListener(v -> {
            if (listener != null) listener.onSortOrderClick();
        });
    }

    public void setSearchListener(OnSearchListener listener) {
        this.listener = listener;
    }

    public void setSearchText(String text) {
        searchInput.setText(text);
    }

    public void clearSearchText() {
        searchInput.setText("");
    }

    public interface OnSearchListener {
        void onSearchTextChanged(String text);

        void onSearchConfirmed(String query);

        void onSearchOptionsClick();

        void onSortTypeClick();

        void onSortOrderClick();
    }
}
