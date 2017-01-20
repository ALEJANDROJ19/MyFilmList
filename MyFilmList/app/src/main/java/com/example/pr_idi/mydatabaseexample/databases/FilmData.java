package com.example.pr_idi.mydatabaseexample.databases;

/**
 * FilmData
 * Created by pr_idi on 10/11/16.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.constants.DbBitmapUtility;
import com.example.pr_idi.mydatabaseexample.models.Film;

public class FilmData {

    // Database fields
    private MySQLiteHelper dbHelper;
    private static final String TAG = "FilmData";

    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
                                    MySQLiteHelper.COLUMN_TITLE,
                                    MySQLiteHelper.COLUMN_DIRECTOR,
                                    MySQLiteHelper.COLUMN_YEAR_RELEASE,
                                    MySQLiteHelper.COLUMN_COUNTRY,
                                    MySQLiteHelper.COLUMN_PROTAGONIST,
                                    MySQLiteHelper.COLUMN_COVER
                                };

    public FilmData(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void insertFilm(Film film) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_TITLE,film.getTitle());
            values.put(MySQLiteHelper.COLUMN_DIRECTOR,film.getDirector());
            values.put(MySQLiteHelper.COLUMN_YEAR_RELEASE,film.getYear());
            values.put(MySQLiteHelper.COLUMN_COUNTRY,film.getCountry());
            values.put(MySQLiteHelper.COLUMN_CRITICS_RATE,film.getCritics_rate());
            values.put(MySQLiteHelper.COLUMN_PROTAGONIST,film.getProtagonist());
            values.put(MySQLiteHelper.COLUMN_COVER,film.getCover());

            db.insertOrThrow(MySQLiteHelper.TABLE_FILMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error intentant inserir una pelicula a la base de dades");
            Log.d(TAG, ""+e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void updateFilm(Film film) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_TITLE,film.getTitle());
            values.put(MySQLiteHelper.COLUMN_DIRECTOR,film.getDirector());
            values.put(MySQLiteHelper.COLUMN_YEAR_RELEASE,film.getYear());
            values.put(MySQLiteHelper.COLUMN_COUNTRY,film.getCountry());
            values.put(MySQLiteHelper.COLUMN_CRITICS_RATE,film.getCritics_rate());
            values.put(MySQLiteHelper.COLUMN_PROTAGONIST,film.getProtagonist());
            values.put(MySQLiteHelper.COLUMN_COVER,film.getCover());

            db.update(MySQLiteHelper.TABLE_FILMS, values,
                    MySQLiteHelper.COLUMN_ID+" = "+String.valueOf(film.getId()),null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error intentant actualitzar una pelicula a la base de dades");
            Log.d(TAG, ""+e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void deleteFilm(Film film) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            int deletedElements = db.delete(MySQLiteHelper.TABLE_FILMS, MySQLiteHelper.COLUMN_ID
                    + " = " + String.valueOf(film.getId()), null);
            if (deletedElements >1)
                Log.d(TAG,"Deleted "+ String.valueOf(deletedElements)+", expected one!");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error intentant elimnar una pelicula a la base de dades");
            Log.d(TAG, ""+e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public List<Film> getAllFilms(String orderBy) {
        List<Film> films = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String SQL_SELECT_QUERRY = String.format("SELECT * FROM %s %s", MySQLiteHelper.TABLE_FILMS, orderBy);
        Cursor cursor = db.rawQuery(SQL_SELECT_QUERRY,null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    films.add(cursorToFilm(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, "Error intentant obtenir les pelicules de la base de dades");
            Log.d(TAG, ""+e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return films;
    }

    public List<Film> getAllFilmsFromDirector(String orderBy, String director) {
        List<Film> films = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String SQL_SELECT_QUERRY = String.format("SELECT * FROM %s WHERE %s = \"%s\" %s",
                MySQLiteHelper.TABLE_FILMS, MySQLiteHelper.COLUMN_DIRECTOR, director, orderBy);
        Cursor cursor = db.rawQuery(SQL_SELECT_QUERRY,null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    films.add(cursorToFilm(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, "Error intentant obtenir les pelicules de la base de dades");
            Log.d(TAG, ""+e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return films;
    }

    public List<Film> getAllFilmsFromProtagonist(String orderBy, String protagonist) {
        List<Film> films = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String SQL_SELECT_QUERRY = String.format("SELECT * FROM %s WHERE %s = \"%s\" %s",
                MySQLiteHelper.TABLE_FILMS, MySQLiteHelper.COLUMN_PROTAGONIST, protagonist, orderBy);
        Cursor cursor = db.rawQuery(SQL_SELECT_QUERRY,null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    films.add(cursorToFilm(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, "Error intentant obtenir les pelicules de la base de dades");
            Log.d(TAG, ""+e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return films;
    }

    public List<String> getAllProtagonists(String orderBy) {
        List<String> protagonists = new LinkedList<>();

        List<Film> films = getAllFilms(orderBy);
        for (Film film : films){
            if (!protagonists.contains(film.getProtagonist()))
            protagonists.add(film.getProtagonist());
        }

        return protagonists;
    }

    public List<String> getAllDirectors(String orderBy) {
        List<String> directors = new LinkedList<>();

        List<Film> films = getAllFilms(orderBy);
        for (Film film : films){
            if (!directors.contains(film.getDirector()))
                directors.add(film.getDirector());
        }

        return directors;
    }

    private Film cursorToFilm(Cursor cursor) {
        Film film = new Film();
        film.setId(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID)));
        film.setTitle(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TITLE)));
        film.setDirector(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DIRECTOR)));
        film.setCountry(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_COUNTRY)));
        film.setYear(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_YEAR_RELEASE)));
        film.setProtagonist(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_PROTAGONIST)));
        film.setCritics_rate(cursor.getFloat(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CRITICS_RATE)));
        film.setCover(cursor.getBlob(cursor.getColumnIndex(MySQLiteHelper.COLUMN_COVER)));
        return film;
    }

    public class OrderBy {
        public static final String SQL_SELECT_ORDERBY_TITLE_ASC  = "ORDER BY " + MySQLiteHelper.COLUMN_TITLE + " ASC";
        public static final String SQL_SELECT_ORDERBY_TITLE_DESC = "ORDER BY " + MySQLiteHelper.COLUMN_TITLE + " DESC";

        public static final String SQL_SELECT_ORDERBY_YEAR_ASC  = "ORDER BY " + MySQLiteHelper.COLUMN_YEAR_RELEASE + " ASC";
        public static final String SQL_SELECT_ORDERBY_YEAR_DESC = "ORDER BY " + MySQLiteHelper.COLUMN_YEAR_RELEASE + " DESC";

        public static final String SQL_SELECT_ORDERBY_SCORE_ASC  = "ORDER BY " + MySQLiteHelper.COLUMN_CRITICS_RATE + " ASC";
        public static final String SQL_SELECT_ORDERBY_SCORE_DESC = "ORDER BY " + MySQLiteHelper.COLUMN_CRITICS_RATE + " DESC";
    }

    public void populateFirst(Activity activity) {
        //We need films to start!
        //Nanoha Reflection
        Film film = new Film();
        film.setTitle("Mahou Shoujo Lyrical Nanoha Reflection");
        film.setDirector("Takayuki Hamana");
        film.setCritics_rate(5.0f);
        film.setProtagonist("Nanoha Takamachi");
        film.setYear(2017);
        film.setCountry("Japó");
        film.setCover(DbBitmapUtility.getBytes(
                DbBitmapUtility.scaleDownBitmap(
                        BitmapFactory.decodeResource(
                                activity.getResources(), R.drawable.nanoha_cover) , 200, activity )));
        insertFilm(film);

        //Nanoha 1
        film = new Film();
        film.setTitle("Mahou Shoujo Lyrical Nanoha: The Movie 1st");
        film.setDirector("Atsushi Nakayama");
        film.setCritics_rate(4.5f);
        film.setProtagonist("Nanoha Takamachi");
        film.setYear(2010);
        film.setCountry("Japó");
        film.setCover(DbBitmapUtility.getBytes(
                DbBitmapUtility.scaleDownBitmap(
                        BitmapFactory.decodeResource(
                                activity.getResources(), R.drawable.nanoha_cover2) , 200, activity )));
        insertFilm(film);

        //Nanoha 2
        film = new Film();
        film.setTitle("Mahou Shoujo Lyrical Nanoha: The Movie 2nd A's");
        film.setDirector("Keizou Kusakawa");
        film.setCritics_rate(5.0f);
        film.setProtagonist("Hayate Yagami");
        film.setYear(2012);
        film.setCountry("Japó");
        film.setCover(DbBitmapUtility.getBytes(
                DbBitmapUtility.scaleDownBitmap(
                        BitmapFactory.decodeResource(
                                activity.getResources(), R.drawable.nanoha_cover3) , 200, activity )));
        insertFilm(film);

        //La ventana indiscreta
        film = new Film();
        film.setTitle("Rear Window");
        film.setDirector("Alfred Hitchcock");
        film.setCritics_rate(5.0f);
        film.setProtagonist("James Stewart");
        film.setYear(1954);
        film.setCountry("EUA");
        film.setCover(DbBitmapUtility.getBytes(
                DbBitmapUtility.scaleDownBitmap(
                        BitmapFactory.decodeResource(
                                activity.getResources(), R.drawable.rear_window) , 200, activity )));
        insertFilm(film);

        //Cinema Paradiso
        film = new Film();
        film.setTitle("Nuovo Cinema Paradiso");
        film.setDirector("Giuseppe Tornatore");
        film.setCritics_rate(5.0f);
        film.setProtagonist("Philippe Noiret");
        film.setYear(1988);
        film.setCountry("Italia");
        film.setCover(DbBitmapUtility.getBytes(
                DbBitmapUtility.scaleDownBitmap(
                        BitmapFactory.decodeResource(
                                activity.getResources(), R.drawable.nuovo_cinema_paradiso) , 200, activity )));
        insertFilm(film);

        //Psycho
        film = new Film();
        film.setTitle("Psycho");
        film.setDirector("Alfred Hitchcock");
        film.setCritics_rate(4.5f);
        film.setProtagonist("Anthony Perkins");
        film.setYear(1960);
        film.setCountry("EUA");
        film.setCover(DbBitmapUtility.getBytes(
                DbBitmapUtility.scaleDownBitmap(
                        BitmapFactory.decodeResource(
                                activity.getResources(), R.drawable.psycho) , 200, activity )));
        insertFilm(film);

        //Rogue One
        film = new Film();
        film.setTitle("Rogue One: A Star Wars Story");
        film.setDirector("Gareth Edwards");
        film.setCritics_rate(4.0f);
        film.setProtagonist("Felicity Jones");
        film.setYear(2016);
        film.setCountry("EUA");
        film.setCover(DbBitmapUtility.getBytes(
                DbBitmapUtility.scaleDownBitmap(
                        BitmapFactory.decodeResource(
                                activity.getResources(), R.drawable.rogue_one) , 200, activity )));
        insertFilm(film);
    }

}