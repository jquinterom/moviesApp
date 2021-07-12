package Controllers;

import android.annotation.SuppressLint;
import android.content.Context;

import Classes.Comment;
import db.dbManager;

public class CommentController {
    private static dbManager manager;
    @SuppressLint("StaticFieldLeak")
    private static CommentController mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context ctx;


    private CommentController(Context context){
        ctx = context;
        manager = new dbManager(ctx);
    }


    public static synchronized CommentController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CommentController(context);
        }
        return mInstance;
    }

    // Registrar comentario
    public boolean registerComment(Comment comment){
        return manager.registerComment(comment);
    }

    // consultar comentarios
    public Comment getCommentByMovieId(int movieId){
        return manager.getCommentByMovieId(movieId);
    }
}
