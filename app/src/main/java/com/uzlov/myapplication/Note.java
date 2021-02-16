package com.uzlov.myapplication;

import java.util.Date;
import java.util.UUID;

public class Note {

    private UUID uuid;
    private String name;
    private String description;
    private Date dateCreate;
    private String autor;

    public Note(String name) {
        this.name = name;
    }

    public Note(String name, String description, Date dateCreate, String autor) {
        this.name = name;
        this.description = description;
        this.dateCreate = dateCreate;
        this.autor = autor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
