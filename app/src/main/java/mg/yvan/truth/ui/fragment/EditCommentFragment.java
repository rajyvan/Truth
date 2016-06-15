package mg.yvan.truth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import mg.yvan.truth.R;
import mg.yvan.truth.models.Verse;

/**
 * Created by Yvan on 15/06/16.
 */
public class EditCommentFragment extends BaseFragment {

    @Bind(R.id.tv_ref)
    TextView mTvRef;
    @Bind(R.id.tv_verse)
    TextView mTvVerse;
    @Bind(R.id.recycler)
    RecyclerView mRecycler;

    private Verse mVerse;

    public static EditCommentFragment newInstance(Verse verse) {

        EditCommentFragment fragment = new EditCommentFragment();
        fragment.setVerse(verse);
        return fragment;
    }

    public void setVerse(Verse verse) {
        mVerse = verse;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setTransitionName(mTvVerse, getResources().getString(R.string.transition_verse));
        mTvVerse.setText(mVerse.getText());
        mTvRef.setText(mVerse.formatReference(getActivity()));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_edit_comment;
    }

    @Override
    public int getTitle() {
        return 0;
    }

}
