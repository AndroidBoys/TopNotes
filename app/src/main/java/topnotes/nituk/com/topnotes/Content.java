package topnotes.nituk.com.topnotes;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Model for a single Content(e.g- Notes,Question Paper etc.)
public class Content implements Serializable {
    public String title;
    public String author;
    public String date;
    public String downloadUrl;
    public String subject;
    public String size;
    public int downloads;
    public String fileName;


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
                &&content.getDownloadUrl().equals(this.getDownloadUrl())
        &&content.getSubject().equals(this.getSubject());
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
