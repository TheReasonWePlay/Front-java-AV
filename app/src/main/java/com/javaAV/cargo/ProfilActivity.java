package com.javaAV.cargo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class ProfilActivity extends AppCompatActivity {

    private String nomUtilisateur;

    private TextView textAvatarProfil;
    private TextView textNomProfil;
    private TextView textInformationNom;

    private MaterialButton boutonDeconnexion;

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // ==========================================
        // NOM UTILISATEUR
        // ==========================================

        nomUtilisateur =
                getIntent().getStringExtra("nom");

        if (nomUtilisateur == null ||
                nomUtilisateur.trim().isEmpty()) {

            nomUtilisateur = "Utilisateur";
        }

        // ==========================================
        // CHARGEMENT XML
        // ==========================================

        setContentView(R.layout.activity_profil);

        initialiserVues();
        initialiserInterface();
        initialiserActions();
        initialiserNavigation();
    }

    // ==============================================
    // INITIALISATION DES VUES
    // ==============================================

    private void initialiserVues() {

        textAvatarProfil =
                findViewById(R.id.textAvatarProfil);

        textNomProfil =
                findViewById(R.id.textNomProfil);

        textInformationNom =
                findViewById(R.id.textInformationNom);

        boutonDeconnexion =
                findViewById(R.id.boutonDeconnexion);

        navigation =
                findViewById(R.id.navigation);
    }

    // ==============================================
    // INTERFACE
    // ==============================================

    private void initialiserInterface() {

        // Nom

        textNomProfil.setText(
                nomUtilisateur
        );

        textInformationNom.setText(
                nomUtilisateur
        );

        // Initiale

        String initiale =
                nomUtilisateur
                        .substring(0, 1)
                        .toUpperCase();

        textAvatarProfil.setText(
                initiale
        );
    }

    // ==============================================
    // ACTIONS
    // ==============================================

    private void initialiserActions() {

        boutonDeconnexion.setOnClickListener(
                view -> deconnecter()
        );
    }

    // ==============================================
    // NAVIGATION
    // ==============================================

    private void initialiserNavigation() {

        // Profil sélectionné

        navigation.setSelectedItemId(
                R.id.nav_profil
        );

        navigation.setOnItemSelectedListener(
                item -> {

                    int id =
                            item.getItemId();

                    // ----------------------------------
                    // ACCUEIL
                    // ----------------------------------

                    if (id == R.id.nav_accueil) {

                        Intent intent =
                                new Intent(
                                        ProfilActivity.this,
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

                    // ----------------------------------
                    // TRAJETS
                    // ----------------------------------

                    if (id == R.id.nav_trajets) {

                        Toast.makeText(
                                this,
                                "Trajets prochainement",
                                Toast.LENGTH_SHORT
                        ).show();

                        return true;
                    }

                    // ----------------------------------
                    // MESSAGES
                    // ----------------------------------

                    if (id == R.id.nav_messages) {

                        Toast.makeText(
                                this,
                                "Messagerie prochainement",
                                Toast.LENGTH_SHORT
                        ).show();

                        return true;
                    }

                    // ----------------------------------
                    // RESERVATIONS
                    // ----------------------------------

                    if (id == R.id.nav_reservations) {

                        Toast.makeText(
                                this,
                                "Réservations prochainement",
                                Toast.LENGTH_SHORT
                        ).show();

                        return true;
                    }

                    // ----------------------------------
                    // PROFIL
                    // ----------------------------------

                    if (id == R.id.nav_profil) {

                        return true;
                    }

                    return false;
                }
        );
    }

    // ==============================================
    // DECONNEXION
    // ==============================================

    private void deconnecter() {

        Log.d(
                "PROFIL_DEBUG",
                ">>> Clic sur déconnexion"
        );

        SocketManager socketManager =
                SocketManager.getInstance(this);

        socketManager.deconnecter();

        Log.d(
                "PROFIL_DEBUG",
                ">>> SocketManager.deconnecter() terminé"
        );

        Intent intent =
                new Intent(
                        ProfilActivity.this,
                        LoginActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        Log.d(
                "PROFIL_DEBUG",
                ">>> Redirection vers LoginActivity"
        );

        startActivity(intent);

        finish();
    }
}