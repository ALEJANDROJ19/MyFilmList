package com.example.pr_idi.mydatabaseexample.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pr_idi.mydatabaseexample.constants.DbBitmapUtility;
import com.example.pr_idi.mydatabaseexample.constants.Dialogs;
import com.example.pr_idi.mydatabaseexample.models.Film;
import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.constants.Constants;

public class PeliculesActivity extends AppCompatActivity implements Dialogs.DialogListenerDelete{
    private Film film;
    private boolean isEdited = false;

    private TextView titol, director, any, pais, protagonista, puntuacio_t;
    private ImageView foto;
    private RatingBar puntuacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicules);
        init();
    }

    private void init() {
        titol = (TextView) findViewById(R.id.tv_actvitypelicules_title);
        director = (TextView) findViewById(R.id.tv_actvitypelicules_director);
        any = (TextView) findViewById(R.id.tv_actvitypelicules_any);
        pais = (TextView) findViewById(R.id.tv_actvitypelicules_pais);
        protagonista = (TextView) findViewById(R.id.tv_actvitypelicules_protagonista);
        foto = (ImageView) findViewById(R.id.iv_actvitypelicules_foto);
        puntuacio = (RatingBar) findViewById(R.id.rb_actvitypelicules_puntuacio);
        puntuacio_t = (TextView) findViewById(R.id.tv_actvitypelicules_puntuacio);

        //Obtenir la pelicula del intent
        try {
            film = (Film) getIntent().getSerializableExtra("film");
        } catch (Exception e) {
            Log.e(Constants.ERROR_TAG,""+e.getMessage());
        }

        if (film != null){
            refreshFilmView();
        } else {
            //TODO: Error management
            setResult(Constants.RETURN_ERROR);
            finish();
        }

        puntuacio.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean isFromUser) {
                film.setCritics_rate(rating); //Useless now (?)
                puntuacio_t.setText(String.valueOf((int) (film.getCritics_rate()*2.0)));
                isEdited = true;
            }
        });

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pelicules);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.button_material_light));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_pelicules_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (isEdited) {
            saveChanges();
            intent.putExtra("film",film);
            setResult(Constants.RETURN_OK,intent);
        } else {
            setResult(Constants.RETURN_CANCEL);
        }
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        } else if (item.getItemId() == R.id.action_edit){
            Intent intent = new Intent(PeliculesActivity.this, EditPeliculesActivity.class);
            intent.putExtra("film",film);
            intent.putExtra("requestCode",Constants.INTENT_CODE_PELICULA_SELECTED);
            startActivityForResult(intent,Constants.INTENT_CODE_PELICULA_SELECTED);
        } else if (item.getItemId() == R.id.action_delete){
            Dialogs.showDeleteFilmDialogOnFilm(this, this, film);
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveChanges() {
        film.setTitle(titol.getText().toString());
        film.setDirector(director.getText().toString());
        film.setYear(Integer.decode(any.getText().toString()));
        film.setCountry(pais.getText().toString());
        film.setProtagonist(protagonista.getText().toString());
        film.setCover(DbBitmapUtility.getBytes(((BitmapDrawable) foto.getDrawable()).getBitmap()));
        film.setCritics_rate(puntuacio.getRating());
    }

    private void refreshFilmView() {
        titol.setText(film.getTitle());
        director.setText(film.getDirector());
        any.setText(String.valueOf(film.getYear()));
        pais.setText(film.getCountry());
        protagonista.setText(film.getProtagonist());
        foto.setImageBitmap(DbBitmapUtility.getImage(film.getCover()));
        puntuacio.setRating(film.getCritics_rate());
        puntuacio_t.setText(String.valueOf((int) (film.getCritics_rate()*2.0)));
    }


    @Override
    public void onDeleteSelected(boolean doDelete, Film film) {
        if (doDelete) {
            //The bomb has been planted!
            Intent intent = new Intent().putExtra("film",film);
            setResult(Constants.RETURN_DELETED,intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_CODE_PELICULA_SELECTED){
            if (resultCode == Constants.RETURN_OK) {
                //Let's update the data! Raising Heart! Set-Up!
                this.film = (Film) data.getSerializableExtra("film");
                refreshFilmView();
                isEdited = true;
            } else if (resultCode == Constants.RETURN_CANCEL) {
                //Nothing changes... only the time remains
            }
        }
    }
}
