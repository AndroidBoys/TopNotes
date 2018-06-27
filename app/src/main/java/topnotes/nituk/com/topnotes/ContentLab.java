package topnotes.nituk.com.topnotes;

import android.content.Context;

import java.util.Date;
import java.util.List;
import java.util.Random;

// Singleton class representing the data model for a list of contents

public class ContentLab {
    private static ContentLab sContentLab;
    private List<Content> mContents;

    private ContentLab(Context context)
    {
       sContentLab = new ContentLab(context);
       // sample data
       for(int i=0;i<20;i++)
       {
          Content content = new Content();
          content.setAuthor("Student"+i);
          content.setDate(new Date());
          content.setTitle("Title"+i);
          mContents.add(content);

       }
    }
    public static ContentLab getInstance(Context context)
    {
        if(sContentLab== null) {
            return new ContentLab(context);
        }
        return sContentLab;
    }

    public List<Content> getContents()
    {
        return mContents;
    }


}
