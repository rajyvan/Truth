package mg.yvan.truth.ui.dialog;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import mg.yvan.truth.R;
import mg.yvan.truth.event.OnNewCommentEvent;
import mg.yvan.truth.models.Comment;
import mg.yvan.truth.models.Verse;
import mg.yvan.truth.models.database.DataVerse;
import mg.yvan.truth.models.database.RealmHelper;

/**
 * Created by Yvan on 07/06/16.
 */
public class NewCommentDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "new_comment_dialog";
    private static final int LOADER_ID = 1;

    @Bind(R.id.tv_ref)
    TextView mTvRef;
    @Bind(R.id.et_comment)
    EditText mEtComment;
    @Bind(R.id.iv_photo)
    CircleImageView mIvPhoto;
    @Bind(R.id.end_verse)
    Spinner mEndVerse;
    @Bind(R.id.btn_send)
    Button mBtnSend;

    private Verse mVerse;

    public static void show(AppCompatActivity activity, Verse verse) {
        NewCommentDialog dialog = NewCommentDialog.newInstance(verse);
        dialog.show(activity.getSupportFragmentManager(), TAG);
    }

    public static NewCommentDialog newInstance(Verse verse) {
        NewCommentDialog fragment = new NewCommentDialog();
        fragment.setVerse(verse);
        return fragment;
    }

    public void setVerse(Verse verse) {
        mVerse = verse;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_new_comment, container, false);
        ButterKnife.bind(this, view);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9f);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.85f);
        getDialog().getWindow().setLayout(width, height);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEtComment.setCursorVisible(true);
        mTvRef.setText(String.format(getString(R.string.new_comment_ref), mVerse.getBook(), mVerse.getChapter(), mVerse.getVerse()));
        getLoaderManager().initLoader(LOADER_ID, null, this);

        mBtnSend.setOnClickListener(v -> {
            final String text = mEtComment.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                final ParseUser user = ParseUser.getCurrentUser();

                Realm realm = RealmHelper.getInstance().getRealmForMainThread();
                realm.beginTransaction();
                final Comment comment = realm.createObject(Comment.class);
                comment.setText(text);
                comment.setAddedDate(new Date());
                comment.setBookId(mVerse.getBookId());
                comment.setBookName(mVerse.getBook());
                comment.setChapter(mVerse.getChapter());
                comment.setStartVerse(mVerse.getVerse());
                comment.setEndVerse(mEndVerse.getSelectedItemPosition() + 1);
                if (user != null && ParseFacebookUtils.isLinked(user)) {
                    comment.setAuthor(user.getUsername());
                    comment.setAuthorUrl(user.getString("photo"));
                }
                realm.commitTransaction();
                EventBus.getDefault().post(new OnNewCommentEvent(comment));
            }
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] args = new String[]{String.valueOf(mVerse.getBookId()), String.valueOf(mVerse.getChapter())};
        return new CursorLoader(getActivity(), DataVerse.CONTENT_URI, DataVerse.PROJECTION_ALL, DataVerse.BOOK_ID + " = ? AND " + DataVerse.CHAPTER + " = ?", args, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            final int count = cursor.getCount();
            List<String> verses = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                verses.add(String.valueOf(i + 1));
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, verses);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mEndVerse.setAdapter(dataAdapter);
            mEndVerse.setSelection(mVerse.getVerse() - 1);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
