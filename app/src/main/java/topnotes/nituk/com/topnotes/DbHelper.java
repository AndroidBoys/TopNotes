package topnotes.nituk.com.topnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;


//his class makes it easy for ContentProvider implementations to defer opening and upgrading the database until first use, to avoid blocking application startup with long-running database upgrades.
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TopNotes";//name of the database
    private static final int DATABASE_VERSION = 1;//database version
    private Context context;

    //syntax of sql to create a a database
    private static final String CREATE = "create table " + DbContract.TABLE_NAME +
            "(id integer primary key autoincrement," + DbContract.CONTENT + " BLOB," + DbContract.SUBJECT_NUMBER + " text," + DbContract.SUBJECT_TYPE_NUMBER + " text);";

    // if table is exist than drop this table(syntax)
    private static final String DROP_TABLE = "drop table if exists " + DbContract.TABLE_NAME;


    //constructor to create a DbHelper object
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;  //saving context
    }

    // calling this method result in database creation
    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    //called when a database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE);

    }

    //called when a database need to be upgrade
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    // custom method to save list of content in database
    public void saveContentList(List<Content> contentList, int subjectNumber, int subjectTypeNumber) {
        DbHelper dbHelper = new DbHelper(context); //creating DbHelper object

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();//creating database


        List<Content> existingList = dbHelper.readContentList(subjectNumber,subjectTypeNumber);

        ContentValues contentValues = new ContentValues();

        //creating a ContentValues object, it will contain values in a set

        for (int i = 0; i < contentList.size(); i++) {


            if(existingList.contains(contentList.get(i)))
            {
                continue;
            }

            //serializing the object, since we can't store objects in sqlite
            byte[] data = SerializationUtils.serialize(contentList.get(i));

            //for retrieving the data
//            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DbContract.TABLE_NAME + " WHERE "
//                    + DbContract.SUBJECT_NUMBER + "=" + subjectNumber + " AND "
//                    + DbContract.SUBJECT_TYPE_NUMBER + "=" + subjectTypeNumber
//                    + " AND " + DbContract.CONTENT + "=" + data, null);
//
//            //if data already found it database then don't add it in database
//            if (cursor.getCount() > 0)
//                continue;

           //adding values in databse
            contentValues.put(DbContract.CONTENT, data);
            contentValues.put(DbContract.SUBJECT_NUMBER, subjectNumber);
            contentValues.put(DbContract.SUBJECT_TYPE_NUMBER, subjectTypeNumber);
            sqLiteDatabase.insert(DbContract.TABLE_NAME, null, contentValues);



        }
        Log.i("savedtodb:",contentList.toString());
    }

    public List<Content> readContentList(int subjectNumber, int subjectTupeNumber) {

        List<Content> contentList = new ArrayList<>();

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DbContract.TABLE_NAME + " WHERE "
                + DbContract.SUBJECT_NUMBER + "=" + subjectNumber + " AND "
                + DbContract.SUBJECT_TYPE_NUMBER + "=" + subjectTupeNumber, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int contentIndex = cursor.getColumnIndex(DbContract.CONTENT);
                byte[] data = cursor.getBlob(contentIndex);
                Content content = SerializationUtils.deserialize(data);
                contentList.add(content);
            }
        //closing the database
            cursor.close();
            dbHelper.close();
        }

        return contentList;


    }
}