package br.edu.ifspsaocarlos.sdm.asynctaskmoviedb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.asynctaskmoviedb.rest.model.Movie;

public class MoviesFragment extends Fragment {

    private MoviesAdapter moviesAdapter;
    private final String URL_TO_HIT = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=58ad9c96d75af0ffe07202e302e525da";

    public MoviesFragment() {
    }

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.grid_movies);
        moviesAdapter = new MoviesAdapter(getContext());
        gridView.setAdapter(moviesAdapter);

        //Conf. o displayImage com as opções default.
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // carregar ImageLoader na criação da app


        //Começa a buscar os dados, inicia a tarefa assíncrona.
        new JSONTask().execute(URL_TO_HIT);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateMovies();
    }

    private void updateMovies() {
        // atualizar a view, sem uso até o momento

    }


    public class JSONTask extends AsyncTask<String,String, List<Movie>> {

        private List<Movie> listMovie;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //se tivesse um ProgressBar fazer a chamada nesse método
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }


                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("results");

                listMovie = new ArrayList<Movie>();

                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++) {
                    //converter o objeto JSON para objeto Java
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Movie movie = gson.fromJson(finalObject.toString(), Movie.class);
                    listMovie.add(movie);
                }
                return listMovie;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }

        @Override
        protected void onPostExecute(final List<Movie> result) {
            super.onPostExecute(result);
            //finalizar o ProgressBar e apresentar o resultado na tela
            if(result != null) {
                moviesAdapter.clear();
                moviesAdapter.addAll(result);
                //aqui pode ser implementado OnItemClickListener() ... e novas intents
            } else {
                moviesAdapter.clear();
                Toast.makeText(getContext(), "Não está habilitado a buscar dados do servidor, verifique a url.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
