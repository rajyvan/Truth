package mg.yvan.truth.ui.fragment;

import mg.yvan.truth.R;

/**
 * Created by Yvan on 10/06/16.
 */
public class CommentsFragment extends BaseFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_comments;
    }

    @Override
    public int getTitle() {
        return R.string.menu_comments;
    }

    @Override
    public boolean showBackButton() {
        return false;
    }
}
