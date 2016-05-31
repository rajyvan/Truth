package mg.yvan.truth.ui.fragment;

import mg.yvan.truth.R;

/**
 * Created by Yvan on 30/05/16.
 */
public class BibleFragment extends BaseFragment {

    @Override
    protected int getLayout() {
        return R.layout.recyclerview;
    }

    @Override
    public int getTitle() {
        return R.string.menu_bible;
    }

}
