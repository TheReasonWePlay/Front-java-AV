package com.javaAV.cargo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.javaAV.cargo.network.SocketClient;

public class LoginActivity extends AppCompatActivity {

    private EditText champNom;
    private Button boutonConnexion;
    private TextView textEtat;

    private SocketManager socketManager;
    private NotificationHelper notificationHelper;

    private static final String HOST = "26.226.199.167";
    private static final int PORT = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.TIRAMISU) {

            requestPermissions(
                    new String[]{
                            android.Manifest.permission.POST_NOTIFICATIONS
                    },
                    100
            );
        }

        notificationHelper =
                new NotificationHelper(this);

        socketManager =
                SocketManager.getInstance(this);

        construireInterface();
    }

    private void construireInterface() {

        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(40, 40, 40, 40);

        TextView titre = new TextView(this);

        titre.setText("Bienvenue sur CarGo");
        titre.setTextSize(28);
        titre.setTextColor(Color.rgb(20, 35, 45));
        titre.setGravity(Gravity.CENTER);

        TextView sousTitre = new TextView(this);

        sousTitre.setText(
                "Connectez-vous pour continuer"
        );

        sousTitre.setTextSize(16);
        sousTitre.setGravity(Gravity.CENTER);
        sousTitre.setPadding(0, 20, 0, 40);

        champNom = new EditText(this);

        champNom.setHint("Entrez votre nom");
        champNom.setTextSize(16);
        champNom.setSingleLine(true);

        boutonConnexion = new Button(this);

        boutonConnexion.setText("Se connecter");
        boutonConnexion.setTextSize(16);

        textEtat = new TextView(this);

        textEtat.setText("");
        textEtat.setTextSize(14);
        textEtat.setGravity(Gravity.CENTER);
        textEtat.setPadding(0, 25, 0, 0);

        layout.addView(titre);
        layout.addView(sousTitre);
        layout.addView(champNom);
        layout.addView(boutonConnexion);
        layout.addView(textEtat);

        setContentView(layout);

        boutonConnexion.setOnClickListener(
                view -> connecter()
        );
    }

    private void connecter() {

        String nom = champNom.getText()
                .toString()
                .trim();

        if (nom.isEmpty()) {

            champNom.setError(
                    "Veuillez entrer votre nom"
            );

            return;
        }

        boutonConnexion.setEnabled(false);

        textEtat.setText(
                "Connexion en cours..."
        );

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