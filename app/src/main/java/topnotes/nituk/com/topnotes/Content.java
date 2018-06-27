package topnotes.nituk.com.topnotes;

import java.util.Date;

// Model for a single Content(e.g- Notes,Question Paper etc.)
public class Content {
    private String Title;
    private String author;
    private Date date;


    public Content() {


    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



}
