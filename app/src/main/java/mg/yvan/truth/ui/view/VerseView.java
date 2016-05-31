package mg.yvan.truth.ui.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mg.yvan.truth.R;

/**
 * Created by Yvan on 31/05/16.
 */
public class VerseView extends CardView {

    @Bind(R.id.tv_verse)
    TextView mTvVerse;
    @Bind(R.id.tv_num)
    TextView mTvNum;

    public VerseView(Context context) {
        this(context, null);
    }

    public VerseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_verse, this);
        ButterKnife.bind(this);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setRadius(getResources().getDimensionPixelSize(R.dimen.card_radius));
        setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));
        setPreventCornerOverlap(false);
        setUseCompatPadding(true);
        setClipChildren(false);
        setClipToPadding(false);
    }

    public void populate(int number, String text) {
        mTvVerse.setText(text);
        mTvNum.setText(String.valueOf(number));
    }

}
