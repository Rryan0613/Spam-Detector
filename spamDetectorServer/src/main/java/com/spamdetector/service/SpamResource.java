package com.spamdetector.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jdk.incubator.vector.VectorOperators;

@Path("/spam")
public class SpamResource {

    //    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();
    ObjectMapper mapper = new ObjectMapper();
    List<TestFile> testResults = new ArrayList<>();

    SpamResource()throws IOException{
//      TODO: load resources, train and test to improve performance on the endpoint calls
        System.out.print("Training and testing the model, please wait");

//      TODO: call  this.trainAndTest();
        this.trainAndTest();
    }

    @GET
    @Produces("application/json")
    public Response getSpamResults() {
//       TODO: return the test results list of TestFile, return in a Response object
        List<Map<String,String>> results = new ArrayList<>();
        for (TestFile testFile : testResults) {
            String spamProbRounded = String.format("%.5f", testFile.getSpamProbability());
            String fileName = testFile.getFilename();
            String spamProbability = String.valueOf(testFile.getSpamProbability());
            String actualClass = testFile.getActualClass();
            Map<String, String> resultMap = Map.of(
                    "spamProbRounded", spamProbRounded,
                    "fileName", fileName,
                    "spamProbability", spamProbability,
                    "actualClass", actualClass
            );
            results.add(resultMap);
        }
        return Response.ok(results).build();
    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() {
        try {
            List<TestFile> testFiles = detector.trainAndTest(new File("data/test"));
            int truePositives = 0;
            int falsePositives = 0;
            int trueNegative = 0;
            int placeholder = 0;
            double accuracy = 0;
            double threshold = 0.5;

            for (TestFile file : testFiles) {
                if (file.getSpamProbability() >= threshold) {
                    String actualClass = file.getActualClass();
                    if (actualClass.equals("/spam")) {
                        truePositives++;
                    } else if (actualClass.equals("/ham")) {
                        falsePositives++;
                    }
                }

                if (file.getSpamProbability() <= threshold) {
                    String actualClass = file.getActualClass();
                    if (actualClass.equals("/spam")) {
                        placeholder++;
                    } else if (actualClass.equals("/ham")) {
                        trueNegative++;
                    }
                }
            }
            accuracy = (double) truePositives / (truePositives + falsePositives);
            accuracy = truePositives + trueNegative;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
        //      TODO: return the precision of the detector, return in a Response object
        List<TestFile> testFiles = detector.trainAndTest(new File("data/test")); // replace with actual path
        int truePositives = 0;
        int falsePositives = 0;
        int trueNegative = 0;
        int placeholder = 0;
        double precision = 0;
        double threshold = 0.5;

        for (TestFile file : testFiles) {
            if (file.getSpamProbability() >= threshold) {
                String actualClass = file.getActualClass();
                if (actualClass.equals("/spam")) {
                    truePositives++;
                } else if (actualClass.equals("/ham")) {
                    falsePositives++;
                }
            }

            if (file.getSpamProbability() <= threshold) {
                String actualClass = file.getActualClass();
                if (actualClass.equals("/spam")) {
                    placeholder++;
                    /*
                    placeholder is here because we don't need anything else other than truePositive, falsePositives,
                    and trueNegative, so it will just be an empty variable
                    */
                } else if (actualClass.equals("/ham")) {
                    trueNegative++;
                }
            }
        }
        precision = truePositives/ falsePositives+truePositives; // should the precision function be a array or a list


        return Response.ok().build();
    }

    private List<TestFile> trainAndTest() throws IOException {
        if (this.detector==null){
            this.detector = new SpamDetector();
        }

//        TODO: load the main directory "data" here from the Resources folder
        File mainDirectory = null;
        return this.detector.trainAndTest(mainDirectory);
    }
}