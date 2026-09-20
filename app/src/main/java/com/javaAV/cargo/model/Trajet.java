package com.javaAV.cargo.model;

public class Trajet {

    private String conducteur;
    private String date;
    private String depart;
    private String destination;
    private String heure;
    private int id;
    private int places;
    private double prix;
    private String statut;

    public Trajet(
            String conducteur,
            String date,
            String depart,
            String destination,
            String heure,
            int id,
            int places,
            double prix,
            String statut
    ) {
        this.conducteur = conducteur;
        this.date = date;
        this.depart = depart;
        this.destination = destination;
        this.heure = heure;
        this.id = id;
        this.places = places;
        this.prix = prix;
        this.statut = statut;
    }

    public String getConducteur() {
        return conducteur;
    }

    public String getDate() {
        return date;
    }

    public String getDepart() {
        return depart;
    }

    public String getDestination() {
        return destination;
    }

    public String getHeure() {
        return heure;
    }

    public int getId() {
        return id;
    }

    public int getPlaces() {
        return places;
    }

    public double getPrix() {
        return prix;
    }

    public String getStatut() {
        return statut;
    }
}