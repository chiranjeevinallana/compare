package com.abc.compare.service;

import com.abc.compare.constants.AppConstants;
import com.abc.compare.ops.FileOps;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CompareServiceImpl implements CompareService{

    private final Map<String,String> fileProperties;

    public CompareServiceImpl(){
        fileProperties = this.loadProperties();
    }

    @Override
    public void compareFile() {

    }

    @Override
    public void compareFiles() {
        Boolean result = Boolean.FALSE;
        final String primarySourceFileLocation = fileProperties.get(AppConstants.PRIMARY_SOURCE_FILE_LOCATION);
        final String secondarySourceFileLocation = fileProperties.get(AppConstants.SECONDARY_SOURCE_FILE_LOCATION);
        final String primaryDestinationFileLocation = fileProperties.get(AppConstants.PRIMARY_DESTINATION_FILE_LOCATION);
        final String secondaryDestinationFileLocation = fileProperties.get(AppConstants.SECONDARY_DESTINATION_FILE_LOCATION);
        final String[] fileNames = this.getFileNames(fileProperties.get(AppConstants.FILE_NAMES));
        final String resultFileLocation = fileProperties.get(AppConstants.RESULT_FILE_LOCATION);
        Map<String, Boolean> resultMap = new HashMap<>();
        try {
            FileOps fileOps = new FileOps();
            for (String fileName: fileNames) {
                if(fileOps.pullFilesToLocal(primarySourceFileLocation,
                        primaryDestinationFileLocation, fileName)
                        && fileOps.pullFilesToLocal(secondarySourceFileLocation,
                        secondaryDestinationFileLocation, fileName)){
                    System.out.println("Compare Application Execution..");
                    result = fileOps.compareFileLinesCount(primaryDestinationFileLocation,
                            secondaryDestinationFileLocation, fileName);
                }
                resultMap.put(fileName, result);
            }
            System.out.println("File Comparison Completed.");
            fileOps.writeResult(resultMap, resultFileLocation);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeResult(){

    }

    private String[] getFileNames(String s) {
        String[] result = {};
        if(StringUtils.isNotEmpty(s)) {
            result = s.split(",");
        }
        return result;
    }

    private Map<String,String> loadProperties() {
        Map<String,String> fileProperties = new HashMap<>();

        try (InputStream input = FileOps.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return fileProperties;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            fileProperties.put(AppConstants.PRIMARY_SOURCE_FILE_LOCATION,
                    prop.getProperty(AppConstants.PRIMARY_SOURCE_FILE_LOCATION));
            fileProperties.put(AppConstants.SECONDARY_SOURCE_FILE_LOCATION,
                    prop.getProperty(AppConstants.SECONDARY_SOURCE_FILE_LOCATION));
            fileProperties.put(AppConstants.PRIMARY_DESTINATION_FILE_LOCATION,
                    prop.getProperty(AppConstants.PRIMARY_DESTINATION_FILE_LOCATION));
            fileProperties.put(AppConstants.SECONDARY_DESTINATION_FILE_LOCATION,
                    prop.getProperty(AppConstants.SECONDARY_DESTINATION_FILE_LOCATION));
            fileProperties.put(AppConstants.FILE_NAMES,
                    prop.getProperty(AppConstants.FILE_NAMES));
            fileProperties.put(AppConstants.RESULT_FILE_LOCATION,
                    prop.getProperty(AppConstants.RESULT_FILE_LOCATION));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fileProperties;
    }

    public static void main(String[] ar) {
        CompareServiceImpl compareService = new CompareServiceImpl();
        Map<String, String> fileProperties = compareService.loadProperties();
        System.out.println("Done");
    }
}
