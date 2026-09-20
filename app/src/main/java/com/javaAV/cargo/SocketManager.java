package com.javaAV.cargo;

import android.content.Context;
import android.util.Log;

import com.javaAV.cargo.network.SocketClient;

public class SocketManager {

    public interface ConnectionListener {

        void onConnected(String message);

        void onError(String message);
    }

    private static SocketManager instance;

    private final Context context;

    private SocketClient socketClient;

    private String nomUtilisateur;

    private NotificationHelper notificationHelper;

    private SocketManager(Context context) {

        this.context =
                context.getApplicationContext();

        notificationHelper =
                new NotificationHelper(this.context);
    }

    public static synchronized SocketManager getInstance(
            Context context) {

        if (instance == null) {

            instance =
                    new SocketManager(context);
        }

        return instance;
    }

    public void connecter(
            String nom,
            ConnectionListener listener) {

        if (socketClient != null &&
                socketClient.isConnected()) {

            listener.onConnected(
                    "Connexion déjà établie"
            );

            return;
        }

        nomUtilisateur = nom;

        socketClient =
                new SocketClient(
                        "26.226.199.167",
                        6000,
                        nom,
                        new SocketClient.SocketListener() {

                            @Override
                            public void onConnected(
                                    String message) {

                                Log.d(
                                        "SOCKET_MANAGER",
                                        "CONNECTE : "
                                                + message
                                );

                                listener.onConnected(
                                        message
                                );
                            }

                            @Override
                            public void onNotification(
                                    String message) {

                                Log.d(
                                        "SOCKET_MANAGER",
                                        "NOTIFICATION : "
                                                + message
                                );

                                notificationHelper
                                        .afficher(message);
                            }

                            @Override
                            public void onError(
                                    String message) {

                                Log.e(
                                        "SOCKET_MANAGER",
                                        "Erreur : "
                                                + message
                                );

                                listener.onError(
                                        message
                                );
                            }

                            @Override
                            public void onDisconnected() {

                                Log.d(
                                        "SOCKET_MANAGER",
                                        "Socket déconnectée"
                                );
                            }
                        }
                );

        socketClient.connect();
    }

    public void deconnecter() {

        if (socketClient != null) {

            socketClient.disconnect();

            socketClient = null;
        }
    }

    public boolean estConnecte() {

        return socketClient != null &&
                socketClient.isConnected();
    }

    public String getNomUtilisateur() {

        return nomUtilisateur;
    }
}