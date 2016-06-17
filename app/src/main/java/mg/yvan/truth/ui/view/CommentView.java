package mg.yvan.truth.ui.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnCommentDeletedEvent;
import mg.yvan.truth.models.Comment;

/**
 * Created by Yvan on 31/05/16.
 */
public class CommentView extends CardView {

    private final static String DATE_FORMAT = "dd MMM yyyy, kk'h'mm";
    private static SimpleDateFormat mDateFormat;

    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_comment)
    TextView mTvComment;
    @Bind(R.id.btn_delete)
    ImageButton mBtnDelete;

    public CommentView(Context context) {
        this(context, null);
    }

    public CommentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_comment, this);
        ButterKnife.bind(this);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setRadius(getResources().getDimensionPixelSize(R.dimen.card_radius));
        setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));
        setPreventCornerOverlap(false);
        setUseCompatPadding(true);
        setClipChildren(false);
        setClipToPadding(false);

        if (mDateFormat == null) {
            mDateFormat = new SimpleDateFormat(DATE_FORMAT);
        }
    }

    public void populate(Comment comment) {
        mTvDate.setText(mDateFormat.format(comment.getAddedDate()));
        mTvComment.setText(comment.getText());
        mBtnDelete.setOnClickListener(v -> {
            EventBus.getDefault().post(new OnCommentDeletedEvent(comment));
        });
    }

}
