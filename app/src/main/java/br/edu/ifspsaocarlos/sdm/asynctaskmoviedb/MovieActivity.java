package br.edu.ifspsaocarlos.sdm.asynctaskmoviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MoviesFragment.newInstance())
                .commit();
    }
}
