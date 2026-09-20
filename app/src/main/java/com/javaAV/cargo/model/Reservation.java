package com.javaAV.cargo.model;

public class Reservation {

    private String dateReservation;
    private int id;
    private int nombrePlaces;
    private String passager;
    private String statut;
    private Trajet trajet;

    public Reservation(
            String dateReservation,
            int id,
            int nombrePlaces,
            String passager,
            String statut,
            Trajet trajet
    ) {
        this.dateReservation = dateReservation;
        this.id = id;
        this.nombrePlaces = nombrePlaces;
        this.passager = passager;
        this.statut = statut;
        this.trajet = trajet;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public int getId() {
        return id;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public String getPassager() {
        return passager;
    }

    public String getStatut() {
        return statut;
    }

    public Trajet getTrajet() {
        return trajet;
    }
}