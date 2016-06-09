package mg.yvan.truth.ui.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mg.yvan.truth.R;
import mg.yvan.truth.models.database.DataVerse;

/**
 * Created by Yvan on 31/05/16.
 */
public class VerseView extends CardView {

    @Bind(R.id.tv_verse)
    TextView mTvVerse;
    @Bind(R.id.tv_num)
    TextView mTvNum;
    @Bind(R.id.btn_share)
    ImageButton mBtnShare;
    @Bind(R.id.btn_favorite)
    ImageButton mBtnFavorite;
    @Bind(R.id.btn_comment)
    ImageButton mBtnComment;
    @Bind(R.id.tv_ref)
    TextView mTvRef;

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

    public void populate(DataVerse verse) {
        populate(verse, null);
    }

    public void populate(DataVerse verse, String key) {
        if (TextUtils.isEmpty(key)) {
            mTvVerse.setText(verse.getText());
            mTvNum.setVisibility(VISIBLE);
            mTvRef.setVisibility(GONE);
        } else {
            mTvVerse.setText(highLight(getContext(), verse.getText(), verse.getNormalizedText(), key));
            mTvNum.setVisibility(GONE);
            mTvRef.setVisibility(VISIBLE);
            mTvRef.setText(verse.formatReference(getContext()));
        }
        mTvNum.setText(String.valueOf(verse.getVerse()));
    }

    private SpannableString highLight(Context context, String text, String normalizedText, String searchKey) {
        if (normalizedText == null || !normalizedText.toLowerCase().contains(searchKey)) {
            return new SpannableString(text);
        }

        SpannableString formatted = new SpannableString(text);
        int start = normalizedText.toLowerCase().indexOf(searchKey);
        int end = start + searchKey.length();

        formatted.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.orange)), start, end, 0);
        int i = normalizedText.toLowerCase().indexOf(searchKey, end);
        while (i > 0) {
            start = i;
            end = i + searchKey.length();
            formatted.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.orange)), start, end, 0);
            i = normalizedText.toLowerCase().indexOf(searchKey, end);
        }
        return formatted;
    }

}
