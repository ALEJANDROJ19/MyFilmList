package com.example.pr_idi.mydatabaseexample.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.constants.Constants;
import com.example.pr_idi.mydatabaseexample.constants.DbBitmapUtility;
import com.example.pr_idi.mydatabaseexample.models.Film;

public class EditPeliculesActivity extends AppCompatActivity {

    private ImageView foto;
    private Button img_dafault;
    private EditText titol, director, any, pais, protagonista;
    private TextView err_titol, err_director, err_any, err_pais, err_protagonista;

    private Film film;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pelicules);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_edit_pelicules_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        titol = (EditText) findViewById(R.id.et_actvityeditpelicules_titol);
        director = (EditText) findViewById(R.id.et_actvityeditpelicules_director);
        any = (EditText) findViewById(R.id.et_actvityeditpelicules_any);
        pais = (EditText) findViewById(R.id.et_actvityeditpelicules_pais);
        protagonista = (EditText) findViewById(R.id.et_actvityeditpelicules_protagonista);
        img_dafault = (Button) findViewById(R.id.bt_actvityeditpelicules_def);
        foto = (ImageView) findViewById(R.id.iv_actvityeditpelicules_foto);
        err_titol = (TextView) findViewById(R.id.error_titol);
        err_director = (TextView) findViewById(R.id.error_director);
        err_any = (TextView) findViewById(R.id.error_any);
        err_pais = (TextView) findViewById(R.id.error_pais);
        err_protagonista = (TextView) findViewById(R.id.error_protagonista);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constants.RESULT_LOAD_IMAGE);
            }
        });

        img_dafault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                foto.setImageDrawable(getResources().getDrawable(R.drawable.ic_foto,getTheme()));
                foto.setImageDrawable(getResources().getDrawable(R.drawable.ic_foto));
            }
        });

        titol.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                err_titol.setVisibility(TextView.GONE);
                return false;
            }
        });

        director.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                err_director.setVisibility(TextView.GONE);
                return false;
            }
        });

        any.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                err_any.setVisibility(TextView.GONE);
                return false;
            }
        });

        pais.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                err_pais.setVisibility(TextView.GONE);
                return false;
            }
        });

        protagonista.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                err_protagonista.setVisibility(TextView.GONE);
                return false;
            }
        });

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_pelicules);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.button_material_light));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Obtenir la pelicula del intent
        try {
            if(getIntent().getExtras().getInt("requestCode") == Constants.INTENT_CODE_PELICULA_NOVA){
                film = new Film();
            } else if (getIntent().getExtras().getInt("requestCode") == Constants.INTENT_CODE_PELICULA_SELECTED) {
                film = (Film) getIntent().getSerializableExtra("film");
                loadFields();
            }
        } catch (Exception e) {
            Log.e(Constants.ERROR_TAG,""+e.getMessage());
        }
    }

    private void loadFields() {
        titol.setText(film.getTitle());
        director.setText(film.getDirector());
        any.setText(String.valueOf(film.getYear()));
        pais.setText(film.getCountry());
        protagonista.setText(film.getProtagonist());
        foto.setImageBitmap(DbBitmapUtility.getImage(film.getCover()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Cancel current edit.
            //super.onBackPressed();
            setResult(Constants.RETURN_CANCEL);
            finish();
        } else if (item.getItemId() == R.id.action_accept) {
            //Save to the database the film
            //First, perform a check. All fields must be filled.
            if (isEmptyFields()){
                //Put in red the fields that the user must fill:
                emptyFieldsToRed();
            } else {
                //All correct! Let's save the film!
                save_film();
                Intent intent = new Intent().putExtra("film",film);
                setResult(Constants.RETURN_OK,intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void save_film() {
        film.setTitle(titol.getText().toString());
        film.setDirector(director.getText().toString());
        film.setYear(Integer.decode(any.getText().toString()));
        film.setCountry(pais.getText().toString());
        film.setProtagonist(protagonista.getText().toString());
            //film.setCover(DbBitmapUtility.getBytes(DbBitmapUtility.scaleDownBitmap( ((BitmapDrawable) foto.getDrawable()).getBitmap(), 200, this )));
        film.setCover(DbBitmapUtility.getBytes(((BitmapDrawable) foto.getDrawable()).getBitmap()));
    }

    private boolean isEmptyFields() {
        return titol.getText().toString().isEmpty() || director.getText().toString().isEmpty() ||
                any.getText().toString().isEmpty() || pais.getText().toString().isEmpty() ||
                protagonista.getText().toString().isEmpty();
    }

    private void emptyFieldsToRed() {
        if (titol.getText().toString().isEmpty())
            err_titol.setVisibility(TextView.VISIBLE);
        if (director.getText().toString().isEmpty())
            err_director.setVisibility(TextView.VISIBLE);
        if (any.getText().toString().isEmpty())
            err_any.setVisibility(TextView.VISIBLE);
        if (pais.getText().toString().isEmpty())
            err_pais.setVisibility(TextView.VISIBLE);
        if (protagonista.getText().toString().isEmpty())
            err_protagonista.setVisibility(TextView.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //foto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            foto.setImageBitmap(DbBitmapUtility.scaleDownBitmap( BitmapFactory.decodeFile(picturePath), 200, this ));
        }
    }
}
