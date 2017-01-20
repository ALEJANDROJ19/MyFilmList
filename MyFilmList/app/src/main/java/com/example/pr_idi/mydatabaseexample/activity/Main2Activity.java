package com.example.pr_idi.mydatabaseexample.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.adapters.PeliculesAdapter;
import com.example.pr_idi.mydatabaseexample.constants.Constants;
import com.example.pr_idi.mydatabaseexample.constants.Dialogs;
import com.example.pr_idi.mydatabaseexample.databases.FilmData;
import com.example.pr_idi.mydatabaseexample.models.Film;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    PeliculesAdapter.PeliculesAdapterItemListener, Dialogs.DialogListener {

    private RecyclerView llista;
    private LinearLayoutManager linearLayoutManager;
    private PeliculesAdapter adapter;
    private Dialogs dialogs = new Dialogs();
    private String actualSortOrder;
    private LinearLayout filter_bar;
    private TextView filter_bar_title;
    private SharedPreferences sharedPreferences;

    private FilmData filmData = new FilmData(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //TODO: Replace with my own action
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(Main2Activity.this, EditPeliculesActivity.class);
                intent.putExtra("requestCode", Constants.INTENT_CODE_PELICULA_NOVA);
                startActivityForResult(intent,Constants.INTENT_CODE_PELICULA_NOVA);
            }
        });

        //Nav
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Sorting rules
        actualSortOrder = FilmData.OrderBy.SQL_SELECT_ORDERBY_YEAR_ASC;

        //First Population
        sharedPreferences = getSharedPreferences("com.example.pr_idi.mydatabaseexample", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstrun", true)) {
            filmData.populateFirst(this);
            sharedPreferences.edit().putBoolean("firstrun", false).apply();
        }

        //RecyclerView
        llista = (RecyclerView) findViewById(R.id.llista_pelicules);
        llista.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        llista.setLayoutManager(linearLayoutManager);

        adapter = new PeliculesAdapter(this);
        adapter.swapLlista(filmData.getAllFilms(actualSortOrder));
        llista.setAdapter(adapter);

        dialogs.setObservador(this);

        //Filter bar
        filter_bar = (LinearLayout) findViewById(R.id.ly_filter_bar);
        filter_bar_title = (TextView) findViewById(R.id.tv_filter_title);
        ImageView filter_bar_button = (ImageView) findViewById(R.id.iv_filter_button_back);
        filter_bar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyAdaptador();
                filter_bar.setVisibility(LinearLayout.GONE);
            }
        });
        filter_bar.setVisibility(LinearLayout.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (filter_bar.getVisibility() == LinearLayout.VISIBLE) {
            notifyAdaptador();
            filter_bar.setVisibility(LinearLayout.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(Main2Activity.this,query,Toast.LENGTH_LONG).show();
                //adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }

        });



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            Dialogs.showSortDialog(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_titol) {
            filter_bar.setVisibility(LinearLayout.GONE);
            notifyAdaptador();
        } else if (id == R.id.nav_director) {
            Dialogs.showDirectorFilter(this,filmData.getAllDirectors(actualSortOrder));
        } else if (id == R.id.nav_protagonista) {
            Dialogs.showProtagonistFilter(this,filmData.getAllProtagonists(actualSortOrder));
        } else if (id == R.id.nav_faq) {
            Dialogs.showFAQDialog(this);
        } else if (id == R.id.nav_about) {
            Dialogs.showAboutDialog(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_CODE_PELICULA_NOVA) {
            if (resultCode == Constants.RETURN_OK) {
                Toast.makeText(this, "Pelicula guardada.", Toast.LENGTH_LONG).show();
                filmData.insertFilm((Film) data.getSerializableExtra("film"));
                notifyAdaptador();
            } else if (resultCode == Constants.RETURN_CANCEL) {
                Toast.makeText(this, "No s'ha guardat la película.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == Constants.INTENT_CODE_PELICULA_SELECTED) {
            if (resultCode == Constants.RETURN_OK) {
                Toast.makeText(this, "Pelicula guardada.", Toast.LENGTH_LONG).show();
                filmData.updateFilm((Film) data.getSerializableExtra("film"));
                notifyAdaptador();
            } else if (resultCode == Constants.RETURN_ERROR) {
                Toast.makeText(this, "S'ha produit un error.", Toast.LENGTH_LONG).show();
            } else if (resultCode == Constants.RETURN_DELETED) {
                Toast.makeText(this, "Pelicula eliminada.", Toast.LENGTH_LONG).show();
                filmData.deleteFilm((Film) data.getSerializableExtra("film"));
                notifyAdaptador();
            }
        }
    }

    @Override
    public void onClick(PeliculesAdapter.ViewHolder viewHolder, Film film) {
        Intent intent = new Intent(Main2Activity.this,PeliculesActivity.class);
        intent.putExtra("requestCode",Constants.INTENT_CODE_PELICULA_SELECTED);
        intent.putExtra("film",film);
        startActivityForResult(intent, Constants.INTENT_CODE_PELICULA_SELECTED);
    }

    @Override
    public void onLongClick(PeliculesAdapter.ViewHolder viewHolder, Film film) {
        Dialogs.showDeleteFilmDialog(this,film);
    }

    @Override
    public void onDeleteSelected(boolean doDelete, Film film) {
        if (doDelete) {
            filmData.deleteFilm(film);
            notifyAdaptador();
        }
    }

    @Override
    public void onFilterDirectorSelected(String director) {
        adapter.swapLlista(filmData.getAllFilmsFromDirector(actualSortOrder,director));
        filter_bar.setVisibility(LinearLayout.VISIBLE);
        filter_bar_title.setText(getString(R.string.director_title_prefix)+" "+director);
    }

    @Override
    public void onFilterProtagonistSelected(String protagonist) {
        adapter.swapLlista(filmData.getAllFilmsFromProtagonist(actualSortOrder,protagonist));
        filter_bar.setVisibility(LinearLayout.VISIBLE);
        filter_bar_title.setText(getString(R.string.protagonist_title_prefix)+" "+protagonist);
    }

    private void notifyAdaptador(){
        adapter.swapLlista(filmData.getAllFilms(actualSortOrder));
        //adapter.notifyDataSetChanged();
    }

    public void onSortSelected(boolean[] booleen){
        if (booleen[0]){
            //Titol
            if (booleen[3])
                actualSortOrder = FilmData.OrderBy.SQL_SELECT_ORDERBY_TITLE_DESC;
            else actualSortOrder = FilmData.OrderBy.SQL_SELECT_ORDERBY_TITLE_ASC;
        } else if (booleen[1]){
            //Any
            if (booleen[3])
                actualSortOrder = FilmData.OrderBy.SQL_SELECT_ORDERBY_YEAR_DESC;
            else actualSortOrder = FilmData.OrderBy.SQL_SELECT_ORDERBY_YEAR_ASC;
        } else if (booleen[2]){
            //puntuació
            if (booleen[3])
                actualSortOrder = FilmData.OrderBy.SQL_SELECT_ORDERBY_SCORE_DESC;
            else actualSortOrder = FilmData.OrderBy.SQL_SELECT_ORDERBY_SCORE_ASC;
        }
        notifyAdaptador();
    }

}
