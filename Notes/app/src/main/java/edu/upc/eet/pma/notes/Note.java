package edu.upc.eet.pma.notes;

/**
 * Created by pauek on 21/10/16.
 */

public class Note {
    private String title, text;

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
