package Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class Helpers {

    public static ProgressDialog pDialog(Context ctx, String title, String message){

        ProgressDialog pDialog = new ProgressDialog(ctx);
        pDialog.setTitle(title);
        pDialog.setMessage(message);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        return pDialog;
    }

    public static void closePDialog(ProgressDialog pDialog){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

    // Toast
    public static Toast longToast(Context ctx, String message){
        return Toast.makeText(ctx, message, Toast.LENGTH_LONG);
    }

}
