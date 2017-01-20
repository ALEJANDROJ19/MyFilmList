package com.example.pr_idi.mydatabaseexample.constants;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.example.pr_idi.mydatabaseexample.R;
import com.example.pr_idi.mydatabaseexample.models.Film;

import java.lang.reflect.Array;
import java.util.List;


public class Dialogs {

    private static DialogListener observador;

    public static void showFAQDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.faqDialogMessage)).setTitle(R.string.faqDialogTitle);
        builder.setIcon(R.drawable.ic_help);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface DialogListener {
        void onSortSelected(boolean[] booleen);
        void onDeleteSelected(boolean doDelete, Film film);
        void onFilterDirectorSelected(String director);
        void onFilterProtagonistSelected(String protagonist);

    }
    public interface DialogListenerDelete {
        void onDeleteSelected(boolean doDelete, Film film);
    }
    public void setObservador(DialogListener observador) {this.observador = observador;}

    private static final boolean[] sort_checked_items = {false,true,false,false};
    public static void showSortDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setTitle("Ordenar películes");
        dialog.setContentView(R.layout.layout_sort);
        final RadioButton titol = (RadioButton) dialog.findViewById(R.id.rb_sort_title);
        final RadioButton any = (RadioButton) dialog.findViewById(R.id.rb_sort_any);
        final RadioButton puntuacio = (RadioButton) dialog.findViewById(R.id.rb_sort_puntuacio);
        final Button ok = (Button) dialog.findViewById(R.id.bt_sort_ok);
        final CheckBox invers = (CheckBox) dialog.findViewById(R.id.cb_sort_inverse);
        titol.setChecked(sort_checked_items[0]);
        any.setChecked(sort_checked_items[1]);
        puntuacio.setChecked(sort_checked_items[2]);
        invers.setChecked(sort_checked_items[3]);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_checked_items[0] = titol.isChecked();
                sort_checked_items[1] = any.isChecked();
                sort_checked_items[2] = puntuacio.isChecked();
                sort_checked_items[3] = invers.isChecked();
                if (observador != null)
                    observador.onSortSelected(sort_checked_items);
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static void showAboutDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.aboutDialogMessage)+"\n—\nAlejandro Jurnet Bolarin\nalejandro.jurnet@estudiant.upc.edu\n16/2017 EPSEVG").setTitle(R.string.aboutDialogTitle);
        builder.setIcon(R.drawable.ic_about);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showDeleteFilmDialog(Activity activity, final Film film) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.delete_film_dialog));
        builder.setIcon(R.drawable.ic_delete_red);
        builder.setPositiveButton(activity.getString(R.string.afirmative_delete_option), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                observador.onDeleteSelected(true,film);
            }
        });
        builder.setNegativeButton(activity.getString(R.string.negative_delete_option), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                observador.onDeleteSelected(false,film);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showDeleteFilmDialogOnFilm(Activity activity, final DialogListenerDelete observador, final Film film) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.delete_film_dialog));
        builder.setIcon(R.drawable.ic_delete_red);
        builder.setPositiveButton(activity.getString(R.string.afirmative_delete_option), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                observador.onDeleteSelected(true,film);
            }
        });
        builder.setNegativeButton(activity.getString(R.string.negative_delete_option), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                observador.onDeleteSelected(false,film);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showProtagonistFilter(Activity activity, final List<String> protagonists) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.dialog_protagonist_filter_title));

        builder.setItems(protagonists.toArray(new CharSequence[protagonists.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                observador.onFilterProtagonistSelected(protagonists.get(i));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showDirectorFilter(Activity activity, final List<String> directors) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.dialog_protagonist_filter_title));

        builder.setItems(directors.toArray(new CharSequence[directors.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                observador.onFilterDirectorSelected(directors.get(i));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }






}
