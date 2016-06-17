package mg.yvan.truth.event;

import mg.yvan.truth.models.Reference;

/**
 * Created by Yvan on 16/06/16.
 */
public class OnReferenceDeletedEvent {

    private Reference mReference;

    public OnReferenceDeletedEvent(Reference reference) {
        mReference = reference;
    }

    public Reference getReference() {
        return mReference;
    }
}
