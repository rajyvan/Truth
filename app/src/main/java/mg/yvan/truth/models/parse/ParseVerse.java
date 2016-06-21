package mg.yvan.truth.models.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

import mg.yvan.truth.models.Verse;

/**
 * Created by Yvan on 20/06/16.
 */
@ParseClassName("Verse")
public class ParseVerse extends ParseObject {

    public static ParseVerse from(Verse verse) {
        ParseVerse parseVerse = new ParseVerse();
        parseVerse.setVerse(verse.getVerse());
        parseVerse.setText(verse.getText());
        parseVerse.setBookId(verse.getBookId());
        parseVerse.setBook(verse.getBook());
        parseVerse.setChapter(verse.getChapter());
        parseVerse.setDateAdded(verse.getDateAdded());
        parseVerse.setObjectId(verse.getParseId());
        return parseVerse;
    }

    public static Verse toVerse(ParseVerse parseVerse) {
        Verse verse = new Verse();
        verse.setVerse(parseVerse.getVerse());
        verse.setText(parseVerse.getText());
        verse.setBookId(parseVerse.getBookId());
        verse.setBook(parseVerse.getBook());
        verse.setChapter(parseVerse.getChapter());
        verse.setDateAdded(parseVerse.getDateAdded());
        verse.setParseId(parseVerse.getObjectId());
        return verse;
    }

    public int getBookId() {
        return getInt("bookId");
    }

    public void setBookId(int bookId) {
        put("bookId", bookId);
    }

    public int getChapter() {
        return getInt("chapter");
    }

    public void setChapter(int chapter) {
        put("chapter", chapter);
    }

    public int getVerse() {
        return getInt("verse");
    }

    public void setVerse(int verse) {
        put("verse", verse);
    }

    public String getText() {
        return getString("text");
    }

    public void setText(String text) {
        put("text", text);
    }

    public String getBook() {
        return getString("book");
    }

    public void setBook(String book) {
        put("book", book);
    }

    public Date getDateAdded() {
        return getDate("dateAdded");
    }

    public void setDateAdded(Date dateAdded) {
        put("dateAdded", dateAdded);
    }

    public boolean isSynced() {
        return getBoolean("isSynced");
    }

    public void setSynced(boolean synced) {
        put("isSynced", synced);
    }
}
