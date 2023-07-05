package com.spamdetector.service;

import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import jakarta.ws.rs.core.Response;

import org.json.*;


@Path("/spam")
public class SpamResource {

//    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();
    List<TestFile> testFiles;

    public SpamResource() throws IOException, URISyntaxException {
        System.out.print("Training and testing the model, please wait");
        this.testFiles = this.trainAndTest();
    }
    
    @GET
    @Produces("application/json")
    public Response getSpamResults() {
//       TODO: return the test results list of TestFile, return in a Response object
        JSONArray data = new JSONArray();
        for(TestFile testFile : this.testFiles){
            JSONObject obj = new JSONObject();
            String spamProbRounded = String.format(String.valueOf(testFile.getSpamProbability()),"%.5f");
            obj.put("spamProbRounded",spamProbRounded);
            obj.put("file",testFile.getFilename());
            obj.put("spamProbability",testFile.getSpamProbability());
            obj.put("actualClass",testFile.getActualClass());
            data.put(obj);
        }
        return Response
                .status(200)
                .header("Access-Control-Allow-Origin","*")
                .header("Content-Type","application/json")
                .entity(data.toString())
                .build();
    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() {
        JSONObject accuracyJson = new JSONObject();
        int numTruePositives = 0;
        int numTrueNegatives = 0;
        int numFiles = testFiles.size();
        for (TestFile file : this.testFiles) {
            // access positives and negatives from TestFile
            // positive is spam, negative is ham
            // truepositive is spam probability (>=0.5) and class is spam
            // truenegative is ham probability (<0.5) and class is ham
            // falsepostive is spam probablity (>=0.5) and class is ham
            if (file.getSpamProbability() >= 0.5 && Objects.equals(file.getActualClass().toLowerCase(), "spam")) {
                numTruePositives += 1;
            }
            if (file.getSpamProbability() < 0.5 && Objects.equals(file.getActualClass().toLowerCase(), "ham")) {
                numTrueNegatives += 1;
            }
        }
        double accuracy;
        try {
            accuracy = ((double) numTruePositives + (double) numTrueNegatives) / (double) numFiles;
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Zero Division Error");
        }

        accuracyJson.put("accuracy", accuracy);
        String data = accuracyJson.toString();


        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .entity(data)
                .build();
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
        JSONObject precisionJson = new JSONObject();
//        JSONArray array = new JSONArray();
//        JSONObject item = new JSONObject();

        int numTruePositives = 0;
        int numFalsePositives = 0;

        for (TestFile file : this.testFiles) {
            // do stuff
            // access positives and negatives from TestFile
            // positive is spam, negative is ham
            // truepositive is spam probability (>=0.5) and class is spam
            // truenegative is ham probability (<0.5) and class is ham
            // falsepostive is spam probablity (>=0.5) and class is ham
            if (file.getSpamProbability() >= 0.5 && Objects.equals(file.getActualClass().toLowerCase(), "spam")) {
                numTruePositives += 1;
            }
            if (file.getSpamProbability() >= 0.5 && Objects.equals(file.getActualClass().toLowerCase(), "ham")) {
                numFalsePositives += 1;
            }
        }
        Double precision;
        try {
            precision = (double) numTruePositives / ((double) numTruePositives + (double) numFalsePositives);
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Zero Division Error");
        }

        precisionJson.put("precision",precision);
        String data = precisionJson.toString();

        return Response.status(200)
                .header("Access-Control-Allow-Origin","*")
                .header("Content-Type","application/json")
                .entity(data)
                .build();
    }

    private List<TestFile> trainAndTest() throws IOException, URISyntaxException {
        if (this.detector == null) {
            this.detector = new SpamDetector();
        }
        URL resource = this.getClass().getClassLoader().getResource("/data");
        if(resource == null){
            throw new NullPointerException("File not found!");
        }
        File mainDirectory;
        try {
            mainDirectory = new File(resource.toURI());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("File not found!");
        }
        return this.detector.trainAndTest(mainDirectory);
    }
}