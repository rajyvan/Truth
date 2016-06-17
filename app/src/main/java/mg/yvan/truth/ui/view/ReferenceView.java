package mg.yvan.truth.ui.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnReferenceDeletedEvent;
import mg.yvan.truth.event.OnReferenceDetailEvent;
import mg.yvan.truth.models.Comment;
import mg.yvan.truth.models.Reference;

/**
 * Created by Yvan on 31/05/16.
 */
public class ReferenceView extends CardView {

    private final static String DATE_FORMAT = "dd MMM yyyy, kk'h'mm";
    private static SimpleDateFormat mDateFormat;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_comment)
    TextView mTvComment;
    @Bind(R.id.btn_delete)
    ImageButton mBtnDelete;
    @Bind(R.id.iv_photo)
    CircleImageView mIvPhoto;
    @Bind(R.id.container)
    LinearLayout mContainer;
    @Bind(R.id.tv_more_comment)
    TextView mTvMoreComment;

    public ReferenceView(Context context) {
        this(context, null);
    }

    public ReferenceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReferenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_reference, this);
        ButterKnife.bind(this);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setRadius(getResources().getDimensionPixelSize(R.dimen.card_radius));
        setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));
        setPreventCornerOverlap(false);
        setUseCompatPadding(true);
        setClipChildren(false);
        setClipToPadding(false);
        setCardBackgroundColor(getResources().getColor(R.color.transparent));

        if (mDateFormat == null) {
            mDateFormat = new SimpleDateFormat(DATE_FORMAT);
        }
    }

    public void populate(Reference reference, boolean isCommunity) {
        List<Comment> comments = new ArrayList<>(reference.getComments());
        Collections.sort(comments, (lhs, rhs) -> rhs.getAddedDate().compareTo(lhs.getAddedDate()));
        final Comment comment = comments.get(0);
        mTvDate.setText(mDateFormat.format(comment.getAddedDate()));
        mTvComment.setText(comment.getText());

        int paddingTop = getResources().getDimensionPixelSize(isCommunity ? R.dimen.view_comment_public_padding_top : R.dimen.view_comment_normal_padding_top);
        mContainer.setPadding(0, paddingTop, 0, 0);

        if (isCommunity) {
            mTvName.setText(comment.getAuthor());
            mIvPhoto.setVisibility(VISIBLE);
            Glide.with(getContext()).load(comment.getAuthorUrl()).placeholder(R.mipmap.ic_launcher).into(mIvPhoto);
        } else {
            mTvName.setText(String.format(getContext().getString(R.string.comment_ref), reference.getBookName(), reference.getChapter(), reference.getStartVerse(), reference.getStartVerse()));
            mIvPhoto.setVisibility(GONE);
        }

        if (comments.size() > 1) {
            mTvMoreComment.setText(getResources().getQuantityString(R.plurals.nb_more_comments, comments.size(), comments.size()));
            mBtnDelete.setImageResource(R.drawable.more);
            mBtnDelete.setOnClickListener(v -> {
                EventBus.getDefault().post(new OnReferenceDetailEvent(reference, this));
            });
        } else {
            mTvMoreComment.setText(null);
            mBtnDelete.setImageResource(R.drawable.delete);
            mBtnDelete.setOnClickListener(v -> {
                EventBus.getDefault().post(new OnReferenceDeletedEvent(reference));
            });
        }
    }

}
