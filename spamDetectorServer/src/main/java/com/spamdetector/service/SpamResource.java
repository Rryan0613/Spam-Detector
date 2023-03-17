package com.spamdetector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.core.Response;



@Path("/spam")
public class SpamResource {
    // URL of the directory containing the test files
    URL url = getClass().getResource("/spam");

    // Object mapper for converting Java objects to JSON strings
    ObjectMapper mapper = new ObjectMapper();

    // List of TestFile objects containing the results of training and testing the SpamDetector
    List<TestFile> testResults;

    // Instance of SpamDetector responsible for all the spam detection logic
    SpamDetector detector = new SpamDetector();

    /**
     * Constructor that loads resources, trains and tests the SpamDetector to improve
     * performance on endpoint calls.
     */
    public SpamResource() throws IOException {
        File mainDirectory = new File("/data");

        // Train and test the detector on the files in the "/data" directory
        try {
            this.detector.trainAndTest(mainDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Endpoint for getting the frequency of words in the test files.
     * Returns the frequency map as a JSON string wrapped in a Response object.
     */
    @GET
    @Produces("application/json")
    public Response getSpamResults() throws IOException {
        // Convert the URL of the test file directory to a File object
        File data;
        try {
            data = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Calculate the frequency of words in the test files using the SpamDetector
        SpamDetector parser = new SpamDetector();
        Map<String, Integer> freq = parser.wordFreqDir(data);

        // Create a Response object containing the frequency map as a JSON string
        Response response = Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(mapper.writeValueAsString(freq))
                .build();

        return response;
    }

    /**
     * Endpoint for getting the accuracy of the SpamDetector on the test files.
     * Returns the accuracy as a JSON string wrapped in a Response object.
     */
    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() throws IOException {
        // Convert the URL of the test file directory to a File object
        File mainDirectory;
        try {
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Train and test the detector on the files in the "/data" directory
        testResults = detector.trainAndTest(mainDirectory);

        // Compute the accuracy of the detector on the test files
        Map<String, Double> accuracy = detector.getAccuracy(testResults);

        // Convert the accuracy to a JSON string
        String json = mapper.writeValueAsString(accuracy);

        // Create a Response object containing the accuracy as a JSON string
        Response response = Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(json)
                .build();

        // Return the Response object
        return response;
    }

    public List<TestFile> trainAndTest() throws IOException {
        if (this.detector==null){
            this.detector = new SpamDetector();
        }
        //      TODO: return the precision of the detector, return in a Response object
        URL url = getClass().getResource("/data");

        // Create a new File object to represent the "/data" directory
        File mainDirectory;
        try {
            assert url != null;
            mainDirectory = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

//        TODO: load the main directory "data" here from the Resources folder
        return this.detector.trainAndTest(mainDirectory);
    }






}