package net.tim.db.entity;

public class Movie {

    Long id;
    String publicationDate;
    String titel;
    int length;
    int fsk;

    public Movie(Long id, String publicationDate, String titel, int length, int fsk) {
        this.id = id;
        this.publicationDate = publicationDate;
        this.titel = titel;
        this.length = length;
        this.fsk = fsk;
    }

    public Long getId() {
        return id;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getTitel() {
        return titel;
    }

    public int getLength() {
        return length;
    }

    public int getFsk() {
        return fsk;
    }
}
