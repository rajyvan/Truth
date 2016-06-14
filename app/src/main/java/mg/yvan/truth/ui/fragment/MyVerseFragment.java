package mg.yvan.truth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import mg.yvan.truth.R;

/**
 * Created by Yvan on 10/06/16.
 */
public class MyVerseFragment extends BaseFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_verse;
    }

    @Override
    public int getTitle() {
        return R.string.menu_my_verse;
    }
}
