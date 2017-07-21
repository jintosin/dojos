package com.tobi.movies.steps;

import android.support.test.InstrumentationRegistry;

import com.tobi.movies.Dependencies;
import com.tobi.movies.EspressoDependencies;
import com.tobi.movies.MovieApplication;
import com.tobi.movies.backend.ConfigurableBackend;
import com.tobi.movies.posterdetails.ApiMovieDetails;
import com.tobi.movies.posterdetails.PosterDetailsRobot;

import java.util.Map;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MovieDetailsSteps {

    private final PosterDetailsRobot posterDetailsRobot = PosterDetailsRobot.create();

    @Given("^the following remote movie details exist$")
    public void the_following_remote_movie_details_exist(final DataTable dataTable) {
        extractMovieDetailsFromDataTable(dataTable);
    }

    private void extractMovieDetailsFromDataTable(DataTable dataTable) {
        ConfigurableBackend configurableBackend = getConfigurableBackend();

        for (final Map<String, String> row : dataTable.asMaps(String.class, String.class)) {

            Long movieId = Long.valueOf(row.get("movieId"));
            String posterPath = row.get("posterPath");
            String title = row.get("title");
            String description = row.get("description");
            String releaseDate = row.get("releaseDate");

            configurableBackend.addMovieDetails(createMovieDetails(movieId, title, description, posterPath, releaseDate));
        }
    }

    private ApiMovieDetails createMovieDetails(long movieId, String movieTitle, String movieOverview, String posterPath, String releaseDate) {
        ApiMovieDetails apiMovieDetails = new ApiMovieDetails();
        apiMovieDetails.originalTitle = movieTitle;
        apiMovieDetails.movieId = movieId;
        apiMovieDetails.overview = movieOverview;
        apiMovieDetails.posterPath = posterPath;
        apiMovieDetails.releaseDate = releaseDate;
        return apiMovieDetails;
    }

    private ConfigurableBackend getConfigurableBackend() {
        MovieApplication movieApplication = (MovieApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        Dependencies dependencies = movieApplication.getDependencies();
        return ((EspressoDependencies) dependencies).getConfigurableBackend();
    }

    @When("^I show the detail screen for movie with id (\\d+)$")
    public void iShowTheDetailScreenForMovieWithId(int movieId) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        posterDetailsRobot.launchDetailsScreen(movieId);
    }

    @Then("^I expect to see \"([^\"]*)\" as the release date$")
    public void iExpectToSeeAsTheReleaseDate(String releaseDate) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        posterDetailsRobot.checkMovieReleaseDateIsDisplayed(releaseDate);
    }
}