package topnotes.nituk.com.topnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TopNotes";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    private static final String CREATE = "create table " + DbContract.TABLE_NAME +
            "(id integer primary key autoincrement," + DbContract.CONTENT + " BLOB,"+ DbContract.SUBJECT_NUMBER + " text," + DbContract.SUBJECT_TYPE_NUMBER + " text);";

    private static final String DROP_TABLE = "drop table if exists " + DbContract.TABLE_NAME;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void saveContentList(List<Content> contentList, int subjectNumber, int subjectTypeNumber,SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < contentList.size(); i++) {
            byte[] data = SerializationUtils.serialize(contentList.get(i));
            contentValues.put(DbContract.CONTENT, data);
            contentValues.put(DbContract.SUBJECT_NUMBER, subjectNumber);
            contentValues.put(DbContract.SUBJECT_TYPE_NUMBER, subjectTypeNumber);
            sqLiteDatabase.insert(DbContract.TABLE_NAME, null, contentValues);

        }
    }

    public List<Content> readContentList(int subjectNumber,int subjectTupeNumber){

        List<Content> contentList=new ArrayList<>();

        DbHelper dbHelper=new DbHelper(context);
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+DbContract.TABLE_NAME+" WHERE "
                    +DbContract.SUBJECT_NUMBER+"="+subjectNumber+" AND "
                    +DbContract.SUBJECT_TYPE_NUMBER+"="+subjectTupeNumber,null);

            if(cursor.getCount()>0){
                while(cursor.moveToNext()){
                    int contentIndex=cursor.getColumnIndex(DbContract.CONTENT);
                    byte[] data=cursor.getBlob(contentIndex);
                    Content content = SerializationUtils.deserialize(data);
                    contentList.add(content);
                   }
                cursor.close();
                dbHelper.close();
            }

            return contentList;


    }
}