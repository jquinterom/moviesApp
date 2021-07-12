package db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Classes.Comment;

public class dbManager {

    private SQLiteDatabase db;
    private ContentValues values = null;
    private String response;
    private final String[] ALL_COLUMNS = {"*"};

    /**
     * CONSTRUCTOR
     **/
    public dbManager(Context context) {

        dbHelper helper = new dbHelper(context);
        db = helper.getWritableDatabase();
    }

    private static String TABLE_COMMENTS = "comments";


    static final String CREATE_TABLE_COMMENTS = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMENTS +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "movie_id INTEGER NOT NULL," +
            "comment TEXT NOT NULL)";


    // Procesos sobre la base de datos
    // registrando nuevo comentario
    public boolean registerComment(Comment comment){
        boolean response = true;
        try{
            values = new ContentValues();
            values.put("movie_id", comment.getMovie_id());
            values.put("comment", comment.getComment());
            db.insert(TABLE_COMMENTS, null, values);
        } catch (Exception e){
            e.printStackTrace();
            response = false;
        }
        return response;
    }


    // obteniendo comentario por movieId
    public Comment getCommentByMovieId(int movieId){
        Comment response = new Comment();
        try{
            @SuppressLint("Recycle") Cursor cComment = db.query(TABLE_COMMENTS, new String[] {"movie_id", "comment"} , "movie_id=?",
                    new String[] {String.valueOf(movieId)}, null, null, null, null);

            while (cComment.moveToNext()){
                response.setMovie_id(cComment.getInt(0));
                response.setComment(cComment.getString(1));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }
}
