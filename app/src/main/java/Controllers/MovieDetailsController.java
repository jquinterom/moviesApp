package Controllers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.navigation.fragment.NavHostFragment;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.moviesapp.MovieDetails;
import com.example.moviesapp.R;
import com.example.moviesapp.databinding.FragmentSecondBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Classes.Comment;
import Utilities.AppConstants;
import Utilities.Helpers;
import Utilities.HttpSingleton;

public class MovieDetailsController {
    Context ctx;
    private ImageLoader imageLoader;

    public MovieDetailsController(Context ctx) {
        this.ctx = ctx;
    }

    // Busca el video en la plataforma correspondiente y lo muestra
    public void showVideo(int movieId, FragmentSecondBinding fragment){
        ProgressDialog pDialog = Helpers.pDialog(ctx, ctx.getResources().getString(R.string.loading),
                ctx.getResources().getString(R.string.loading_movie));
        pDialog.show();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.getUrlVideos(movieId), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        String key = "";
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getString("type").equals("Trailer")){
                                key = jsonObject.getString("key");
                            }
                        }
                        if(!key.equals("")){
                            // realizar peticion de video
                            fragment.webViewVideo.loadUrl(AppConstants.getUrlYoutube(key));
                        } else {
                            Helpers.longToast(ctx, "No existe trailer").show();
                        }

                    } catch (JSONException e){
                        Helpers.longToast(ctx, ctx.getResources().getString(R.string.error_processing)).show();
                        e.printStackTrace();
                        Helpers.closePDialog(pDialog);
                    }
                    catch (Exception e){
                        Helpers.longToast(ctx, ctx.getResources().getString(R.string.error_processing)).show();
                        e.printStackTrace();
                        Helpers.closePDialog(pDialog);
                    }

                    Helpers.closePDialog(pDialog);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NoConnectionError) {
                        Helpers.longToast(ctx, ctx.getResources().getString(R.string.no_server)).show();
                    } else {
                        Helpers.longToast(ctx, ctx.getResources().getString(R.string.error_processing)).show();
                    }
                    error.printStackTrace();
                    Helpers.closePDialog(pDialog);
                }
            });

        HttpSingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    // realiza el proceso de carga de lso detalles de la pelicula
    public void getDetails(Bundle args, MovieDetails movieDetails, FragmentSecondBinding binding, MovieDetails mDetails){
        ProgressDialog pDialog = Helpers.pDialog(ctx, ctx.getResources().getString(R.string.loading),
                ctx.getResources().getString(R.string.loading_movie));
        pDialog.show();
        try{
            if (args == null) {
                // Informar que no hay información
                Helpers.longToast(ctx, ctx.getResources().getString(R.string.alt_no_data)).show();
                Helpers.closePDialog(pDialog);

                NavHostFragment.findNavController(movieDetails)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);

            } else {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        AppConstants.urlMovieDetails(args.getInt("movieId", 0)), null, response -> {
                    try {
                        // Cargando el poster de la pelicula
                        String backdrop_path ;
                        if(!response.getString("backdrop_path").equals("null")){
                            backdrop_path = response.getString("backdrop_path");
                        } else {
                            backdrop_path = response.getJSONObject("belongs_to_collection").getString("backdrop_path");
                        }
                        String urlImg = AppConstants.urlImageMovie(backdrop_path);
                        // Cargando imagen
                        imageLoader = HttpSingleton.getInstance(ctx)
                                .getImageLoader();
                        imageLoader.get(urlImg, ImageLoader.getImageListener(binding.imageMovie,
                                R.drawable.loading, android.R.drawable
                                        .ic_dialog_alert));
                        // Cargando imagen en segundo plano para evitar problemas de continuidad
                        binding.imageMovie.post(() -> binding.imageMovie.setImageUrl(urlImg, imageLoader));

                        // Cargando Texto de resumen
                        binding.txtSummary.setText(response.getString("overview"));

                        // Cargando generos
                        JSONArray jsonArrayGenders = response.getJSONArray("genres");
                        StringBuilder genres = new StringBuilder();
                        for(int i = 0; i < jsonArrayGenders.length(); i++){
                            genres.append(jsonArrayGenders.getJSONObject(i).getString("name")).append(", ");
                        }
                        binding.txtGenresMovie.setText(genres.substring(0, genres.length() -2));
                        binding.txtRateMovie.setText(new StringBuilder().append(response.getString("vote_average")).
                                append(" Total Votos: ").append(response.getString("vote_count")));

                        // Año de la pelicula
                        if(!response.getString("release_date").equals("")) {
                            @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("yyyy-MM-dd").
                                    parse(response.getString("release_date"));
                            assert date != null;
                            @SuppressLint("SimpleDateFormat") String dateStr = new SimpleDateFormat("yyyy").format(date);
                            binding.txtSummaryTitle.setText(new StringBuilder().append(binding.txtSummaryTitle.getText()).append(" - ").append(dateStr));
                        }
                    } catch (JSONException jsonException) {
                        Helpers.longToast(ctx, ctx.getResources().getString(R.string.alt_no_task_complete)).show();
                        jsonException.printStackTrace();
                        Helpers.closePDialog(pDialog);
                    } catch (ParseException e) {
                        Helpers.longToast(ctx, ctx.getResources().getString(R.string.alt_no_task_complete)).show();
                        e.printStackTrace();
                        Helpers.closePDialog(pDialog);
                    }
                    Helpers.closePDialog(pDialog);
                }, error -> {
                    if(error instanceof NoConnectionError){
                        Helpers.longToast(ctx, ctx.getResources().getString(R.string.no_server)).show();
                    } else {
                        Helpers.longToast(ctx, ctx.getResources().getString(R.string.error_processing)).show();
                    }
                    error.printStackTrace();
                    Helpers.closePDialog(pDialog);

                    NavHostFragment.findNavController(movieDetails)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                });
                // Asignando peticion a la cola
                HttpSingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            Helpers.closePDialog(pDialog);
            Helpers.longToast(ctx, ctx.getResources().getString(R.string.error_processing)).show();
        }
    }

    public void registerComment(Comment comment){
        // obteniendo comentario
        boolean commentRegister = CommentController.getInstance(ctx).registerComment(comment);

        if(commentRegister){
            Helpers.longToast(ctx,ctx.getResources().getString(R.string.registered)).show();
        } else {
            Helpers.longToast(ctx, ctx.getResources().getString(R.string.error_processing)).show();
        }
    }
}
