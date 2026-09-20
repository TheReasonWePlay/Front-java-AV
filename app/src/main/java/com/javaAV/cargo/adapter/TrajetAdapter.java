package com.javaAV.cargo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.javaAV.cargo.R;
import com.javaAV.cargo.model.Trajet;

import java.util.List;
import java.util.Locale;

public class TrajetAdapter
        extends RecyclerView.Adapter<TrajetAdapter.TrajetViewHolder> {

    public interface OnReserverClickListener {
        void onReserverClick(Trajet trajet);
    }

    private final List<Trajet> trajets;
    private final OnReserverClickListener listener;

    public TrajetAdapter(
            List<Trajet> trajets,
            OnReserverClickListener listener
    ) {
        this.trajets = trajets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrajetViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_trajet,
                        parent,
                        false
                );

        return new TrajetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull TrajetViewHolder holder,
            int position
    ) {

        Trajet trajet =
                trajets.get(position);

        holder.textConducteur.setText(
                trajet.getConducteur()
        );

        holder.textDepart.setText(
                trajet.getDepart()
        );

        holder.textDestination.setText(
                trajet.getDestination()
        );

        holder.textPrix.setText(
                formaterPrix(
                        trajet.getPrix()
                )
        );

        if (trajet.getPlaces() <= 0) {
            holder.textPlaces.setText("Complet");
            holder.boutonReserver.setEnabled(false);
            holder.boutonReserver.setText("Complet");
        } else {
            holder.textPlaces.setText(
                    trajet.getPlaces() + " places disponibles"
            );

            holder.boutonReserver.setEnabled(true);
            holder.boutonReserver.setText("Réserver");
        }

        holder.textDateHeure.setText(
                formaterDateHeure(
                        trajet.getDate(),
                        trajet.getHeure()
                )
        );

        holder.textStatut.setText(
                formaterStatut(
                        trajet.getStatut()
                )
        );

        String conducteur =
                trajet.getConducteur();

        if (conducteur != null &&
                !conducteur.trim().isEmpty()) {

            holder.textAvatarConducteur.setText(
                    conducteur
                            .substring(0, 1)
                            .toUpperCase()
            );

        } else {

            holder.textAvatarConducteur.setText("?");

        }


        holder.boutonReserver.setOnClickListener(
                view -> {

                    if (listener != null) {
                        listener.onReserverClick(trajet);
                    }

                }
        );
    }

    @Override
    public int getItemCount() {
        return trajets.size();
    }

    private String formaterPrix(
            double prix
    ) {

        return String.format(
                Locale.FRANCE,
                "%,.0f Ar",
                prix
        );
    }

    private String formaterDateHeure(
            String date,
            String heure
    ) {

        if (date == null) {
            return "";
        }

        try {

            String[] morceaux =
                    date.split("-");

            String dateFormatee =
                    morceaux[2]
                            + "/"
                            + morceaux[1]
                            + "/"
                            + morceaux[0];

            String heureFormatee =
                    heure != null &&
                            heure.length() >= 5
                            ? heure.substring(0, 5)
                            : "";

            return "◷  "
                    + dateFormatee
                    + " à "
                    + heureFormatee;

        } catch (Exception e) {

            return date;
        }
    }

    private String formaterStatut(
            String statut
    ) {

        if (statut == null ||
                statut.trim().isEmpty()) {

            return "";
        }

        if ("EN_ATTENTE".equals(statut)) {
            return "EN ATTENTE";
        }

        if ("CONFIRMEE".equals(statut)) {
            return "CONFIRMÉ";
        }

        return statut.replace(
                "_",
                " "
        );
    }

    static class TrajetViewHolder
            extends RecyclerView.ViewHolder {

        TextView textAvatarConducteur;
        TextView textConducteur;
        TextView textStatut;
        TextView textDepart;
        TextView textDestination;
        TextView textDateHeure;
        TextView textPlaces;
        TextView textPrix;

        MaterialButton boutonReserver;

        TrajetViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            textAvatarConducteur =
                    itemView.findViewById(
                            R.id.textAvatarConducteur
                    );

            textConducteur =
                    itemView.findViewById(
                            R.id.textConducteur
                    );

            textStatut =
                    itemView.findViewById(
                            R.id.textStatut
                    );

            textDepart =
                    itemView.findViewById(
                            R.id.textDepart
                    );

            textDestination =
                    itemView.findViewById(
                            R.id.textDestination
                    );

            textDateHeure =
                    itemView.findViewById(
                            R.id.textDateHeure
                    );

            textPlaces =
                    itemView.findViewById(
                            R.id.textPlaces
                    );

            textPrix =
                    itemView.findViewById(
                            R.id.textPrix
                    );

            boutonReserver =
                    itemView.findViewById(
                            R.id.boutonReserver
                    );
        }
    }
}