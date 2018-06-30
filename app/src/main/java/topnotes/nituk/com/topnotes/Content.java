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


    public Content(String title, String author, String date, String downloadUrl) {
        this.setTitle(title);
        this.setAuthor(author);
        this.setDate(date);
        this.setDownloadUrl(downloadUrl);
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

}
