package manoj.jek.go.com.contactsdemo.ui.ui.custom;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import manoj.jek.go.com.contactsdemo.R;

public class SearchBar extends RelativeLayout {

    private EditText _searchEditText;
    private ImageView _closeView;

    public SearchBar(Context context) {
        super(context);
        initView();
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void addTextListener(TextListener textListener) {
        _searchEditText.addTextChangedListener(textListener);
    }

    private void initView() {
        addSearchEditText();
        addCloseOption();
    }

    private void addSearchEditText() {
        _searchEditText = new EditText(getContext());
        _searchEditText.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(_searchEditText);
    }

    private void addCloseOption() {
        _closeView = new ImageView(getContext());
        RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        relLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        relLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int padding = getResources().getDimensionPixelSize(R.dimen.default_padding);
        _closeView.setPadding(padding, 0, padding, 0);
        _closeView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        _closeView.setLayoutParams(relLayoutParams);
        _closeView.setImageResource(R.drawable.ic_close_black_24dp);
        _closeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                _searchEditText.setText(null);
                setVisibility(GONE);
            }
        });
        addView(_closeView);
    }

}
