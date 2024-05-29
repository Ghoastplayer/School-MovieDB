package net.tim.db.entity;

public class Person {

    Long id;
    String firstName;
    String lastName;
    String dateOfBirth;
    String country;
    String gender;

    public Person(Long id, String firstName, String lastName, String dateOfBirth, String country, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public String getGender() {
        return gender;
    }
}
