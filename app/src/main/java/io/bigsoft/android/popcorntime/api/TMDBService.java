package io.bigsoft.android.popcorntime.api;

import io.bigsoft.android.popcorntime.model.MovieResponses;
import io.bigsoft.android.popcorntime.model.ReviewResponses;
import io.bigsoft.android.popcorntime.model.TrailerResponses;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBService {
    @GET("movie/popular")
    Call<MovieResponses> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieResponses> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{id}/reviews")
    Call<TrailerResponses> getVideos(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponses> getReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
