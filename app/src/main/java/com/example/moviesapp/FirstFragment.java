package com.example.moviesapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import androidx.navigation.fragment.NavHostFragment;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.moviesapp.databinding.FragmentFirstBinding;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Objects;
import Utilities.AppConstants;
import Utilities.GridAdapter;
import Utilities.Helpers;
import Utilities.HttpSingleton;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private int V_PAGE = 1;

    private int[] moviesId;
    private String[] namesMovies ;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(getLayoutInflater() );
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // realizando llamada al API
        getMovies();

        binding.gridMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putInt("movieId", moviesId[i]);
                args.putString("movieName", namesMovies[i]);
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, args);

            }
        });

        binding.fabPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                V_PAGE--;
                // Se verifica que no realice peticion a una pagina inexistente
                if(V_PAGE == 0){
                    V_PAGE = 1;
                }
                getMovies();
            }
        });

        binding.fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                V_PAGE++;
                getMovies();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Obtener las peliculas populares por página
    private void getMovies() {
        ProgressDialog pDialog = Helpers.pDialog(FirstFragment.this.getContext(), getResources().getString(R.string.loading),
                getString(R.string.loading_popular_movies));
        pDialog.show();

        // Asignando nombre a la actividad
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.title_activity);

        // Verificando que no consulte pagina superior a indicada por el api
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConstants.urlPopular(V_PAGE), null, response -> {
            try {
                // obteniendo el arreglo de las peliculas
                JSONArray jsonArray = response.getJSONArray("results");
                namesMovies = new String[jsonArray.length()];
                String[] urlsMovies = new String[jsonArray.length()];
                moviesId = new int[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    namesMovies[i] = jsonObject.getString("title");
                    urlsMovies[i] = AppConstants.urlImageMovie(jsonObject.getString( "poster_path" ));
                    moviesId[i] = jsonObject.getInt("id");
                }

                // Agregando el arreglo al adaptador
                GridAdapter gridAdapter = new GridAdapter(FirstFragment.this.getContext(), namesMovies, urlsMovies);
                binding.gridMovies.setAdapter(gridAdapter);

                // Agregar contador
                binding.txtPageCount.setText(new StringBuilder().append( getResources().getString(R.string.page))
                        .append(" ")
                        .append(response.getString("page"))
                        .append(" de ")
                        .append(response.getString("total_pages")));

                // Cerrar dialogo
                Helpers.closePDialog(pDialog);
            } catch (Exception ex) {
                ex.printStackTrace();
                Helpers.closePDialog(pDialog);
                Helpers.longToast(FirstFragment.this.getContext(), getResources().getString(R.string.alt_no_task_complete)).show();
            }
        }, error -> {
            if(error instanceof NoConnectionError){
                Helpers.longToast(FirstFragment.this.getContext(), getString(R.string.no_server)).show();
            } else {
                Helpers.longToast(FirstFragment.this.getContext(), getString(R.string.error_processing)).show();
            }
            error.printStackTrace();
            Helpers.closePDialog(pDialog);
        });
        HttpSingleton.getInstance(FirstFragment.this.getContext()).addToRequestQueue(jsonObjectRequest);
    }
}