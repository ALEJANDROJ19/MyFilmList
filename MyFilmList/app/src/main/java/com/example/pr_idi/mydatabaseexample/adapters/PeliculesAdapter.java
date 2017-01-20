package com.example.pr_idi.mydatabaseexample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.constants.DbBitmapUtility;
import com.example.pr_idi.mydatabaseexample.models.Film;

import java.util.LinkedList;
import java.util.List;


public class PeliculesAdapter extends RecyclerView.Adapter<PeliculesAdapter.ViewHolder> {

    //private Cursor pelicules;
    private List<Film> pelicules, allPelicules;

    private PeliculesAdapterItemListener observador;



    public interface PeliculesAdapterItemListener {
        void onClick(ViewHolder viewHolder, Film film);
        void onLongClick(ViewHolder viewHolder, Film film);
    }
    public PeliculesAdapter(PeliculesAdapterItemListener observador) {
        this.observador = observador;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        //elements UI
        public TextView titol, subtitol;
        public ImageView foto;
        public RatingBar puntuacio;

        public ViewHolder(View itemView) {
            super(itemView);
            titol = (TextView) itemView.findViewById(R.id.tv_titol);
            subtitol = (TextView) itemView.findViewById(R.id.tv_subtitol);
            foto = (ImageView) itemView.findViewById(R.id.iv_foto);
            puntuacio = (RatingBar) itemView.findViewById(R.id.rb_puntuacio);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //observador.onClick(this, getPeliculaId(getAdapterPosition()));
            observador.onClick(this, getPelicula(getAdapterPosition()));
        }


        @Override
        public boolean onLongClick(View view) {
            observador.onLongClick(this, getPelicula(getAdapterPosition()));
            return true;
        }
    }

    private int getPeliculaId(int adapterPosition) {
//        if (pelicules != null)
//            if (pelicules.moveToPosition(adapterPosition))
//                return pelicules.getInt(pelicules.getColumnIndex(MySQLiteHelper.COLUMN_ID));
//        return -1;
        if (pelicules != null)
            return (int) pelicules.get(adapterPosition).getId();
        return -1;
    }

    private Film getPelicula(int adapterPosition) {
        if (pelicules != null)
            return pelicules.get(adapterPosition);
        return null;
    }


    public PeliculesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pelicula, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PeliculesAdapter.ViewHolder holder, int position) {
        //pelicules.moveToPosition(position);
        Film film = pelicules.get(position);

//        holder.titol.setText(pelicules.get(pelicules.getColumnIndex(MySQLiteHelper.COLUMN_TITLE)));
//        holder.subtitol.setText(pelicules.getString(pelicules.getColumnIndex(MySQLiteHelper.COLUMN_YEAR_RELEASE)));
//        holder.foto.setImageBitmap(DbBitmapUtility.getImage(pelicules.getBlob(pelicules.getColumnIndex(MySQLiteHelper.COLUMN_COVER))));
//        holder.puntuacio.setRating(pelicules.getFloat(pelicules.getColumnIndex(MySQLiteHelper.COLUMN_CRITICS_RATE)));
        holder.titol.setText(film.getTitle());
        holder.subtitol.setText(String.valueOf(film.getYear())+" · "+film.getDirector());
        holder.foto.setImageBitmap(DbBitmapUtility.getImage(film.getCover()));
        holder.puntuacio.setRating(film.getCritics_rate());
    }

    @Override
    public int getItemCount() {
        if (pelicules != null)
            //return pelicules.getCount();
            return pelicules.size();
        return 0;
    }

    public void swapLlista(List<Film> novaLlista){
        if (novaLlista != null){
            pelicules = novaLlista;
            allPelicules = new LinkedList<>(novaLlista);
            notifyDataSetChanged();
        }
    }

    public void filter(String filter) {
        String text = filter.toLowerCase();
        pelicules.clear();
        if (text.isEmpty()){
            pelicules.addAll(allPelicules);
        } else {
            for(Film film : allPelicules){
                if (film.getTitle().toLowerCase().contains(text))
                    pelicules.add(film);
            }
        }
        notifyDataSetChanged();
    }

//    public void filterProtagonist(String filter) {
//        String text = filter.toLowerCase();
//        pelicules.clear();
//        if (text.isEmpty()){
//            pelicules.addAll(allPelicules);
//        } else {
//            for(Film film : allPelicules){
//                if (film.getProtagonist().toLowerCase().contains(text))
//                    pelicules.add(film);
//            }
//        }
//        notifyDataSetChanged();
//    }
//
//    public void filterDirector(String filter) {
//        String text = filter.toLowerCase();
//        pelicules.clear();
//        if (text.isEmpty()){
//            pelicules.addAll(allPelicules);
//        } else {
//            for(Film film : allPelicules){
//                if (film.getDirector().toLowerCase().contains(text))
//                    pelicules.add(film);
//            }
//        }
//        notifyDataSetChanged();
//    }
}
