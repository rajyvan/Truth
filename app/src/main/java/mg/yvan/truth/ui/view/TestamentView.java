package mg.yvan.truth.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mg.yvan.truth.Defines;
import mg.yvan.truth.R;

/**
 * Created by Yvan on 06/06/16.
 */
public class TestamentView extends RelativeLayout {

    @Bind(R.id.tv_label)
    TextView mTvLabel;
    @Bind(R.id.tv_number)
    TextView mTvNumber;

    private boolean mIsOld;

    public TestamentView(Context context) {
        this(context, null);
    }

    public TestamentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestamentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // load the styled attributes and set their properties
        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.TestamentView, defStyleAttr, 0);

        mIsOld = attributes.getBoolean(R.styleable.TestamentView_is_old, true);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_testament, this);
        ButterKnife.bind(this);

        mTvLabel.setText(mIsOld ? R.string.old_testament : R.string.new_testament);
        mTvNumber.setText(String.format(getResources().getString(R.string.nb_book), mIsOld ? Defines.OLD_TESTAMENT : Defines.NEW_TESTAMENT));
    }

}
