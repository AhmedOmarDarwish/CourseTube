package app.com.youtubeapiv3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String coursename, String courseabout, byte[] image,String playlist){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO COURSES VALUES (NULL, ?, ?,?,?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, coursename);
        statement.bindString(2, courseabout);
        statement.bindBlob(3, image);
        statement.bindString(4, playlist);

        statement.executeInsert();
    }

    public void updateData(String coursename, String courseabout, byte[] image,String playlist, int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE COURSES SET coursename = ?, courseabout = ?,image=?,playlist=? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, coursename);
        statement.bindString(2, courseabout);
        statement.bindBlob(3, image);
        statement.bindString(4, playlist);
        statement.bindDouble(5, (double)id);

        statement.execute();
        database.close();
    }

    public  void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM COURSES WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
