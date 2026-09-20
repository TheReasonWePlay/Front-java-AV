package com.javaAV.cargo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilActivity extends AppCompatActivity {

    private String nomUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        nomUtilisateur = getIntent()
                .getStringExtra("nom");

        if (nomUtilisateur == null) {
            nomUtilisateur = "Utilisateur";
        }

        construireInterface();
    }

    private void construireInterface() {

        LinearLayout principal =
                new LinearLayout(this);

        principal.setOrientation(
                LinearLayout.VERTICAL
        );

        principal.setBackgroundColor(
                Color.rgb(250, 252, 251)
        );

        // ==========================================
        // CONTENU PRINCIPAL
        // ==========================================

        LinearLayout contenu =
                new LinearLayout(this);

        contenu.setOrientation(
                LinearLayout.VERTICAL
        );

        contenu.setPadding(
                30,
                40,
                30,
                30
        );

        // Titre

        TextView titre =
                new TextView(this);

        titre.setText("Mon profil");
        titre.setTextSize(28);
        titre.setTextColor(
                Color.rgb(25, 35, 45)
        );

        titre.setPadding(
                0,
                0,
                0,
                30
        );

        contenu.addView(titre);

        // Nom utilisateur

        TextView nom =
                new TextView(this);

        nom.setText(
                "Bonjour, " + nomUtilisateur
        );

        nom.setTextSize(20);
        nom.setTextColor(
                Color.rgb(25, 35, 45)
        );

        nom.setPadding(
                0,
                0,
                0,
                40
        );

        contenu.addView(nom);

        // ==========================================
        // BOUTON DECONNEXION
        // ==========================================

        Button boutonDeconnexion =
                new Button(this);

        boutonDeconnexion.setText(
                "Se déconnecter"
        );

        boutonDeconnexion.setTextSize(16);

        contenu.addView(
                boutonDeconnexion,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        boutonDeconnexion.setOnClickListener(
                view -> deconnecter()
        );

        // ==========================================
        // AJOUT DU CONTENU PRINCIPAL
        // ==========================================

        principal.addView(
                contenu,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        // ==========================================
        // BARRE DE NAVIGATION
        // ==========================================

        LinearLayout navigation =
                creerNavigation();

        principal.addView(
                navigation,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        90
                )
        );

        setContentView(principal);
    }

    private LinearLayout creerNavigation() {

        LinearLayout navigation =
                new LinearLayout(this);

        navigation.setOrientation(
                LinearLayout.HORIZONTAL
        );

        navigation.setGravity(
                Gravity.CENTER
        );

        navigation.setBackgroundColor(
                Color.WHITE
        );

        String[] noms = {
                "⌂\nAccueil",
                "➤\nTrajets",
                "□\nMessages",
                "▣\nRéservations",
                "♙\nProfil"
        };

        for (String nom : noms) {

            Button bouton =
                    new Button(this);

            bouton.setText(nom);
            bouton.setTextSize(10);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                    );

            navigation.addView(
                    bouton,
                    params
            );

            bouton.setOnClickListener(view -> {

                if (nom.contains("Accueil")) {

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

                } else if (nom.contains("Profil")) {

                    // Déjà sur le profil

                } else if (nom.contains("Messages")) {

                    Toast.makeText(
                            this,
                            "Messagerie prochainement",
                            Toast.LENGTH_SHORT
                    ).show();

                } else if (nom.contains("Trajets")) {

                    Toast.makeText(
                            this,
                            "Trajets prochainement",
                            Toast.LENGTH_SHORT
                    ).show();

                } else if (nom.contains("Réservations")) {

                    Toast.makeText(
                            this,
                            "Réservations prochainement",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }

        return navigation;
    }

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