package hikaru.moreorless.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import hikaru.moreorless.Model.Streamer;


public class DBHelper extends SQLiteAssetHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "guess.db";
    private static final String TABLE_NAME = "streamer";

    private static final String KEY_NAME = "name";
    private static final String KEY_FOLLOWS = "follows";
    private static final String KEY_IMAGE = "image";

    String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            KEY_NAME + " TEXT," +
            KEY_FOLLOWS + " TEXT";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Get the Single Contact
    public List<Streamer> getStreamers(Integer numRows)
    {
        List<Streamer> contactList = new ArrayList<Streamer>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT" + numRows;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_NAME, KEY_FOLLOWS, KEY_IMAGE }, null, null, null, null, null, String.valueOf(numRows));
        // return contact
        try {
            if (cursor.moveToFirst()) {
                do {
                    Streamer streamer = new Streamer();
                    streamer.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                    streamer.setImage(cursor.getString(cursor.getColumnIndex(KEY_IMAGE)));
                    streamer.setFollows(cursor.getInt(cursor.getColumnIndex(KEY_FOLLOWS)));
                    contactList.add(streamer);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            System.out.println("Error while trying to get posts from database");
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        }
        return contactList;
    }
    public void insertNewData(String name, String logo, String followers)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_FOLLOWS, logo);
        values.put(KEY_IMAGE, followers);

        // updating row
        db.execSQL("INSERT INTO " + TABLE_NAME + "('" + KEY_NAME + "', '" + KEY_FOLLOWS + "', '" + KEY_IMAGE + "') VALUES('" + name + "', '" +  logo + "', '" +  followers + "')");
        db.close();
    }

    public void deleteData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    public boolean doesDataExist()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;

    }
}