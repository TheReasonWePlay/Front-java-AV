package com.javaAV.cargo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.javaAV.cargo.adapter.TrajetAdapter;
import com.javaAV.cargo.model.Trajet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.DialogInterface;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;

public class TrajetActivity extends AppCompatActivity {

    private static final String TAG =
            "API_TRAJETS";

    private String nomUtilisateur;

    private RecyclerView recyclerTrajets;

    private BottomNavigationView navigation;

    private TrajetAdapter adapter;

    private final List<Trajet> trajets =
            new ArrayList<>();

    private ExecutorService executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        nomUtilisateur =
                getIntent().getStringExtra("nom");

        if (nomUtilisateur == null ||
                nomUtilisateur.trim().isEmpty()) {

            nomUtilisateur =
                    "Utilisateur";
        }

        setContentView(
                R.layout.activity_trajet
        );

        initialiserVues();

        initialiserRecyclerView();

        initialiserNavigation();

        executorService =
                Executors.newSingleThreadExecutor();

        chargerTrajets();
    }


    private void initialiserVues() {

        recyclerTrajets =
                findViewById(
                        R.id.recyclerTrajets
                );

        navigation =
                findViewById(
                        R.id.navigation
                );
    }


    private void initialiserRecyclerView() {

        adapter = new TrajetAdapter(
                trajets,
                trajet -> afficherDialogueReservation(trajet)
        );

        recyclerTrajets.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerTrajets.setAdapter(
                adapter
        );
    }


    private void initialiserNavigation() {

        navigation.setSelectedItemId(
                R.id.nav_trajets
        );

        navigation.setOnItemSelectedListener(
                item -> {

                    int id =
                            item.getItemId();


                    if (id == R.id.nav_accueil) {

                        Intent intent =
                                new Intent(
                                        TrajetActivity.this,
                                        HomeActivity.class
                                );

                        intent.putExtra(
                                "nom",
                                nomUtilisateur
                        );

                        startActivity(intent);

                        finish();

                        return true;
                    }


                    if (id == R.id.nav_trajets) {

                        return true;
                    }


                    if (id == R.id.nav_messages) {

                        Toast.makeText(
                                this,
                                "Messagerie prochainement",
                                Toast.LENGTH_SHORT
                        ).show();

                        return true;
                    }


                    if (id == R.id.nav_reservations) {

                        Toast.makeText(
                                this,
                                "Réservations prochainement",
                                Toast.LENGTH_SHORT
                        ).show();

                        return true;
                    }


                    if (id == R.id.nav_profil) {

                        Intent intent =
                                new Intent(
                                        TrajetActivity.this,
                                        ProfilActivity.class
                                );

                        intent.putExtra(
                                "nom",
                                nomUtilisateur
                        );

                        startActivity(intent);

                        finish();

                        return true;
                    }

                    return false;
                }
        );
    }

    private void afficherDialogueReservation(Trajet trajet) {

        final android.widget.EditText editNombrePlaces =
                new android.widget.EditText(this);

        editNombrePlaces.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
        );

        editNombrePlaces.setHint("Nombre de places");
        editNombrePlaces.setSingleLine(true);

        int padding = (int) (
                20 * getResources().getDisplayMetrics().density
        );

        editNombrePlaces.setPadding(
                padding,
                padding,
                padding,
                padding
        );

        AlertDialog dialogue =
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Réserver ce trajet")
                        .setMessage(
                                trajet.getDepart()
                                        + " → "
                                        + trajet.getDestination()
                                        + "\n"
                                        + trajet.getPlaces()
                                        + " places disponibles"
                        )
                        .setView(editNombrePlaces)
                        .setNegativeButton(
                                "Annuler",
                                null
                        )
                        .setPositiveButton(
                                "Réserver",
                                null
                        )
                        .create();

        dialogue.setOnShowListener(dialog -> {

            android.widget.Button bouton =
                    dialogue.getButton(
                            android.app.AlertDialog.BUTTON_POSITIVE
                    );

            bouton.setOnClickListener(view -> {

                String texte =
                        editNombrePlaces
                                .getText()
                                .toString()
                                .trim();

                if (texte.isEmpty()) {

                    editNombrePlaces.setError(
                            "Indiquez le nombre de places"
                    );

                    return;
                }

                int nombrePlaces;

                try {

                    nombrePlaces =
                            Integer.parseInt(texte);

                } catch (NumberFormatException e) {

                    editNombrePlaces.setError(
                            "Nombre invalide"
                    );

                    return;
                }

                if (nombrePlaces <= 0) {

                    editNombrePlaces.setError(
                            "Le nombre doit être supérieur à 0"
                    );

                    return;
                }

                if (nombrePlaces > trajet.getPlaces()) {

                    editNombrePlaces.setError(
                            "Seulement "
                                    + trajet.getPlaces()
                                    + " places disponibles"
                    );

                    return;
                }

                dialogue.dismiss();

                creerReservation(
                        trajet,
                        nombrePlaces
                );
            });
        });

        dialogue.show();
    }

    private void creerReservation(
            Trajet trajet,
            int nombrePlaces
    ) {

        Log.d(TAG, "========================================");
        Log.d(TAG, ">>> Création réservation");
        Log.d(TAG, "Trajet ID : " + trajet.getId());
        Log.d(TAG, "Passager : " + nomUtilisateur);
        Log.d(TAG, "Nombre places : " + nombrePlaces);

        executorService.execute(() -> {

            HttpURLConnection connexion = null;

            try {

                String urlString =
                        Config.API_BASE_URL + "reservations";

                Log.d(TAG, "URL réservation : " + urlString);

                URL url = new URL(urlString);

                connexion =
                        (HttpURLConnection) url.openConnection();

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

                JSONObject jsonReservation =
                        new JSONObject();

                jsonReservation.put(
                        "trajetId",
                        trajet.getId()
                );

                jsonReservation.put(
                        "passager",
                        nomUtilisateur
                );

                jsonReservation.put(
                        "nombrePlaces",
                        nombrePlaces
                );

                String json =
                        jsonReservation.toString();

                Log.d(TAG, "JSON envoyé : " + json);

                try (java.io.OutputStream outputStream =
                             connexion.getOutputStream()) {

                    byte[] bytes =
                            json.getBytes(
                                    java.nio.charset.StandardCharsets.UTF_8
                            );

                    outputStream.write(bytes);
                    outputStream.flush();
                }

                int code =
                        connexion.getResponseCode();

                Log.d(
                        TAG,
                        "<<< Code HTTP réservation : "
                                + code
                );

                java.io.InputStream inputStream;

                if (code >= 200 && code < 300) {

                    inputStream =
                            connexion.getInputStream();

                } else {

                    inputStream =
                            connexion.getErrorStream();
                }

                StringBuilder resultat =
                        new StringBuilder();

                if (inputStream != null) {

                    BufferedReader reader =
                            new BufferedReader(
                                    new InputStreamReader(
                                            inputStream
                                    )
                            );

                    String ligne;

                    while (
                            (ligne = reader.readLine())
                                    != null
                    ) {
                        resultat.append(ligne);
                    }

                    reader.close();
                }

                String reponse =
                        resultat.toString();

                Log.d(
                        TAG,
                        "Réponse réservation : "
                                + reponse
                );

                if (code >= 200 && code < 300) {

                    new Handler(
                            Looper.getMainLooper()
                    ).post(() -> {

                        Toast.makeText(
                                TrajetActivity.this,
                                "Réservation créée avec succès",
                                Toast.LENGTH_LONG
                        ).show();

                        chargerTrajets();
                    });

                } else {

                    new Handler(
                            Looper.getMainLooper()
                    ).post(() ->
                            afficherErreurReservation(
                                    code,
                                    reponse
                            )
                    );
                }

            } catch (Exception e) {

                Log.e(
                        TAG,
                        "ERREUR création réservation",
                        e
                );

                new Handler(
                        Looper.getMainLooper()
                ).post(() ->
                        Toast.makeText(
                                TrajetActivity.this,
                                "Impossible de créer la réservation : "
                                        + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );

            } finally {

                if (connexion != null) {
                    connexion.disconnect();
                }

                Log.d(
                        TAG,
                        "Connexion réservation fermée"
                );
            }
        });
    }

    private void afficherErreurReservation(
            int code,
            String reponse
    ) {

        String message =
                "Impossible de créer la réservation.";

        try {

            if (reponse != null &&
                    !reponse.trim().isEmpty()) {

                JSONObject erreur =
                        new JSONObject(reponse);

                String messageApi =
                        erreur.optString("message");

                if (messageApi != null &&
                        !messageApi.trim().isEmpty()) {

                    message = messageApi;
                }
            }

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Impossible de lire l'erreur API",
                    e
            );
        }

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();

        Log.e(
                TAG,
                "Erreur réservation HTTP "
                        + code
                        + " : "
                        + reponse
        );
    }
    private void chargerTrajets() {

        Log.d(
                TAG,
                "========================================"
        );

        Log.d(
                TAG,
                ">>> Chargement des trajets"
        );

        executorService.execute(() -> {

            HttpURLConnection connexion =
                    null;

            try {

                String urlString =
                        Config.API_BASE_URL
                                + "trajets";

                Log.d(
                        TAG,
                        "URL API : "
                                + urlString
                );

                URL url =
                        new URL(urlString);

                connexion =
                        (HttpURLConnection)
                                url.openConnection();

                connexion.setRequestMethod(
                        "GET"
                );

                connexion.setConnectTimeout(
                        5000
                );

                connexion.setReadTimeout(
                        5000
                );

                connexion.setRequestProperty(
                        "Accept",
                        "application/json"
                );


                Log.d(
                        TAG,
                        ">>> Envoi GET..."
                );

                int code =
                        connexion.getResponseCode();

                Log.d(
                        TAG,
                        "<<< Code HTTP : "
                                + code
                );


                if (code !=
                        HttpURLConnection.HTTP_OK) {

                    throw new Exception(
                            "HTTP " + code
                    );
                }


                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        connexion.getInputStream()
                                )
                        );

                StringBuilder resultat =
                        new StringBuilder();

                String ligne;

                while (
                        (ligne =
                                reader.readLine())
                                != null
                ) {

                    resultat.append(
                            ligne
                    );
                }

                reader.close();


                String json =
                        resultat.toString();

                Log.d(
                        TAG,
                        "Réponse API : "
                                + json
                );


                List<Trajet> liste =
                        analyserTrajets(
                                json
                        );


                Log.d(
                        TAG,
                        "Nombre de trajets : "
                                + liste.size()
                );


                new Handler(
                        Looper.getMainLooper()
                ).post(() -> {

                    trajets.clear();

                    trajets.addAll(
                            liste
                    );

                    adapter.notifyDataSetChanged();

                    Log.d(
                            TAG,
                            "Liste affichée"
                    );
                });


            } catch (Exception e) {

                Log.e(
                        TAG,
                        "ERREUR chargement trajets",
                        e
                );


                new Handler(
                        Looper.getMainLooper()
                ).post(() -> {

                    Toast.makeText(
                            TrajetActivity.this,
                            "Impossible de charger les trajets : "
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


    private List<Trajet> analyserTrajets(
            String json
    ) throws Exception {

        List<Trajet> liste =
                new ArrayList<>();

        JSONArray tableau =
                new JSONArray(json);


        for (
                int i = 0;
                i < tableau.length();
                i++
        ) {

            JSONObject objet =
                    tableau.getJSONObject(i);


            Trajet trajet =
                    new Trajet(


                            objet.optString(
                                    "conducteur"
                            ),

                            objet.optString(
                                    "date"
                            ),

                            objet.optString(
                                    "depart"
                            ),

                            objet.optString(
                                    "destination"
                            ),

                            objet.optString(
                                    "heure"
                            ),

                            objet.optInt(
                                    "id"
                            ),

                            objet.optInt(
                                    "places"
                            ),

                            objet.optDouble(
                                    "prix"
                            ),

                            objet.optString(
                                    "statut"
                            )
                    );


            liste.add(
                    trajet
            );
        }


        return liste;
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (executorService != null) {

            executorService.shutdownNow();
        }
    }
}