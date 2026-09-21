package com.javaAV.cargo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProposerTrajetActivity extends AppCompatActivity {

    private static final String TAG = "API_PROPOSER_TRAJET";

    private String nomUtilisateur;

    private EditText editDepart;
    private EditText editDestination;
    private EditText editDate;
    private EditText editHeure;
    private EditText editPlaces;

    private MaterialButton boutonMoinsPlaces;
    private MaterialButton boutonPlusPlaces;
    private EditText editPrix;

    private TextView textPrixRecommande;

    private MaterialButton boutonProposer;

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nomUtilisateur =
                getIntent().getStringExtra("nom");

        if (nomUtilisateur == null ||
                nomUtilisateur.trim().isEmpty()) {

            nomUtilisateur = "Utilisateur";
        }

        setContentView(R.layout.activity_proposer_trajet);

        initialiserVues();
        initialiserActions();

        boutonMoinsPlaces = findViewById(R.id.boutonMoinsPlaces);
        boutonPlusPlaces = findViewById(R.id.boutonPlusPlaces);
        editPlaces = findViewById(R.id.editPlaces);

        boutonMoinsPlaces.setOnClickListener(v -> {

            int places = Integer.parseInt(
                    editPlaces.getText().toString()
            );

            if (places > 1) {
                places--;
                editPlaces.setText(String.valueOf(places));
            }
        });

        boutonPlusPlaces.setOnClickListener(v -> {

            int places = Integer.parseInt(
                    editPlaces.getText().toString()
            );

            places++;
            editPlaces.setText(String.valueOf(places));
        });

        executorService =
                Executors.newSingleThreadExecutor();
    }

    private void initialiserVues() {

        editDepart =
                findViewById(R.id.editDepart);

        editDestination =
                findViewById(R.id.editDestination);

        editDate =
                findViewById(R.id.editDate);

        editHeure =
                findViewById(R.id.editHeure);

        editPlaces =
                findViewById(R.id.editPlaces);

        editPrix =
                findViewById(R.id.editPrix);

        boutonProposer =
                findViewById(R.id.boutonProposer);

        textPrixRecommande =
                findViewById(R.id.textPrixRecommande);
    }

    private void initialiserActions() {

        editDate.setOnClickListener(
                view -> afficherDatePicker()
        );

        editHeure.setOnClickListener(
                view -> afficherTimePicker()
        );

        boutonProposer.setOnClickListener(
                view -> proposerTrajet()
        );
    }

    private void afficherDatePicker() {

        Calendar calendrier =
                Calendar.getInstance();

        int annee =
                calendrier.get(Calendar.YEAR);

        int mois =
                calendrier.get(Calendar.MONTH);

        int jour =
                calendrier.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog =
                new DatePickerDialog(
                        this,
                        (datePicker, anneeChoisie,
                         moisChoisi, jourChoisi) -> {

                            String date =
                                    String.format(
                                            java.util.Locale.US,
                                            "%04d-%02d-%02d",
                                            anneeChoisie,
                                            moisChoisi + 1,
                                            jourChoisi
                                    );

                            editDate.setText(date);
                        },
                        annee,
                        mois,
                        jour
                );

        dialog.show();
    }

    private void afficherTimePicker() {

        Calendar calendrier =
                Calendar.getInstance();

        int heure =
                calendrier.get(Calendar.HOUR_OF_DAY);

        int minute =
                calendrier.get(Calendar.MINUTE);

        TimePickerDialog dialog =
                new TimePickerDialog(
                        this,
                        (timePicker,
                         heureChoisie,
                         minuteChoisie) -> {

                            String heure1 =
                                    String.format(
                                            java.util.Locale.US,
                                            "%02d:%02d:00",
                                            heureChoisie,
                                            minuteChoisie
                                    );

                            editHeure.setText(heure1);
                        },
                        heure,
                        minute,
                        true
                );

        dialog.show();
    }

    private void proposerTrajet() {

        String depart =
                editDepart.getText()
                        .toString()
                        .trim();

        String destination =
                editDestination.getText()
                        .toString()
                        .trim();

        String date =
                editDate.getText()
                        .toString()
                        .trim();

        String heure =
                editHeure.getText()
                        .toString()
                        .trim();

        String placesText =
                editPlaces.getText()
                        .toString()
                        .trim();

        String prixText =
                editPrix.getText()
                        .toString()
                        .trim();

        if (depart.isEmpty()) {
            editDepart.setError("Veuillez saisir le départ");
            editDepart.requestFocus();
            return;
        }

        if (destination.isEmpty()) {
            editDestination.setError("Veuillez saisir la destination");
            editDestination.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            editDate.setError("Veuillez choisir une date");
            editDate.requestFocus();
            return;
        }

        if (heure.isEmpty()) {
            editHeure.setError("Veuillez choisir une heure");
            editHeure.requestFocus();
            return;
        }

        if (placesText.isEmpty()) {
            editPlaces.setError("Veuillez saisir le nombre de places");
            editPlaces.requestFocus();
            return;
        }

        if (prixText.isEmpty()) {
            editPrix.setError("Veuillez saisir votre prix");
            editPrix.requestFocus();
            return;
        }

        int places;

        double prix;

        try {

            places =
                    Integer.parseInt(placesText);

        } catch (NumberFormatException e) {

            editPlaces.setError(
                    "Nombre de places invalide"
            );

            editPlaces.requestFocus();
            return;
        }

        try {

            prix =
                    Double.parseDouble(prixText);

        } catch (NumberFormatException e) {

            editPrix.setError(
                    "Prix invalide"
            );

            editPrix.requestFocus();
            return;
        }

        if (places <= 0) {

            editPlaces.setError(
                    "Le nombre de places doit être supérieur à 0"
            );

            editPlaces.requestFocus();
            return;
        }

        if (prix <= 0) {

            editPrix.setError(
                    "Le prix doit être supérieur à 0"
            );

            editPrix.requestFocus();
            return;
        }

        boutonProposer.setEnabled(false);
        boutonProposer.setText("Envoi en cours...");

        envoyerTrajet(
                depart,
                destination,
                date,
                heure,
                places,
                prix
        );
    }

    private void envoyerTrajet(
            String depart,
            String destination,
            String date,
            String heure,
            int places,
            double prix
    ) {

        executorService.execute(() -> {

            HttpURLConnection connexion = null;

            try {

                String urlString =
                        Config.API_BASE_URL
                                + "trajets";

                Log.d(
                        TAG,
                        "========================================"
                );

                Log.d(
                        TAG,
                        ">>> Création d'un nouveau trajet"
                );

                Log.d(
                        TAG,
                        "Utilisateur : "
                                + nomUtilisateur
                );

                Log.d(
                        TAG,
                        "URL : "
                                + urlString
                );

                Log.d(
                        TAG,
                        "Départ : "
                                + depart
                );

                Log.d(
                        TAG,
                        "Destination : "
                                + destination
                );

                Log.d(
                        TAG,
                        "Date : "
                                + date
                );

                Log.d(
                        TAG,
                        "Heure : "
                                + heure
                );

                Log.d(
                        TAG,
                        "Places : "
                                + places
                );

                Log.d(
                        TAG,
                        "Prix : "
                                + prix
                );

                URL url =
                        new URL(urlString);

                connexion =
                        (HttpURLConnection)
                                url.openConnection();

                connexion.setRequestMethod("POST");

                connexion.setConnectTimeout(5000);
                connexion.setReadTimeout(5000);

                connexion.setDoOutput(true);

                connexion.setRequestProperty(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );

                connexion.setRequestProperty(
                        "Accept",
                        "application/json"
                );

                JSONObject objet =
                        new JSONObject();

                objet.put(
                        "conducteur",
                        nomUtilisateur
                );

                objet.put(
                        "depart",
                        depart
                );

                objet.put(
                        "destination",
                        destination
                );

                objet.put(
                        "date",
                        date
                );

                objet.put(
                        "heure",
                        heure
                );

                objet.put(
                        "places",
                        places
                );

                objet.put(
                        "prix",
                        prix
                );

                String json =
                        objet.toString();

                Log.d(
                        TAG,
                        "JSON envoyé : "
                                + json
                );

                byte[] donnees =
                        json.getBytes(
                                StandardCharsets.UTF_8
                        );

                OutputStream outputStream =
                        connexion.getOutputStream();

                outputStream.write(donnees);
                outputStream.flush();
                outputStream.close();

                Log.d(
                        TAG,
                        ">>> Requête POST envoyée"
                );

                int code =
                        connexion.getResponseCode();

                Log.d(
                        TAG,
                        "<<< Code HTTP : "
                                + code
                );

                InputStream inputStream;

                if (code >= 200 && code < 300) {

                    inputStream =
                            connexion.getInputStream();

                } else {

                    inputStream =
                            connexion.getErrorStream();
                }

                String reponse =
                        lireReponse(inputStream);

                Log.d(
                        TAG,
                        "Réponse API : "
                                + reponse
                );

                final String reponseFinale =
                        reponse;

                if (code >= 200 && code < 300) {

                    new Handler(
                            Looper.getMainLooper()
                    ).post(() -> {

                        Toast.makeText(
                                ProposerTrajetActivity.this,
                                "Trajet proposé avec succès",
                                Toast.LENGTH_LONG
                        ).show();

                        retournerAccueil();
                    });

                } else {

                    traiterErreurApi(
                            code,
                            reponseFinale
                    );
                }

            } catch (Exception e) {

                Log.e(
                        TAG,
                        "Erreur lors de la création du trajet",
                        e
                );

                new Handler(
                        Looper.getMainLooper()
                ).post(() -> {

                    boutonProposer.setEnabled(true);
                    boutonProposer.setText(
                            "Proposer le trajet"
                    );

                    Toast.makeText(
                            ProposerTrajetActivity.this,
                            "Erreur réseau : "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });

            } finally {

                if (connexion != null) {

                    connexion.disconnect();
                }

                Log.d(
                        TAG,
                        "Connexion HTTP fermée"
                );
            }
        });
    }

    private String lireReponse(
            InputStream inputStream
    ) throws Exception {

        if (inputStream == null) {
            return "";
        }

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                inputStream,
                                StandardCharsets.UTF_8
                        )
                );

        StringBuilder resultat =
                new StringBuilder();

        String ligne;

        while ((ligne = reader.readLine()) != null) {

            resultat.append(ligne);
        }

        reader.close();

        return resultat.toString();
    }

    private void traiterErreurApi(
            int code,
            String reponse
    ) {

        new Handler(
                Looper.getMainLooper()
        ).post(() -> {

            boutonProposer.setEnabled(true);

            boutonProposer.setText(
                    "Proposer le trajet"
            );

            String message =
                    "Erreur HTTP " + code;

            Double prixRecommande = null;

            try {

                JSONObject erreur =
                        new JSONObject(reponse);

                message =
                        erreur.optString(
                                "message",
                                message
                        );

                if (erreur.has("prixRecommande")
                        && !erreur.isNull("prixRecommande")) {

                    prixRecommande =
                            erreur.getDouble(
                                    "prixRecommande"
                            );
                }

            } catch (Exception e) {

                Log.e(
                        TAG,
                        "Impossible d'analyser l'erreur API",
                        e
                );
            }


            // ------------------------------------------------
            // Prix refusé par le backend
            // ------------------------------------------------

            if (prixRecommande != null) {

                textPrixRecommande.setText(
                        "Prix recommandé : "
                                + formaterPrix(
                                prixRecommande
                        )
                );

                textPrixRecommande.setVisibility(
                        View.VISIBLE
                );

                editPrix.requestFocus();

                Toast.makeText(
                        ProposerTrajetActivity.this,
                        message,
                        Toast.LENGTH_LONG
                ).show();

                Log.e(
                        TAG,
                        "Prix refusé : "
                                + prixRecommande
                );

            } else {

                Toast.makeText(
                        ProposerTrajetActivity.this,
                        message,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private String formaterPrix(
            double prix
    ) {

        return String.format(
                java.util.Locale.FRANCE,
                "%,.0f Ar",
                prix
        );
    }

    private void retournerAccueil() {

        Intent intent =
                new Intent(
                        ProposerTrajetActivity.this,
                        HomeActivity.class
                );

        intent.putExtra(
                "nom",
                nomUtilisateur
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        );

        startActivity(intent);

        finish();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}