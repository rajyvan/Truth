package mg.yvan.truth.ui.view;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnFavoriteRemovedEvent;
import mg.yvan.truth.event.OnReferenceDetailEvent;
import mg.yvan.truth.models.Reference;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.models.database.DataBook;
import mg.yvan.truth.models.database.DataVerse;
import mg.yvan.truth.models.database.RealmHelper;

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
    @Bind(R.id.tv_nb_comment)
    TextView mTvNbComment;

    private Reference mReference;

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

        mBtnFavorite.setImageResource(verse.isFavorite() ? R.drawable.like_on : R.drawable.like_off);

        mBtnShare.setOnClickListener(v -> {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle("Truth")
                    .setContentDescription(verse.getText())
                    .build();
            ShareDialog shareDialog = new ShareDialog((Activity) getContext());
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        });

        mBtnFavorite.setOnClickListener(v -> {
            if (verse.isFavorite()) {
                mBtnFavorite.setImageResource(R.drawable.like_off);
                verse.removeFromFavorite();
            } else {
                mBtnFavorite.setImageResource(R.drawable.like_on);
                verse.addToFavorite();
            }
        });

        final Verse localVerse = Verse.from(verse);
        mReference = RealmHelper.getInstance().getRealmForMainThread().where(Reference.class)
                .equalTo(Reference.BOOK_ID, localVerse.getBookId())
                .equalTo(Reference.CHAPTER, localVerse.getChapter())
                .equalTo(Reference.START_VERSE, localVerse.getVerse())
                .equalTo(Reference.END_VERSE, localVerse.getVerse())
                .findFirst();

        if (mReference == null) {
            mReference = new Reference();
            mReference.setBookId(localVerse.getBookId());
            mReference.setChapter(localVerse.getChapter());
            mReference.setStartVerse(localVerse.getVerse());
            mReference.setEndVerse(localVerse.getVerse());
            String selection = DataBook.BOOK_REF_ID + "=" + localVerse.getBookId();
            Cursor cursor = getContext().getContentResolver().query(DataBook.CONTENT_URI, DataBook.PROJECTION_ALL, selection, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final DataBook book = new DataBook().fromCursor(cursor);
                mReference.setBookName(book.getName());
            }
        }

        mBtnComment.setOnClickListener(v -> {
            EventBus.getDefault().post(new OnReferenceDetailEvent(mReference, this));
        });

        mTvNbComment.setText("");
        if (mReference.getComments() != null) {
            int nbComment = mReference.getComments().size();
            mTvNbComment.setText(nbComment > 0 ? String.valueOf(nbComment) : "");
        }
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

    public void populate(Verse verse) {
        mTvNum.setVisibility(GONE);
        mTvRef.setVisibility(VISIBLE);
        mTvVerse.setText(verse.getText());
        mTvRef.setText(verse.formatReference(getContext()));
        mBtnFavorite.setImageResource(R.drawable.like_on);
        mBtnFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OnFavoriteRemovedEvent(verse));
            }
        });

        mBtnShare.setOnClickListener(v -> {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle("Truth")
                    .setContentDescription(verse.getText())
                    .build();
            ShareDialog shareDialog = new ShareDialog((Activity) getContext());
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        });

        mReference = RealmHelper.getInstance().getRealmForMainThread().where(Reference.class)
                .equalTo(Reference.BOOK_ID, verse.getBookId())
                .equalTo(Reference.CHAPTER, verse.getChapter())
                .equalTo(Reference.START_VERSE, verse.getVerse())
                .equalTo(Reference.END_VERSE, verse.getVerse())
                .findFirst();

        if (mReference == null) {
            mReference = new Reference();
            mReference.setBookId(verse.getBookId());
            mReference.setChapter(verse.getChapter());
            mReference.setStartVerse(verse.getVerse());
            mReference.setEndVerse(verse.getVerse());
            String selection = DataBook.BOOK_REF_ID + "=" + verse.getBookId();
            Cursor cursor = getContext().getContentResolver().query(DataBook.CONTENT_URI, DataBook.PROJECTION_ALL, selection, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final DataBook book = new DataBook().fromCursor(cursor);
                mReference.setBookName(book.getName());
            }
        }

        mBtnComment.setOnClickListener(v -> {
            EventBus.getDefault().post(new OnReferenceDetailEvent(mReference, this));
        });

        mTvNbComment.setText("");
        if (mReference.getComments() != null) {
            int nbComment = mReference.getComments().size();
            mTvNbComment.setText(nbComment > 0 ? String.valueOf(nbComment) : "");
        }
    }

}
