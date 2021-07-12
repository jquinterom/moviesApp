package com.example.moviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.example.moviesapp.databinding.FragmentSecondBinding;
import java.util.Objects;

import Classes.Comment;
import Controllers.CommentController;
import Controllers.MovieDetailsController;
import Utilities.Helpers;

public class MovieDetails extends Fragment {

    private FragmentSecondBinding binding;
    private int movieId = 0 ;
    private String movieName = "";
    private MovieDetailsController movieDetailsController;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {

            binding.webViewVideo.setVisibility(View.INVISIBLE);
            movieDetailsController = new MovieDetailsController(MovieDetails.this.getContext());

            if (getArguments() != null) {
                movieId = getArguments().getInt("movieId", 0);
                movieName = getArguments().getString("movieName", getResources().getString(R.string.app_name));
            }

            binding.fabPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getArguments() != null) {
                        movieDetailsController.showVideo(movieId, binding);
                    } else {
                        Helpers.longToast(MovieDetails.this.getContext(), getResources().getString(R.string.error_processing)).show();
                    }
                }
            });

            // Cargar los detalles de la aplicación
            movieDetailsController.getDetails(getArguments(), MovieDetails.this, binding, MovieDetails.this);

            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(movieName);

            // obteniendo focus en el comentario
            binding.etComment.requestFocus();

            // Obteniendo comentario
            Comment comment = CommentController.getInstance(MovieDetails.this.getContext()).getCommentByMovieId(movieId);
            binding.etComment.setText(comment.getComment());

            binding.btnSaveComment.setOnClickListener(view1 -> {
                Comment commentRegister = new Comment(movieId, binding.etComment.getText().toString());
                movieDetailsController.registerComment(commentRegister); // registrando comentario
            });
        } catch (Exception e){
            e.printStackTrace();
            Helpers.longToast(MovieDetails.this.getContext(), MovieDetails.this.getResources()
                    .getString(R.string.error_load_screen)).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}