package net.tim.quiz;

import net.tim.db.DB_Manager;
import net.tim.db.entity.Movie;
import net.tim.db.entity.Person;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class QuizQuestionGenerator {

    DB_Manager dbManager;
    List<Movie> allMovies;
    List<Person> allPersons;
    List<QuizQuestion> allQuestions;
    boolean QuestionGenerationIsDone = false;

    public QuizQuestionGenerator(DB_Manager dbManager) {
        this.dbManager = dbManager;
        this.allMovies = dbManager.getAllMovies();
        this.allPersons = dbManager.getAllPersons();

        generateXQuestions(100);
    }

    public void generateXQuestions(int amount) {
        QuestionGenerationIsDone = false;

        SwingWorker<List<QuizQuestion>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<QuizQuestion> doInBackground() {
                // This code will be executed in a background thread
                return generateQuestions(amount);
            }

            @Override
            protected void done() {
                try {
                    // This method will be called when the background
                    // thread finishes execution
                    allQuestions = get();
                    QuestionGenerationIsDone = true;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    public List<QuizQuestion> getQuestions(int amount) {
        if (!QuestionGenerationIsDone) {
            throw new IllegalStateException("Questions are still being generated");
        } else if (amount > allQuestions.size()) {
            throw new IllegalArgumentException("Not enough questions available");
        }

        List<QuizQuestion> questions = allQuestions.subList(0, amount);
        allQuestions = allQuestions.subList(amount, allQuestions.size()); // Remove used questions
        return questions;
    }

    private List<QuizQuestion> generateQuestions(int amount) {
        List<QuizQuestion> questions = new ArrayList<>();

        // Generate questions in parallel
        questions.addAll(generateWhichActorInCastInMovieQuestions().parallelStream().toList());
        questions.addAll(generateWhichMovieHasCastQuestion().parallelStream().toList());
        questions.addAll(generateWhichYearWasMovieReleasedQuestions().parallelStream().toList());
        questions.addAll(generateWhatIsTheLengthOfMovieQuestions().parallelStream().toList());
        questions.addAll(generateWhatIsTheFSKOfMovieQuestions().parallelStream().toList());
        questions.addAll(generateInWhichMovieDidActorPlayQuestions().parallelStream().toList());
        questions.addAll(generateWhoWasBornInCountryQuestions().parallelStream().toList());
        questions.addAll(generateWhoWasBornOnDateQuestions().parallelStream().toList());
        questions.addAll(generateWhatIsTheGenderOfActorQuestions().parallelStream().toList());
        questions.addAll(generateWhoWorkedOnMovieInRoleQuestions().parallelStream().toList());

        Collections.shuffle(questions);

        //remove the second half of the questions if the list is too long
        if (questions.size() > amount) {
            questions = questions.subList(0, amount);
        }

        return questions;
    }

    private List<QuizQuestion> generateWhichActorInCastInMovieQuestions() {
        List<QuizQuestion> questions = new ArrayList<>();
        List<Movie> movies = allMovies;

        for (Movie movie : movies) {
            List<Person> cast = dbManager.getPersonsByMovie(movie);
            for (Person person : cast) {
                String question = "Which actor was in the cast of the movie " + movie.getTitel() + "?";
                String correctAnswer = person.getFirstName() + " " + person.getLastName();
                String[] answers = generateFakePersons(correctAnswer); // Generate fake persons
                QuizQuestion quizQuestion = new QuizQuestion(question, answers, correctAnswer);
                questions.add(quizQuestion);
            }
        }

        return questions;
    }

    private List<QuizQuestion> generateWhichMovieHasCastQuestion() {
        List<QuizQuestion> questions = new ArrayList<>();
        List<Movie> movies = allMovies;

        for (Movie movie : movies) {
            List<Person> cast = dbManager.getPersonsByMovie(movie);
            for (Person person : cast) {
                String question = "Who is in the cast of the movie " + movie.getTitel() + "?";
                String correctAnswer = person.getFirstName() + " " + person.getLastName();
                String[] answers = generateFakePersons(correctAnswer); // Generate fake persons
                QuizQuestion quizQuestion = new QuizQuestion(question, answers, correctAnswer);
                questions.add(quizQuestion);
            }
        }

        return questions;
    }

    private String[] generateFakePersons(String correctPerson) {
        String[] fakePersons = new String[4];
        fakePersons[0] = correctPerson; // The first answer is the correct one

        // Generate fake persons
        List<Person> persons = allPersons;
        List<String> personNames = new ArrayList<>();
        for (Person person : persons) {
            personNames.add(person.getFirstName() + " " + person.getLastName());
        }
        personNames.remove(correctPerson);
        Collections.shuffle(personNames);

        for (int i = 1; i < 4; i++) {
            fakePersons[i] = personNames.get(i);
        }

        // Shuffle the answers so the correct one is not always the first one
        Collections.shuffle(Arrays.asList(fakePersons));

        return fakePersons;
    }

    private List<QuizQuestion> generateWhichYearWasMovieReleasedQuestions() {
        List<QuizQuestion> questions = new ArrayList<>();
        List<Movie> movies = allMovies;

        for (Movie movie : movies) {
            String question = "In which year was the movie " + movie.getTitel() + " released?";
            String correctAnswer = String.valueOf(movie.getPublicationDate());
            String[] answers = generateFakeYears(correctAnswer); // Generate fake years
            QuizQuestion quizQuestion = new QuizQuestion(question, answers, correctAnswer);
            questions.add(quizQuestion);
        }

        return questions;
    }

    private String[] generateFakeYears(String correctYear) {
        String[] fakeYears = new String[4];
        fakeYears[0] = correctYear; // The first answer is the correct one

        // Generate fake years
        for (int i = 1; i < 4; i++) {
            fakeYears[i] = String.valueOf(new Random().nextInt(50) + 1970); // Random year between 1970 and 2020
        }

        // Shuffle the answers so the correct one is not always the first one
        Collections.shuffle(Arrays.asList(fakeYears));

        return fakeYears;
    }

    private List<QuizQuestion> generateWhatIsTheLengthOfMovieQuestions() {
        List<QuizQuestion> questions = new ArrayList<>();
        List<Movie> movies = allMovies;

        for (Movie movie : movies) {
            String question = "What is the length of the movie " + movie.getTitel() + "?";
            String correctAnswer = String.valueOf(movie.getLength());
            String[] answers = generateFakeLengths(correctAnswer); // Generate fake lengths
            QuizQuestion quizQuestion = new QuizQuestion(question, answers, correctAnswer);
            questions.add(quizQuestion);
        }

        return questions;
    }

    private String[] generateFakeLengths(String correctLength) {
        String[] fakeLengths = new String[4];
        fakeLengths[0] = correctLength; // The first answer is the correct one

        // Generate fake lengths
        for (int i = 1; i < 4; i++) {
            fakeLengths[i] = String.valueOf(new Random().nextInt(200) + 50); // Random length between 50 and 250
        }

        // Shuffle the answers so the correct one is not always the first one
        Collections.shuffle(Arrays.asList(fakeLengths));

        return fakeLengths;
    }

    private List<QuizQuestion> generateWhatIsTheFSKOfMovieQuestions() {
        List<QuizQuestion> questions = new ArrayList<>();
        List<Movie> movies = allMovies;

        for (Movie movie : movies) {
            String question = "What is the FSK of the movie " + movie.getTitel() + "?";
            String correctAnswer = String.valueOf(movie.getFsk());
            String[] answers = generateFakeFSKs(correctAnswer); // Generate fake FSKs
            QuizQuestion quizQuestion = new QuizQuestion(question, answers, correctAnswer);
            questions.add(quizQuestion);
        }

        return questions;
    }

    private String[] generateFakeFSKs(String correctFSK) {
        correctFSK = correctFSK.trim();
        String[] fakeFSKs = new String[4];
        fakeFSKs[0] = correctFSK; // The first answer is the correct one

        // Generate fake FSKs
        List<String> FSKs = new ArrayList<>(Arrays.asList("0", "6", "12", "16", "18"));
        FSKs.remove(correctFSK); // Remove the correct FSK (to avoid duplicates)
        Collections.shuffle(FSKs);

        for (int i = 1; i < 4; i++) {
            fakeFSKs[i] = FSKs.get(i-1);
        }

        // Shuffle the answers so the correct one is not always the first one
        Collections.shuffle(Arrays.asList(fakeFSKs));

        return fakeFSKs;
    }

    private List<QuizQuestion> generateInWhichMovieDidActorPlayQuestions() {
        List<QuizQuestion> questions = new ArrayList<>();
        List<Person> persons = allPersons;

        for (Person person : persons) {
            List<Movie> movies = dbManager.getMoviesByPerson(person);
            for (Movie movie : movies) {
                String question = "In which movie did " + person.getFirstName() + " " + person.getLastName() + " play?";
                String correctAnswer = movie.getTitel();
                String[] answers = generateFakeMovies(correctAnswer); // Generate fake movies
                QuizQuestion quizQuestion = new QuizQuestion(question, answers, correctAnswer);
                questions.add(quizQuestion);
            }
        }

        return questions;
    }

    private String[] generateFakeMovies(String correctMovie) {
        String[] fakeMovies = new String[4];
        fakeMovies[0] = correctMovie; // The first answer is the correct one

        // Generate fake movies
        List<Movie> movies = allMovies;
        List<String> movieTitles = new ArrayList<>();
        for (Movie movie : movies) {
            movieTitles.add(movie.getTitel());
        }
        movieTitles.remove(correctMovie);
        Collections.shuffle(movieTitles);

        for (int i = 1; i < 4; i++) {
            fakeMovies[i] = movieTitles.get(i);
        }

        // Shuffle the answers so the correct one is not always the first one
        Collections.shuffle(Arrays.asList(fakeMovies));

        return fakeMovies;
    }

    private List<QuizQuestion> generateWhoWasBornInCountryQuestions() {
        List<QuizQuestion> questions = new ArrayList<>();
        List<Person> persons = allPersons;

        for (Person person : persons) {
            String question = "In which country was " + person.getFirstName() + " " + person.getLastName() + " born?";
            String correctAnswer = person.getCountry();
            String[] answers = generateFakeCountries(correctAnswer); // Generate fake countries
            QuizQuestion quizQuestion = new QuizQuestion(question, answers, correctAnswer);
            questions.add(quizQuestion);
        }

        return questions;
    }

    private String[] generateFakeCountries(String correctCountry) {
        String[] fakeCountries = new String[4];
        fakeCountries[0] = correctCountry; // The first answer is the correct one

        // Generate fake countries
        List<String> countries = new ArrayList<>(Arrays.asList("USA", "Germany", "France", "Italy", "Spain", "China", "Russia", "Australia", "Canada", "Brazil"));
        countries.remove(correctCountry);
        Collections.shuffle(countries);

        for (int i = 1; i < 4; i++) {
            fakeCountries[i] = countries.get(i);
        }

        // Shuffle the answers so the correct one is not always the first one
        Collections.shuffle(Arrays.asList(fakeCountries));

        return fakeCountries;
    }

    private List<QuizQuestion> generateWhoWasBornOnDateQuestions() {
        List<QuizQuestion> questions = new ArrayList<>();
        List<Person> persons = allPersons;

        for (Person person : persons) {
            String question = "When was " + person.getFirstName() + " " + person.getLastName() + " born?";
            String correctAnswer = person.getDateOfBirth();
            String[] answers = generateFakeBirthdates(correctAnswer); // Generate fake birthdates
            QuizQuestion quizQuestion = new QuizQuestion(question, answers, correctAnswer);
            questions.add(quizQuestion);
        }

        return questions;
    }

    private String[] generateFakeBirthdates(String correctBirthdate) {
        String[] fakeBirthdates = new String[4];
        fakeBirthdates[0] = correctBirthdate; // The first answer is the correct one

        // Generate fake birthdates
        for (int i = 1; i < 4; i++) {
            // This is a simple way to generate fake birthdates. You might want to improve this to make the fake birthdates more believable.
            fakeBirthdates[i] = "19" + (new Random().nextInt(99) + 1) + "-" + (new Random().nextInt(12) + 1) + "-" + (new Random().nextInt(28) + 1);
        }

        // Shuffle the answers so the correct one is not always the first one
        Collections.shuffle(Arrays.asList(fakeBirthdates));

        return fakeBirthdates;
    }

    //TODO: Gender is not similar in all Actor datasets
    private List<QuizQuestion> generateWhatIsTheGenderOfActorQuestions() {
        List<QuizQuestion> questions = new ArrayList<>();
        List<Person> actors = allPersons;

        for (Person actor : actors) {
            String question = "What is the gender of " + actor.getFirstName() + " " + actor.getLastName() + "?";
            String[] answers = {"Male", "Female", "Other", "Unknown"};
            String correctAnswer = actor.getGender();
            QuizQuestion quizQuestion = new QuizQuestion(question, answers, correctAnswer);
            questions.add(quizQuestion);
        }

        return questions;
    }

    private List<QuizQuestion> generateWhoWorkedOnMovieInRoleQuestions() {
        List<QuizQuestion> questions = new ArrayList<>();
        List<Movie> movies = allMovies;
        List<Person> persons = allPersons;

        for (Movie movie : movies) {
            for (Person person : persons) {
                String role = dbManager.getCast(movie, person);
                if (role != null) {
                    String question = "What role did " + person.getFirstName() + " " + person.getLastName() + " play in the movie " + movie.getTitel() + "?";
                    String[] answers = {"Actor", "Director", "Producer", "Writer"};
                    String correctAnswer = role;
                    QuizQuestion quizQuestion = new QuizQuestion(question, answers, correctAnswer);
                    questions.add(quizQuestion);
                }
            }
        }

        return questions;
    }

}
