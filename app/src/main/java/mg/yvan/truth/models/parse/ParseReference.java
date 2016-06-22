package mg.yvan.truth.models.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

import mg.yvan.truth.models.Reference;

/**
 * Created by Yvan on 20/06/16.
 */
@ParseClassName("Reference")
public class ParseReference extends ParseObject {

    public static ParseReference from(Reference reference) {
        ParseReference parseReference = new ParseReference();
        parseReference.setObjectId(reference.getParseId());
        parseReference.setBookName(reference.getBookName());
        parseReference.setChapter(reference.getChapter());
        parseReference.setBookId(reference.getBookId());
        parseReference.setStartVerse(reference.getStartVerse());
        parseReference.setEndVerse(reference.getEndVerse());
        parseReference.setUpdateDate(reference.getUpdateDate());
        return parseReference;
    }

    public static Reference toReference(ParseReference parseReference) {
        Reference reference = new Reference();
        reference.setParseId(parseReference.getObjectId());
        reference.setBookName(parseReference.getBookName());
        reference.setBookId(parseReference.getBookId());
        reference.setChapter(parseReference.getChapter());
        reference.setStartVerse(parseReference.getStartVerse());
        reference.setEndVerse(parseReference.getEndVerse());
        reference.setUpdateDate(parseReference.getUpdateDate());
        return reference;
    }

    public int getBookId() {
        return getInt("bookId");
    }

    public void setBookId(int bookId) {
        put("bookId", bookId);
    }

    public String getBookName() {
        return getString("bookName");
    }

    public void setBookName(String bookName) {
        put("bookName", bookName);
    }

    public int getChapter() {
        return getInt("chapter");
    }

    public void setChapter(int chapter) {
        put("chapter", chapter);
    }

    public int getStartVerse() {
        return getInt("startVerse");
    }

    public void setStartVerse(int startVerse) {
        put("startVerse", startVerse);
    }

    public int getEndVerse() {
        return getInt("endVerse");
    }

    public void setEndVerse(int endVerse) {
        put("endVerse", endVerse);
    }

    public Date getUpdateDate() {
        return getDate("updateDate");
    }

    public void setUpdateDate(Date updateDate) {
        put("updateDate", updateDate);
    }
}
