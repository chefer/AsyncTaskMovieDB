package br.edu.ifspsaocarlos.sdm.asynctaskmoviedb;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import br.edu.ifspsaocarlos.sdm.asynctaskmoviedb.rest.model.Movie;

public class MoviesAdapter extends ArrayAdapter<Movie> {

    public MoviesAdapter(Context context) {
        super(context, R.layout.grid_item_movie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        Movie movie = getItem(position);

        TextView titleView = (TextView) convertView.findViewById(R.id.movieTitle);
        titleView.setText(movie.getTitle());

        String imageUrl = getContext().getString(R.string.tmdb_image_base_url) + movie.getPosterPath();
        ImageView imageView = (ImageView) convertView.findViewById(R.id.moviePoster);

        // se quiser podemos usar o picasso para recuperar as imagens tbm ai é só comentar o ImageLoader abaixo.
        //  Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.ic_photo).error(R.drawable.ic_error).into(imageView);

        //Então agora, usamos o ImageLoader conf. la no onCreateView() para a imagem
        ImageLoader.getInstance().displayImage(imageUrl, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                //Iniciar ProgressBar
                //Toast.makeText(getContext(), "Início do Download das imagens", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                //finalizar ProgressBar
                //Toast.makeText(getContext(), "Falha do Download das imagens", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //finalizar ProgressBar
                //Toast.makeText(getContext(), "Completo o Download das imagens", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                //finalizar ProgressBar
                //Toast.makeText(getContext(), "Cancelado o Download das imagens", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}