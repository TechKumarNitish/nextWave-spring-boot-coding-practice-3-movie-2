/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 *
 */

package com.example.movie.service;

import com.example.movie.model.Movie;
import com.example.movie.repository.MovieRepository;
import com.example.movie.model.MovieRowMapper;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;

@Service
public class MovieH2Service implements MovieRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Movie> getMovies() {
        List<Movie> allMovies = db.query("select * from movielist", new MovieRowMapper());

        ArrayList<Movie> movies = new ArrayList<>(allMovies);
        return movies;
    }

    @Override
    public Movie addMovie(Movie movie) {
        db.update("insert into movielist(moviename, leadactor) values(?, ?)", movie.getMovieName(),
                movie.getLeadActor());
        Movie savedMovie = db.queryForObject("select * from movielist where moviename = ? and leadActor = ?",
                new MovieRowMapper(), movie.getMovieName(), movie.getLeadActor());
        return savedMovie;

    }

    @Override
    public Movie getMovieById(int movieId) {
        try {
            Movie movie = db.queryForObject("select * from movielist where movieid = ?", new MovieRowMapper(), movieId);
            return movie;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Movie updateMovie(int movieId, Movie movie) {
        if (movie.getMovieName() != null)
            db.update("update movielist set moviename = ? where movieid = ?", movie.getMovieName(), movieId);
        if (movie.getLeadActor() != null)
            db.update("update movielist set leadactor = ? where movieid = ?", movie.getLeadActor(), movieId);
        return getMovieById(movieId);

    }

    @Override
    public void deleteMovie(int movieId) {
        db.update("delete from movielist where movieid = ?", movieId);
    }

}