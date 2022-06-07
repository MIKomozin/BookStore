package skbx.example.struct.book.links;

import java.io.Serializable;

public class BookToTagId implements Serializable {

    private int tag;
    private int book;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }
}
