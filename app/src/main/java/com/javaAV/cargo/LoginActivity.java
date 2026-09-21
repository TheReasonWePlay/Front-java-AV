package com.javaAV.cargo;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText champNom;
    private MaterialButton boutonConnexion;
    private TextView textEtat;

    private SocketManager socketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // =========================================
        // PERMISSION NOTIFICATIONS
        // =========================================

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            requestPermissions(
                    new String[]{
                            Manifest.permission.POST_NOTIFICATIONS
                    },
                    100
            );
        }

        // =========================================
        // SOCKET MANAGER
        // =========================================

        socketManager =
                SocketManager.getInstance(this);

        // =========================================
        // INTERFACE XML
        // =========================================

        setContentView(R.layout.activity_login);

        initialiserVues();

        initialiserActions();
    }


    // =============================================
    // INITIALISATION DES VUES
    // =============================================

    private void initialiserVues() {

        champNom =
                findViewById(R.id.champNom);

        boutonConnexion =
                findViewById(R.id.boutonConnexion);

        textEtat =
                findViewById(R.id.textEtat);
    }


    // =============================================
    // ACTIONS
    // =============================================

    private void initialiserActions() {

        boutonConnexion.setOnClickListener(
                view -> connecter()
        );

        // Permettre de valider avec le clavier
        champNom.setOnEditorActionListener(
                (v, actionId, event) -> {

                    connecter();

                    return true;
                }
        );
    }


    // =============================================
    // CONNEXION
    // =============================================

    private void connecter() {

        String nom =
                champNom.getText()
                        .toString()
                        .trim();


        // =========================================
        // VALIDATION
        // =========================================

        if (nom.isEmpty()) {

            champNom.setError(
                    "Veuillez entrer votre nom"
            );

            champNom.requestFocus();

            return;
        }


        // =========================================
        // ETAT CONNEXION
        // =========================================

        boutonConnexion.setEnabled(false);

        textEtat.setText(
                "Connexion en cours..."
        );


        // =========================================
        // CONNEXION SOCKET
        // =========================================

        socketManager.connecter(
                nom,
                new SocketManager.ConnectionListener() {

                    @Override
                    public void onConnected(
                            String message) {

                        runOnUiThread(() -> {

                            textEtat.setText(
                                    "Connexion réussie"
                            );


                            // =====================
                            // HOME
                            // =====================

                            Intent intent =
                                    new Intent(
                                            LoginActivity.this,
                                            HomeActivity.class
                                    );

                            intent.putExtra(
                                    "nom",
                                    nom
                            );

                            startActivity(intent);

                            finish();
                        });
                    }


                    @Override
                    public void onError(
                            String message) {

                        runOnUiThread(() -> {

                            boutonConnexion
                                    .setEnabled(true);

                            textEtat.setText(
                                    "Erreur : "
                                            + message
                            );

                            Toast.makeText(
                                    LoginActivity.this,
                                    message,
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                    }
                }
        );
    }
}