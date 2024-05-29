package net.tim.db;

import net.tim.db.entity.Movie;
import net.tim.db.entity.Person;

import java.util.ArrayList;
import java.util.List;

public class DB_Manager {

    private final DatabaseConnector dbConnector;

    public DB_Manager(String pIP, int pPort, String pDatabase, String pUsername, String pPassword) {
        dbConnector = new DatabaseConnector(pIP, pPort, pDatabase, pUsername, pPassword);
        testConnection();
    }

    private void testConnection() {
        if (dbConnector.getErrorMessage() == null) {
            System.out.println("Connection successful!");
        } else {
            throw new RuntimeException("Connection failed: " + dbConnector.getErrorMessage());
        }
    }

    private void executeStatement(String statement) {
        dbConnector.executeStatement(statement);
        if (dbConnector.getErrorMessage() != null) {
            throw new RuntimeException("Error while executing statement: " + dbConnector.getErrorMessage());
        } else if (dbConnector.getCurrentQueryResult() == null) {
            throw new RuntimeException("Check db and your statement: " + statement);
        }
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();

        executeStatement("SELECT DISTINCT * FROM filmdb.film ORDER BY FilmID;");


        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            movies.add(new Movie(Long.parseLong(row[0]), row[1], row[2], Integer.parseInt(row[3]), Integer.parseInt(row[4])));
        }

