package com.javaAV.cargo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import com.javaAV.cargo.model.Reservation;
import com.javaAV.cargo.model.Trajet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private String nomUtilisateur;

    private TextView textBonjour;
    private TextView textAvatar;

    private MaterialCardView cardRecherche;
    private MaterialCardView cardProposer;

    private BottomNavigationView navigation;

    private TextView textDestination1;
    private TextView textDepart1;
    private TextView textPrix1;
    private TextView textDate1;
    private TextView textAvatar1;
    private TextView textConducteur1;

    private MaterialCardView cardTrajet1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ==========================================
        // RÉCUPÉRATION DU NOM
        // ==========================================

        nomUtilisateur = getIntent()
                .getStringExtra("nom");

        if (nomUtilisateur == null ||
                nomUtilisateur.trim().isEmpty()) {

            nomUtilisateur = "Utilisateur";
        }

        // ==========================================
        // CHARGEMENT DE L'INTERFACE XML
        // ==========================================

        setContentView(R.layout.activity_home);

        initialiserVues();

        initialiserInterface();

        initialiserActions();

        initialiserNavigation();

        chargerReservations();
    }


    // =====================================================
    // INITIALISATION DES VUES
    // =====================================================

    private void initialiserVues() {

        textBonjour = findViewById(
                R.id.textBonjour
        );

        textAvatar = findViewById(
                R.id.textAvatar
        );

        cardRecherche = findViewById(
                R.id.cardRecherche
        );

        cardProposer = findViewById(
                R.id.cardProposer
        );

        navigation = findViewById(
                R.id.navigation
        );

        cardTrajet1 = findViewById(R.id.cardTrajet1);

        textDestination1 =
                findViewById(R.id.textDestination1);

        textDepart1 =
                findViewById(R.id.textDepart1);

        textPrix1 =
                findViewById(R.id.textPrix1);

        textDate1 =
                findViewById(R.id.textDate1);

        textAvatar1 =
                findViewById(R.id.textAvatar1);

        textConducteur1 =
                findViewById(R.id.textConducteur1);
    }


    // =====================================================
    // INITIALISATION INTERFACE
    // =====================================================

    private void initialiserInterface() {

        textBonjour.setText(
                "Bonjour, " + nomUtilisateur + ""
        );

        String initiale =
                nomUtilisateur
                        .substring(0, 1)
                        .toUpperCase();

        textAvatar.setText(initiale);
    }


    // =====================================================
    // ACTIONS
    // =====================================================

    private void initialiserActions() {

        // -----------------------------------------
        // RECHERCHER UN TRAJET
        // -----------------------------------------

        cardRecherche.setOnClickListener(view -> {

            Toast.makeText(
                    this,
                    "Recherche de trajet",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // -----------------------------------------
        // PROPOSER UN TRAJET
        // -----------------------------------------

        cardProposer.setOnClickListener(view -> {

            Toast.makeText(
                    this,
                    "Proposition de trajet",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }


    // =====================================================
    // NAVIGATION BASSE
    // =====================================================

    private void initialiserNavigation() {

        // Accueil est actuellement actif
        navigation.setSelectedItemId(
                R.id.nav_accueil
        );

        navigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            // ==========================================
            // ACCUEIL
            // ==========================================

            if (id == R.id.nav_accueil) {

                return true;
            }


            // ==========================================
            // TRAJETS
            // ==========================================

            if (id == R.id.nav_trajets) {

                Toast.makeText(
                        this,
                        "Trajets prochainement",
                        Toast.LENGTH_SHORT
                ).show();

                return true;
            }


            // ==========================================
            // MESSAGES
            // ==========================================

            if (id == R.id.nav_messages) {

                Toast.makeText(
                        this,
                        "Messagerie prochainement",
                        Toast.LENGTH_SHORT
                ).show();

                return true;
            }


            // ==========================================
            // RÉSERVATIONS
            // ==========================================

            if (id == R.id.nav_reservations) {

                Toast.makeText(
                        this,
                        "Réservations prochainement",
                        Toast.LENGTH_SHORT
                ).show();

                return true;
            }


            // ==========================================
            // PROFIL
            // ==========================================

            if (id == R.id.nav_profil) {

                Intent intent =
                        new Intent(
                                HomeActivity.this,
                                ProfilActivity.class
                        );

                intent.putExtra(
                        "nom",
                        nomUtilisateur
                );

                startActivity(intent);

                return true;
            }

            return false;
        });
    }
    private void chargerReservations() {

        Log.d(
                "API_RESERVATIONS",
                "========================================"
        );

        Log.d(
                "API_RESERVATIONS",
                ">>> Début chargement des réservations"
        );

        Log.d(
                "API_RESERVATIONS",
                "Utilisateur connecté : " + nomUtilisateur
        );

        ExecutorService executor =
                Executors.newSingleThreadExecutor();

        Handler handler =
                new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            HttpURLConnection connexion = null;

            try {

                // ==========================================
                // NOM UTILISATEUR
                // ==========================================

                String nomEncode =
                        URLEncoder.encode(
                                nomUtilisateur,
                                "UTF-8"
                        );

                Log.d(
                        "API_RESERVATIONS",
                        "Nom encodé : " + nomEncode
                );


                // ==========================================
                // URL
                // ==========================================

                String urlString =
                        Config.API_BASE_URL
                                + "reservations/passager/"
                                + nomEncode;

                Log.d(
                        "API_RESERVATIONS",
                        "URL API : " + urlString
                );


                URL url =
                        new URL(urlString);

                Log.d(
                        "API_RESERVATIONS",
                        "URL créée avec succès"
                );


                // ==========================================
                // CONNEXION
                // ==========================================

                connexion =
                        (HttpURLConnection)
                                url.openConnection();

                Log.d(
                        "API_RESERVATIONS",
                        "Connexion HTTP ouverte"
                );

                connexion.setRequestMethod("GET");

                connexion.setConnectTimeout(5000);

                connexion.setReadTimeout(5000);

                connexion.setRequestProperty(
                        "Accept",
                        "application/json"
                );

                Log.d(
                        "API_RESERVATIONS",
                        "Paramètres HTTP configurés"
                );


                // ==========================================
                // CODE HTTP
                // ==========================================

                Log.d(
                        "API_RESERVATIONS",
                        ">>> Envoi de la requête GET..."
                );

                int code =
                        connexion.getResponseCode();

                Log.d(
                        "API_RESERVATIONS",
                        "<<< Code HTTP reçu : " + code
                );


                if (code != HttpURLConnection.HTTP_OK) {

                    Log.e(
                            "API_RESERVATIONS",
                            "ERREUR HTTP : " + code
                    );

                    throw new Exception(
                            "HTTP " + code
                    );
                }


                // ==========================================
                // LECTURE REPONSE
                // ==========================================

                Log.d(
                        "API_RESERVATIONS",
                        "Lecture de la réponse..."
                );

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        connexion.getInputStream()
                                )
                        );

                StringBuilder resultat =
                        new StringBuilder();

                String ligne;

                while ((ligne = reader.readLine()) != null) {

                    resultat.append(ligne);
                }

                reader.close();


                // ==========================================
                // REPONSE BRUTE
                // ==========================================

                String json =
                        resultat.toString();

                Log.d(
                        "API_RESERVATIONS",
                        "Réponse API reçue : " + json
                );

                Log.d(
                        "API_RESERVATIONS",
                        "Taille réponse : "
                                + json.length()
                                + " caractères"
                );


                // ==========================================
                // PARSING JSON
                // ==========================================

                Log.d(
                        "API_RESERVATIONS",
                        ">>> Analyse du JSON..."
                );

                List<Reservation> reservations =
                        analyserReservations(json);

                Log.d(
                        "API_RESERVATIONS",
                        "<<< JSON analysé avec succès"
                );

                Log.d(
                        "API_RESERVATIONS",
                        "Nombre de réservations : "
                                + reservations.size()
                );


                // ==========================================
                // AFFICHAGE
                // ==========================================

                handler.post(() -> {

                    Log.d(
                            "API_RESERVATIONS",
                            ">>> Affichage des réservations sur le thread UI"
                    );

                    afficherReservations(
                            reservations
                    );

                    Log.d(
                            "API_RESERVATIONS",
                            "<<< Affichage terminé"
                    );
                });


            } catch (Exception e) {

                // ==========================================
                // ERREUR
                // ==========================================

                Log.e(
                        "API_RESERVATIONS",
                        "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                );

                Log.e(
                        "API_RESERVATIONS",
                        "ERREUR LORS DU CHARGEMENT DES RESERVATIONS"
                );

                Log.e(
                        "API_RESERVATIONS",
                        "Type : "
                                + e.getClass().getName()
                );

                Log.e(
                        "API_RESERVATIONS",
                        "Message : "
                                + e.getMessage()
                );

                Log.e(
                        "API_RESERVATIONS",
                        "Exception complète :",
                        e
                );

                // Stack trace également visible dans Logcat

                e.printStackTrace();


                handler.post(() -> {

                    Toast.makeText(
                            HomeActivity.this,
                            "Erreur API : "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                });

            } finally {

                if (connexion != null) {

                    connexion.disconnect();

                    Log.d(
                            "API_RESERVATIONS",
                            "Connexion HTTP fermée"
                    );
                }

                Log.d(
                        "API_RESERVATIONS",
                        "========================================"
                );
            }
        });
    }

    private List<Reservation> analyserReservations(
            String json
    ) throws Exception {

        List<Reservation> reservations =
                new ArrayList<>();

        JSONArray tableau =
                new JSONArray(json);

        for (int i = 0; i < tableau.length(); i++) {

            JSONObject objet =
                    tableau.getJSONObject(i);

            JSONObject objetTrajet =
                    objet.getJSONObject("trajet");

            Trajet trajet =
                    new Trajet(
                            objetTrajet.optString(
                                    "conducteur"
                            ),

                            objetTrajet.optString(
                                    "date"
                            ),

                            objetTrajet.optString(
                                    "depart"
                            ),

                            objetTrajet.optString(
                                    "destination"
                            ),

                            objetTrajet.optString(
                                    "heure"
                            ),

                            objetTrajet.optInt("id"),

                            objetTrajet.optInt(
                                    "places"
                            ),

                            objetTrajet.optDouble(
                                    "prix"
                            ),

                            objetTrajet.optString(
                                    "statut"
                            )
                    );

            Reservation reservation =
                    new Reservation(
                            objet.optString(
                                    "dateReservation"
                            ),

                            objet.optInt("id"),

                            objet.optInt(
                                    "nombrePlaces"
                            ),

                            objet.optString(
                                    "passager"
                            ),

                            objet.optString(
                                    "statut"
                            ),

                            trajet
                    );

            reservations.add(reservation);
        }

        return reservations;
    }

    private void afficherReservations(
            List<Reservation> reservations
    ) {

        // Aucun trajet

        if (reservations.isEmpty()) {

            cardTrajet1.setVisibility(
                    View.GONE
            );

            return;
        }

        // Premier trajet

        afficherTrajet(
                reservations.get(0),
                1
        );

        cardTrajet1.setVisibility(
                View.VISIBLE
        );


        // Deuxième trajet

        if (reservations.size() >= 2) {

            afficherTrajet(
                    reservations.get(1),
                    2
            );

        }
    }

    private void afficherTrajet(
            Reservation reservation,
            int numero
    ) {

        Trajet trajet =
                reservation.getTrajet();

        if (trajet == null) {
            return;
        }

        TextView destination;
        TextView depart;
        TextView prix;
        TextView date;
        TextView avatar;
        TextView conducteur;

            destination = textDestination1;
            depart = textDepart1;
            prix = textPrix1;
            date = textDate1;
            avatar = textAvatar1;
            conducteur = textConducteur1;


        // Destination

        destination.setText(
                trajet.getDestination()
        );

        // Départ

        depart.setText(
                trajet.getDepart()
        );

        // Prix

        prix.setText(
                formaterPrix(trajet.getPrix())
        );

        // Date + heure

        date.setText(
                formaterDateHeure(
                        trajet.getDate(),
                        trajet.getHeure()
                )
        );

        // Conducteur

        String nomConducteur =
                trajet.getConducteur();

        conducteur.setText(
                nomConducteur
        );

        // Initiale

        if (nomConducteur != null &&
                !nomConducteur.trim().isEmpty()) {

            avatar.setText(
                    nomConducteur
                            .substring(0, 1)
                            .toUpperCase()
            );
        } else {

            avatar.setText("?");
        }
    }

    private String formaterPrix(double prix) {

        return String.format(
                java.util.Locale.FRANCE,
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

            String[] morceauxDate =
                    date.split("-");

            String dateFormatee =
                    morceauxDate[2]
                            + "/"
                            + morceauxDate[1]
                            + "/"
                            + morceauxDate[0];

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
}