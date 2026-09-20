package com.javaAV.cargo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Space;


import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private String nomUtilisateur;

    private static final int VERT_CARGO =
            Color.rgb(0, 190, 135);

    private static final int VERT_CLAIR =
            Color.rgb(232, 247, 239);

    private static final int FOND =
            Color.rgb(249, 251, 250);

    private static final int TEXTE =
            Color.rgb(25, 31, 42);

    private static final int TEXTE_GRIS =
            Color.rgb(115, 122, 132);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        nomUtilisateur = getIntent()
                .getStringExtra("nom");

        if (nomUtilisateur == null ||
                nomUtilisateur.trim().isEmpty()) {

            nomUtilisateur = "Utilisateur";
        }

        construireInterface();
    }

    private void construireInterface() {

        LinearLayout racine =
                new LinearLayout(this);

        racine.setOrientation(
                LinearLayout.VERTICAL
        );

        racine.setBackgroundColor(FOND);

        // ==============================
        // CONTENU SCROLLABLE
        // ==============================

        ScrollView scrollView =
                new ScrollView(this);

        scrollView.setFillViewport(true);

        LinearLayout contenu =
                new LinearLayout(this);

        contenu.setOrientation(
                LinearLayout.VERTICAL
        );

        contenu.setPadding(
                16,
                28,
                16,
                25
        );

        // ==============================
        // HEADER
        // ==============================

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.HORIZONTAL
        );

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        LinearLayout blocTitre =
                new LinearLayout(this);

        blocTitre.setOrientation(
                LinearLayout.VERTICAL
        );

        TextView bienvenue =
                texte(
                        "Bonjour, " + nomUtilisateur,
                        23,
                        TEXTE
                );

        bienvenue.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        TextView sousTitre =
                texte(
                        "Où allons-nous aujourd'hui ?",
                        14,
                        TEXTE_GRIS
                );

        blocTitre.addView(bienvenue);

        LinearLayout.LayoutParams sousParams =
                new LinearLayout.LayoutParams(
                        -2,
                        -2
                );

        sousParams.topMargin = 4;

        blocTitre.addView(
                sousTitre,
                sousParams
        );

        header.addView(
                blocTitre,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        // Avatar
        TextView avatar =
                creerAvatar(
                        nomUtilisateur
                                .substring(0, 1)
                                .toUpperCase()
                );

        header.addView(avatar);

        contenu.addView(header);

        espace(contenu, 28);

        // ==============================
        // ACTIONS
        // ==============================

        LinearLayout actions =
                new LinearLayout(this);

        actions.setOrientation(
                LinearLayout.HORIZONTAL
        );

        View rechercher =
                creerAction(
                        "⌕",
                        "Rechercher un trajet",
                        true
                );

        View proposer =
                creerAction(
                        "+",
                        "Proposer un trajet",
                        false
                );

        LinearLayout.LayoutParams actionParams =
                new LinearLayout.LayoutParams(
                        0,
                        110,
                        1
                );

        actionParams.setMargins(
                0,
                0,
                7,
                0
        );

        actions.addView(
                rechercher,
                actionParams
        );

        LinearLayout.LayoutParams actionParams2 =
                new LinearLayout.LayoutParams(
                        0,
                        110,
                        1
                );

        actionParams2.setMargins(
                7,
                0,
                0,
                0
        );

        actions.addView(
                proposer,
                actionParams2
        );

        contenu.addView(actions);

        rechercher.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Recherche de trajet",
                        Toast.LENGTH_SHORT
                ).show()
        );

        proposer.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Proposition de trajet",
                        Toast.LENGTH_SHORT
                ).show()
        );

        // ==============================
        // TRAJETS RECENTS
        // ==============================

        espace(contenu, 35);

        TextView titre =
                texte(
                        "Trajets récents",
                        20,
                        TEXTE
                );

        titre.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        contenu.addView(titre);

        espace(contenu, 13);

        ajouterTrajet(
                contenu,
                "Fianarantsoa",
                "Ambositra",
                "14 000 Ar",
                "Demain à 14:00",
                "Rabemanjary Jean Luc",
                "R"
        );

        ajouterTrajet(
                contenu,
                "Toliara",
                "Ambalavao",
                "49 000 Ar",
                "Ven. 13 Oct. à 08:30",
                "Sarah Liantsoa",
                "S"
        );

        scrollView.addView(contenu);

        racine.addView(
                scrollView,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        // ==============================
        // NAVIGATION
        // ==============================

        racine.addView(
                creerNavigation(),
                new LinearLayout.LayoutParams(
                        -1,
                        72
                )
        );

        setContentView(racine);
    }

    // =====================================================
    // CARTE TRAJET
    // =====================================================

    private void ajouterTrajet(
            LinearLayout parent,
            String depart,
            String destination,
            String prix,
            String date,
            String conducteur,
            String initiale) {

        LinearLayout carte =
                new LinearLayout(this);

        carte.setOrientation(
                LinearLayout.VERTICAL
        );

        carte.setPadding(
                16,
                14,
                16,
                14
        );

        carte.setBackground(
                fondArrondi(
                        Color.WHITE,
                        18,
                        Color.rgb(225, 229, 231),
                        1
                )
        );

        // Ligne principale
        LinearLayout ligne =
                new LinearLayout(this);

        ligne.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView trajet =
                texte(
                        depart + "   →   " + destination,
                        15,
                        TEXTE
                );

        trajet.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        ligne.addView(
                trajet,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        TextView prixText =
                texte(
                        prix,
                        16,
                        VERT_CARGO
                );

        prixText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        ligne.addView(prixText);

        carte.addView(ligne);

        // Date
        TextView dateText =
                texte(
                        "▣   " + date,
                        13,
                        TEXTE_GRIS
                );

        LinearLayout.LayoutParams dateParams =
                new LinearLayout.LayoutParams(
                        -2,
                        -2
                );

        dateParams.topMargin = 12;

        carte.addView(
                dateText,
                dateParams
        );

        // Conducteur
        LinearLayout conducteurLayout =
                new LinearLayout(this);

        conducteurLayout.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView avatar =
                creerPetitAvatar(initiale);

        conducteurLayout.addView(avatar);

        TextView conducteurText =
                texte(
                        conducteur,
                        13,
                        TEXTE
                );

        LinearLayout.LayoutParams nomParams =
                new LinearLayout.LayoutParams(
                        -2,
                        -2
                );

        nomParams.leftMargin = 8;

        conducteurLayout.addView(
                conducteurText,
                nomParams
        );

        LinearLayout.LayoutParams conducteurParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        conducteurParams.topMargin = 10;

        carte.addView(
                conducteurLayout,
                conducteurParams
        );

        LinearLayout.LayoutParams carteParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        carteParams.setMargins(
                0,
                0,
                0,
                12
        );

        parent.addView(
                carte,
                carteParams
        );
    }

    // =====================================================
    // ACTION
    // =====================================================

    private View creerAction(
            String symbole,
            String titre,
            boolean active) {

        LinearLayout carte =
                new LinearLayout(this);

        carte.setOrientation(
                LinearLayout.VERTICAL
        );

        carte.setPadding(
                14,
                12,
                14,
                10
        );

        carte.setGravity(
                Gravity.CENTER_VERTICAL
        );

        int fond = active
                ? VERT_CLAIR
                : Color.WHITE;

        carte.setBackground(
                fondArrondi(
                        fond,
                        14,
                        Color.rgb(225, 229, 231),
                        1
                )
        );

        TextView icone =
                texte(
                        symbole,
                        27,
                        VERT_CARGO
                );

        icone.setGravity(
                Gravity.CENTER
        );

        icone.setBackground(
                fondArrondi(
                        Color.WHITE,
                        12,
                        Color.TRANSPARENT,
                        0
                )
        );

        carte.addView(
                icone,
                new LinearLayout.LayoutParams(
                        42,
                        42
                )
        );

        TextView texte =
                texte(
                        titre,
                        14,
                        TEXTE
                );

        texte.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(
                        -2,
                        -2
                );

        textParams.topMargin = 7;

        carte.addView(
                texte,
                textParams
        );

        return carte;
    }

    // =====================================================
    // NAVIGATION
    // =====================================================

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

        ajouterBoutonNavigation(
                navigation,
                "⌂",
                "Accueil",
                true
        );

        ajouterBoutonNavigation(
                navigation,
                "➤",
                "Trajets",
                false
        );

        ajouterBoutonNavigation(
                navigation,
                "□",
                "Messages",
                false
        );

        ajouterBoutonNavigation(
                navigation,
                "▣",
                "Réservations",
                false
        );

        ajouterBoutonNavigation(
                navigation,
                "♙",
                "Profil",
                false
        );

        return navigation;
    }

    private void ajouterBoutonNavigation(
            LinearLayout parent,
            String icone,
            String nom,
            boolean actif) {

        LinearLayout bouton =
                new LinearLayout(this);

        bouton.setOrientation(
                LinearLayout.VERTICAL
        );

        bouton.setGravity(
                Gravity.CENTER
        );

        TextView icon =
                texte(
                        icone,
                        23,
                        actif
                                ? VERT_CARGO
                                : TEXTE_GRIS
                );

        icon.setGravity(
                Gravity.CENTER
        );

        bouton.addView(icon);

        TextView label =
                texte(
                        nom,
                        10,
                        actif
                                ? VERT_CARGO
                                : TEXTE_GRIS
                );

        label.setGravity(
                Gravity.CENTER
        );

        bouton.addView(label);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                );

        parent.addView(
                bouton,
                params
        );

        bouton.setOnClickListener(view -> {

            if (nom.contains("Profil")) {

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

    // =====================================================
    // UTILITAIRES
    // =====================================================

    private TextView texte(
            String valeur,
            float taille,
            int couleur) {

        TextView text =
                new TextView(this);

        text.setText(valeur);
        text.setTextSize(taille);
        text.setTextColor(couleur);

        return text;
    }

    private TextView creerAvatar(
            String initiale) {

        TextView avatar =
                creerPetitAvatar(initiale);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        52,
                        52
                );

        avatar.setLayoutParams(params);

        return avatar;
    }

    private TextView creerPetitAvatar(
            String initiale) {

        TextView avatar =
                texte(
                        initiale,
                        16,
                        Color.WHITE
                );

        avatar.setGravity(
                Gravity.CENTER
        );

        avatar.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        avatar.setBackground(
                fondArrondi(
                        VERT_CARGO,
                        50,
                        Color.TRANSPARENT,
                        0
                )
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        32,
                        32
                );

        avatar.setLayoutParams(params);

        return avatar;
    }

    private GradientDrawable fondArrondi(
            int couleur,
            float rayon,
            int bordure,
            int epaisseur) {

        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(couleur);
        drawable.setCornerRadius(rayon);

        if (epaisseur > 0) {
            drawable.setStroke(
                    epaisseur,
                    bordure
            );
        }

        return drawable;
    }

    private void espace(
            LinearLayout parent,
            int hauteur) {

        Space space = new Space(this);

        parent.addView(
                space,
                new LinearLayout.LayoutParams(
                        1,
                        hauteur
                )
        );
    }
}