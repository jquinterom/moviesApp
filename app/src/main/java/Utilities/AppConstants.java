package Utilities;

public class AppConstants {
    private static final String apiKey = "40cf4d7fc54c9d9c6f41796202d44350";
    private static final String urlBase = "https://api.themoviedb.org/3/movie/";
    private static final String urlImagesMovies = "https://image.tmdb.org/t/p/w500";
    private static final String urlVideos = "https://api.themoviedb.org/3/movie/";
    private static final String urlYoutube = "https://www.youtube.com/watch?v=";


    public static String urlPopular(int page){
        return urlBase + "popular?api_key=" + apiKey + "&language=es-ES&page=" + page;
    }

    public static String urlMovieDetails(int movieId){
        return urlBase + movieId + "?api_key=" + apiKey + "&language=es-ES";
    }

    public static String urlImageMovie(String urlImage){
        return urlImagesMovies + urlImage;
    }

    public static String getUrlVideos(int movieId){
        return urlVideos + movieId + "/videos?api_key=" +apiKey + "&language=en-ES";
    }

    public static String getUrlYoutube(String videoCode){
        return urlYoutube + videoCode;
    }


}
