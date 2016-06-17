package mg.yvan.truth.event;

import android.view.View;

import mg.yvan.truth.models.Reference;

/**
 * Created by Yvan on 16/06/16.
 */
public class OnReferenceDetailEvent {

    private Reference mReference;
    private View mSharedView;

    public OnReferenceDetailEvent(Reference reference, View sharedView) {
        mReference = reference;
        mSharedView = sharedView;
    }

    public Reference getReference() {
        return mReference;
    }

    public View getSharedView() {
        return mSharedView;
    }
}
