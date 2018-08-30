package com.example.agenda.tfgagenda.model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class Event {

    private int id;
    private String name;
    private String owner;
    private String latitud;
    private String longitud;
    private String eventDate;
    private String nomParticipantss;
    public String eventDateFinish;
    public String hourStart;
    public String hourEnd;

    public String getNomParticipantss() {
        return nomParticipantss;
    }

    public void setNomParticipantss(String nomParticipantss) {
        this.nomParticipantss = nomParticipantss;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nom) {
        this.name = nom;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
    public String getEventDateFinish() {
        return eventDateFinish;
    }

    public void setEventDateFinish(String eventDateFinish) {
        this.eventDateFinish = eventDateFinish;
    }

    public String getHourStart() {
        return hourStart;
    }

    public void setHourStart(String hourStart) {
        this.hourStart = hourStart;
    }

    public String getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(String hourEnd) {
        this.hourEnd = hourEnd;
    }
}
