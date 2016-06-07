package mg.yvan.truth.ui.view;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mg.yvan.truth.R;
import mg.yvan.truth.models.Book;

/**
 * Created by Yvan on 31/05/16.
 */
public class BookView extends LinearLayout {

    @Bind(R.id.tv_book)
    TextView mTvBook;

    public BookView(Context context) {
        this(context, null);
    }

    public BookView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_book, this);
        ButterKnife.bind(this);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void populate(Cursor cursor) {
        mTvBook.setText(cursor.getString(cursor.getColumnIndex(Book.NAME)));
    }

}
