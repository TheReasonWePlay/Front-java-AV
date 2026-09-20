package com.javaAV.cargo.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.util.Log;

public class SocketClient {

    public interface SocketListener {

        void onConnected(String message);

        void onNotification(String message);

        void onError(String message);

        void onDisconnected();
    }

    private final String host;
    private final int port;
    private final String clientName;
    private final SocketListener listener;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    private volatile boolean connected = false;
    private volatile boolean authenticated = false;

    public SocketClient(
            String host,
            int port,
            String clientName,
            SocketListener listener) {

        this.host = host;
        this.port = port;
        this.clientName = clientName;
        this.listener = listener;
    }

    public void connect() {

        executor.execute(() -> {

            try {

                Log.d("SOCKET_DEBUG", "Tentative connexion à "
                        + host + ":" + port);

                socket = new Socket(host, port);

                Log.d("SOCKET_DEBUG", "Socket TCP connectée");

                reader = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                );

                writer = new PrintWriter(
                        socket.getOutputStream(),
                        true
                );

                connected = true;

                Log.d(
                        "SOCKET_DEBUG",
                        "Envoi REGISTER:" + clientName
                );

                writer.println("REGISTER:" + clientName);
                writer.flush();

                String message;

                while (connected &&
                        (message = reader.readLine()) != null) {

                    Log.d(
                            "SOCKET_DEBUG",
                            "MESSAGE REÇU = [" + message + "]"
                    );

                    traiterMessage(message);
                }

            } catch (IOException exception) {

                Log.e(
                        "SOCKET_DEBUG",
                        "ERREUR SOCKET",
                        exception
                );

                if (connected) {

                    listener.onError(
                            "Erreur Socket : "
                                    + exception.getMessage()
                    );
                }

            } finally {

                Log.d(
                        "SOCKET_DEBUG",
                        ">>> FINALLY : fermeture du socket"
                );

                connected = false;
                authenticated = false;

                fermerConnexion();

                listener.onDisconnected();
            }
        });
    }

    private void traiterMessage(String message) {

        Log.d(
                "SOCKET_DEBUG",
                "traiterMessage() = [" + message + "]"
        );

        if (message.startsWith("CONNECTE:")) {

            Log.d(
                    "SOCKET_DEBUG",
                    ">>> CONNECTE DETECTE"
            );

            authenticated = true;

            listener.onConnected(message);

        } else if (message.startsWith("NOTIFICATION:")) {

            Log.d(
                    "SOCKET_DEBUG",
                    ">>> NOTIFICATION DETECTEE"
            );

            String notification =
                    message.substring(
                            "NOTIFICATION:".length()
                    );

            listener.onNotification(notification);

        } else if (message.startsWith("ERREUR:")) {

            Log.d(
                    "SOCKET_DEBUG",
                    ">>> ERREUR SERVEUR"
            );

            listener.onError(message);

        } else {

            Log.d(
                    "SOCKET_DEBUG",
                    ">>> MESSAGE INCONNU"
            );

            listener.onError(
                    "Message serveur inconnu : " + message
            );
        }
    }

    public void envoyerMessage(String message) {

        executor.execute(() -> {

            if (writer != null && connected) {

                writer.println(message);
                writer.flush();
            }
        });
    }

    public void disconnect() {

        Log.d(
                "SOCKET_DEBUG",
                ">>> disconnect() APPELE EXPLICITEMENT"
        );

        // Signaler immédiatement que la connexion doit s'arrêter
        connected = false;

        // Récupérer la socket actuelle
        Socket socketLocal = socket;

        // Fermer directement la socket.
        // Cela doit débloquer le readLine() du thread réseau.
        if (socketLocal != null) {

            try {

                Log.d(
                        "SOCKET_DEBUG",
                        ">>> Fermeture du Socket TCP"
                );

                socketLocal.shutdownInput();

            } catch (IOException exception) {

                Log.d(
                        "SOCKET_DEBUG",
                        "shutdownInput : "
                                + exception.getMessage()
                );
            }

            try {

                socketLocal.shutdownOutput();

            } catch (IOException exception) {

                Log.d(
                        "SOCKET_DEBUG",
                        "shutdownOutput : "
                                + exception.getMessage()
                );
            }

            try {

                socketLocal.close();

                Log.d(
                        "SOCKET_DEBUG",
                        ">>> Socket TCP fermée"
                );

            } catch (IOException exception) {

                Log.e(
                        "SOCKET_DEBUG",
                        "Erreur fermeture socket : "
                                + exception.getMessage()
                );
            }
        }

        // Arrêter les tâches de l'executor
        executor.shutdownNow();

        Log.d(
                "SOCKET_DEBUG",
                ">>> disconnect() TERMINE"
        );
    }

    private void fermerConnexion() {

        Log.d(
                "SOCKET_DEBUG",
                ">>> fermerConnexion()"
        );

        Socket socketLocal = socket;

        socket = null;
        reader = null;
        writer = null;

        if (socketLocal != null) {

            try {

                socketLocal.close();

                Log.d(
                        "SOCKET_DEBUG",
                        ">>> Socket fermée dans fermerConnexion()"
                );

            } catch (IOException exception) {

                Log.e(
                        "SOCKET_DEBUG",
                        "Erreur fermeture socket : "
                                + exception.getMessage()
                );
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
