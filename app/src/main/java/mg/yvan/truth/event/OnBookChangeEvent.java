package mg.yvan.truth.event;

/**
 * Created by Yvan on 03/06/15.
 */
public class OnBookChangeEvent {

    private long bookId;
    private int chapter;
    private int verse;

    public OnBookChangeEvent(long bookId, int chapter, int verse) {
        this.bookId = bookId;
        this.chapter = chapter;
        this.verse = verse;
    }

    public long getBookId() {
        return bookId;
    }

    public int getChapter() {
        return chapter;
    }

    public int getVerse() {
        return verse;
    }

}