        return movies;
    }

    public List<Movie> getMoviesByTitle(String title) {
        List<Movie> movies = new ArrayList<>();

        executeStatement("SELECT DISTINCT * FROM filmdb.film WHERE titel LIKE '%" + title + "%' order by FilmID;");
        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            movies.add(new Movie(Long.parseLong(row[0]), row[1], row[2], Integer.parseInt(row[3]), Integer.parseInt(row[4])));
        }

        return movies;
    }

    public List<Movie> getMoviesByActor(String actor) {
        List<Movie> movies = new ArrayList<>();

        executeStatement("SELECT DISTINCT f.* FROM filmdb.film f JOIN filmdb.mitspielen m on f.FilmID = m.filmID JOIN filmdb.person p on m.personID = p.personID WHERE p.Vorname LIKE '%" + actor + "%' or p.Nachname LIKE '%" + actor + "%'");
        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            movies.add(new Movie(Long.parseLong(row[0]), row[1], row[2], Integer.parseInt(row[3]), Integer.parseInt(row[4])));
        }

        return movies;
    }

    public List<Movie> getMoviesByGenre(String genre) {
        List<Movie> movies = new ArrayList<>();

        executeStatement("SELECT DISTINCT f.* FROM filmdb.film f JOIN filmdb.hatGenre h on f.FilmID = h.FilmID JOIN filmdb.genre g on h.GenreID = g.GenreID WHERE g.Genre LIKE '%" + genre + "%';");
        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            movies.add(new Movie(Long.parseLong(row[0]), row[1], row[2], Integer.parseInt(row[3]), Integer.parseInt(row[4])));
        }
        return movies;
    }

    public List<Movie> getMoviesByRegisseur(String regisseur) {
        List<Movie> movies = new ArrayList<>();

        executeStatement("SELECT DISTINCT f.* FROM filmdb.film f JOIN filmdb.mitwirken r on f.FilmID = r.FilmID JOIN filmdb.person p on r.PersonID = p.PersonID WHERE p.Vorname LIKE '%" + regisseur + "%' or p.Nachname LIKE '%" + regisseur + "%'");
        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            movies.add(new Movie(Long.parseLong(row[0]), row[1], row[2], Integer.parseInt(row[3]), Integer.parseInt(row[4])));
        }

        return movies;
    }

    public List<Movie> getMoviesByYear(String operator, String year) {

        if (year.isEmpty()){
            return getAllMovies();
        }

        List<Movie> movies = new ArrayList<>();

        executeStatement("SELECT DISTINCT * FROM filmdb.film WHERE filmdb.film.Erscheinungsjahr" + operator + " '" + year + "' order by FilmID");
        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            movies.add(new Movie(Long.parseLong(row[0]), row[1], row[2], Integer.parseInt(row[3]), Integer.parseInt(row[4])));
        }

        return movies;
    }

    public List<Movie> getMoviesByLength(String operator, String length) {

        if (length.isEmpty()){
            return getAllMovies();
        }

        List<Movie> movies = new ArrayList<>();

        executeStatement("SELECT DISTINCT * FROM filmdb.film WHERE Laenge " + operator + length + " order by FilmID");
        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            movies.add(new Movie(Long.parseLong(row[0]), row[1], row[2], Integer.parseInt(row[3]), Integer.parseInt(row[4])));
        }

        return movies;
    }

    public List<Movie> getMoviesByFSK(String operator, String fsk) {

        if (fsk.isEmpty()){
            return getAllMovies();
        }

        List<Movie> movies = new ArrayList<>();

        executeStatement("SELECT DISTINCT * FROM filmdb.film WHERE Altersfreigabe " + operator + fsk + " order by FilmID");
        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            movies.add(new Movie(Long.parseLong(row[0]), row[1], row[2], Integer.parseInt(row[3]), Integer.parseInt(row[4])));
        }

        return movies;
    }

    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<>();

        executeStatement("SELECT DISTINCT * FROM filmdb.person ORDER BY PersonID");

        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            persons.add(new Person(Long.parseLong(row[0]), row[1], row[2], row[3], row[4], row[5]));
        }

        return persons;
    }

    public List<Person> getPersonsByName(String name) {
        List<Person> persons = new ArrayList<>();

        executeStatement("SELECT DISTINCT * FROM filmdb.person WHERE Vorname LIKE '%" + name + "%' OR Nachname LIKE '%" + name + "%' ORDER BY PersonID");

        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            persons.add(new Person(Long.parseLong(row[0]), row[1], row[2], row[3], row[4], row[5]));
        }

        return persons;
    }

    public List<Person> getPersonsByBirthdate(String operator, String birthdate) {
        List<Person> persons = new ArrayList<>();

        executeStatement("SELECT DISTINCT * FROM filmdb.person WHERE Geburtsdatum " + operator + " '" + birthdate + "' ORDER BY PersonID");

        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            persons.add(new Person(Long.parseLong(row[0]), row[1], row[2], row[3], row[4], row[5]));
        }

        return persons;
    }

    public List<Person> getPersonsByCountry(String country) {
        List<Person> persons = new ArrayList<>();

        executeStatement("SELECT DISTINCT * FROM filmdb.person WHERE Land LIKE '%" + country + "%' ORDER BY PersonID");

        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            persons.add(new Person(Long.parseLong(row[0]), row[1], row[2], row[3], row[4], row[5]));
        }

        return persons;
    }

    public List<Person> getPersonsByGender(String gender) {
        List<Person> persons = new ArrayList<>();

        executeStatement("SELECT DISTINCT * FROM filmdb.person WHERE geschlecht LIKE '%" + gender + "%' order by PersonID");

        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            persons.add(new Person(Long.parseLong(row[0]), row[1], row[2], row[3], row[4], row[5]));
        }

        return persons;
    }

    public String getCast(Movie movie, Person person) {
        String role = null;

        executeStatement("SELECT DISTINCT rolle FROM mitspielen where filmID = " + movie.getId() + " AND personID = " + person.getId() + ";");
        String[][] result = dbConnector.getCurrentQueryResult().getData();

        if (result.length > 0)
            role = result[0][0];

        return role;
    }

    public List<Movie> getMoviesByPerson(Person person) {
        List<Movie> movies = new ArrayList<>();

        executeStatement("SELECT DISTINCT f.* FROM filmdb.film f JOIN filmdb.mitspielen m on f.FilmID = m.filmID JOIN filmdb.person p on m.personID = p.personID WHERE p.personID = " + person.getId() + ";");
        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            movies.add(new Movie(Long.parseLong(row[0]), row[1], row[2], Integer.parseInt(row[3]), Integer.parseInt(row[4])));
        }

        return movies;
    }

    public List<Person> getPersonsByMovie(Movie movie) {
        List<Person> persons = new ArrayList<>();

        executeStatement("SELECT DISTINCT p.* FROM filmdb.person p JOIN filmdb.mitspielen m on p.PersonID = m.personID WHERE m.filmID = " + movie.getId() + ";");
        String[][] result = dbConnector.getCurrentQueryResult().getData();

        for (String[] row : result) {
            persons.add(new Person(Long.parseLong(row[0]), row[1], row[2], row[3], row[4], row[5]));
        }

        return persons;
    }


}