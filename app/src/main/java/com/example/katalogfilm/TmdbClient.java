package com.example.katalogfilm;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TmdbClient {

    private static final String TOKEN = "b0b1b7542963befc2f848ce363e5c4ab";

    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface MovieCallback {
        void onSuccess(List<Movie> movies);
        void onError(String msg);
    }

    public static void getPopularMovies(MovieCallback callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/now_playing" + "?language=id-ID&page=1");

                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(1500);
                connection.setReadTimeout(15000);

                connection.setRequestProperty(
                        "Authorization", "Bearer" + TOKEN
                );

                connection.setRequestProperty(
                        "accept", "appliaction/json"
                );

                int status = connection.getResponseCode();

                InputStream stream =
                        (status >= 200 && status < 300)
                        ? connection.getInputStream()
                                : connection.getErrorStream();

                StringBuilder response = new StringBuilder();

                try (BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        stream, StandardCharsets.UTF_8))) {

                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                if (status < 200 || status >= 300) {
                    throw new Exception(
                            "HTTP" + status + ":  " + response
                    );
                }

                JSONObject json =
                        new JSONObject(response.toString());

                JSONArray result =
                        json.getJSONArray("result");

                List<Movie> movies = new ArrayList<>();

                for (int i = 0; i < result.length(); i++){
                    JSONObject item = result.getJSONObject(i);

                    movies.add(new Movie(
                            item.optInt("id"),
                            item.optString("title", "Tanpa judul"),
                            item.isNull("poster_path")
                             ? null
                                    : item.optString("poster_path"),
                            item.optDouble("vote_average", 0.0)
                    ));
                }

                mainHandler.post(
                        () -> callback.onSuccess(movies)
                );
            } catch (Exception e) {
                String msg = e.getMessage() != null
                        ? e.getMessage()
                        : "Terjadi Kesalahan saat mengambil data";

                mainHandler.post(
                        () -> callback.onError(msg)
                );
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }
}
