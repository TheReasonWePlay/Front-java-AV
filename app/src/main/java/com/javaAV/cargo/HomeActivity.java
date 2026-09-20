package com.javaAV.cargo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private String nomUtilisateur;

    private TextView textBonjour;
    private TextView textAvatar;

    private MaterialCardView cardRecherche;
    private MaterialCardView cardProposer;

    private BottomNavigationView navigation;

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
}