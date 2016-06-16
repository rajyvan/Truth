package mg.yvan.truth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnNewCommentEvent;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.ui.dialog.NewCommentDialog;

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
    @Bind(R.id.fab)
    FloatingActionButton mFab;

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

        mFab.setOnClickListener(v -> {
            NewCommentDialog.show((AppCompatActivity) getActivity(), mVerse);
        });

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_edit_comment;
    }

    @Override
    public int getTitle() {
        return 0;
    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEventMainThread(OnNewCommentEvent event) {

    }
}
