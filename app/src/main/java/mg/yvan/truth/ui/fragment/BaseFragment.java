package mg.yvan.truth.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Yvan on 16/04/16.
 */
public abstract class BaseFragment extends Fragment {

    protected abstract int getLayout();

    public abstract int getTitle();

    protected void setTitle(String title) {
        getActivity().setTitle(title);
    }

    public abstract boolean showBackButton();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        if (getTitle() > 0) {
            setTitle(getString(getTitle()));
        }
        return view;
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

}

