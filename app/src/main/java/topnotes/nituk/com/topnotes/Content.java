package topnotes.nituk.com.topnotes;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Model for a single Content(e.g- Notes,Question Paper etc.)
public class Content implements Serializable {
    private String title;
    private String author;
    private String date;
    private String downloadUrl;


    public Content() {

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public boolean equals(Object obj) {
        Content content =(Content)obj;
        return content.getTitle().equals(this.getTitle())
                && content.getAuthor().equals(this.getAuthor())
                &&content.getDate().equals(this.getDate())
                &&content.getDownloadUrl().equals(this.getDownloadUrl());
    }
}
