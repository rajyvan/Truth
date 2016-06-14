package mg.yvan.truth.ui.fragment;

import mg.yvan.truth.R;

/**
 * Created by Yvan on 10/06/16.
 */
public class MyStatisticFragment extends BaseFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_statistics;
    }

    @Override
    public int getTitle() {
        return R.string.menu_my_statistics;
    }
}
