package com.javaAV.cargo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.javaAV.cargo.network.SocketClient;

public class MainActivity extends AppCompatActivity {

    private SocketClient socketClient;

    private TextView textEtat;
    private TextView textNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 30, 30, 30);

        textEtat = new TextView(this);
        textEtat.setText("État : Déconnecté");
        textEtat.setTextSize(18);

        textNotifications = new TextView(this);
        textNotifications.setText("Notifications :\n");
        textNotifications.setTextSize(16);

        Button boutonConnexion = new Button(this);
        boutonConnexion.setText("Se connecter");

        Button boutonDeconnexion = new Button(this);
        boutonDeconnexion.setText("Se déconnecter");

        layout.addView(textEtat);
        layout.addView(textNotifications);
        layout.addView(boutonConnexion);
        layout.addView(boutonDeconnexion);

        setContentView(layout);

        boutonConnexion.setOnClickListener(view ->
                connecterSocket()
        );

        boutonDeconnexion.setOnClickListener(view -> {

            if (socketClient != null) {
                socketClient.disconnect();
            }

            textEtat.setText("État : Déconnecté");
        });
    }

    private void connecterSocket() {

        String host = "26.226.199.167";
        int port = 6000;
        String nomConducteur = "Jean";

        socketClient = new SocketClient(
                host,
                port,
                nomConducteur,
                new SocketClient.SocketListener() {

                    @Override
                    public void onConnected(String message) {

                        runOnUiThread(() ->
                                textEtat.setText(
                                        "État : Connecté"
                                )
                        );
                    }

                    @Override
                    public void onNotification(String message) {

                        runOnUiThread(() -> {

                            String ancienTexte =
                                    textNotifications.getText()
                                            .toString();

                            textNotifications.setText(
                                    ancienTexte
                                            + "\n- "
                                            + message
                            );
                        });
                    }

                    @Override
                    public void onError(String message) {

                        runOnUiThread(() ->
                                textEtat.setText(
                                        "Erreur : " + message
                                )
                        );
                    }

                    @Override
                    public void onDisconnected() {

                        runOnUiThread(() ->
                                textEtat.setText(
                                        "État : Déconnecté"
                                )
                        );
                    }
                }
        );

        socketClient.connect();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (socketClient != null) {
            socketClient.disconnect();
        }
    }
}