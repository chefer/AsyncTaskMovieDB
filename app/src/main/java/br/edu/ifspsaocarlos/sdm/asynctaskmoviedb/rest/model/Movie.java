package br.edu.ifspsaocarlos.sdm.asynctaskmoviedb.rest.model;

public class Movie {
    private String title;
    private String poster_path;


    public Movie(String title, String poster_path) {
        this.title = title;
        this.poster_path = poster_path;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getTitle() {
        return title;
    }
}
