package com.uzlov.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.uzlov.myapplication.R;

import java.util.Date;
import java.util.Objects;

public class Note implements Parcelable {

    private String id;
    private String name;
    private String description;
    private String dateCreate;
    private String author;

    public Note() {
    }

    public Note(String id, String name) {
        this.setId(id);
        this.setName(name);
        this.setDateCreate(new Date().toString());
        this.setAuthor("Неизвестный");
        this.setDescription("");
    }

    public Note(String id, String name, String description, String dateCreate, String author) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setDateCreate(dateCreate);
        this.setAuthor(author);
    }


    protected Note(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        dateCreate = in.readString();
        author = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(dateCreate);
        dest.writeString(author);
    }

    public int getImage() {
        return R.drawable.ic_account_24;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id.equals(note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, dateCreate, author);
    }
}
