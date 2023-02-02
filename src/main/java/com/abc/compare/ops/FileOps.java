package com.abc.compare.ops;

import com.abc.compare.constants.AppConstants;
import com.abc.compare.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileOps {

    private final FileUtil fileUtil;
    private final Map<String,String> fileProperties;

    public FileOps(){
        fileUtil = new FileUtil();
        fileProperties = this.loadProperties();
    }

    public Boolean pullFilesToLocal(){
        if (fileProperties == null || fileProperties.isEmpty()) {
            System.out.println("Properties not defined properly.");
            return Boolean.FALSE;
        }
        System.out.println("Coping files to local..");
        try {
            fileUtil.copyFile(fileProperties.get(AppConstants.SOURCE_FILE_LOCATION)
                            + fileProperties.get(AppConstants.SOURCE_FILE_NAME),
                    fileProperties.get(AppConstants.DESTINATION_FILE_LOCATION)
                            + fileProperties.get(AppConstants.SOURCE_FILE_NAME));
            fileUtil.copyFile(fileProperties.get(AppConstants.SOURCE_FILE_LOCATION)
                            + fileProperties.get(AppConstants.DESTINATION_FILE_NAME),
                    fileProperties.get(AppConstants.DESTINATION_FILE_LOCATION)
                            + fileProperties.get(AppConstants.DESTINATION_FILE_NAME));
            System.out.println("Coping files to local completed..");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Boolean.TRUE;
    }

    public Boolean compareFileLinesCount() throws IOException {
        Boolean result = Boolean.FALSE;
        if (fileProperties == null || fileProperties.isEmpty()) {
            System.out.println("Properties not defined properly.");
            return Boolean.FALSE;
        }

        final long lineNo = fileUtil.filesCompareByLine(
                Path.of(fileProperties.get(AppConstants.DESTINATION_FILE_LOCATION)
                        + fileProperties.get(AppConstants.SOURCE_FILE_NAME)),
                Path.of(fileProperties.get(AppConstants.DESTINATION_FILE_LOCATION)
                        + fileProperties.get(AppConstants.DESTINATION_FILE_NAME))
        );

        if (lineNo == -1) {
            result = Boolean.TRUE;
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
            fileProperties.put(AppConstants.SOURCE_FILE_LOCATION,
                    prop.getProperty(AppConstants.SOURCE_FILE_LOCATION));
            fileProperties.put(AppConstants.DESTINATION_FILE_LOCATION,
                    prop.getProperty(AppConstants.DESTINATION_FILE_LOCATION));
            fileProperties.put(AppConstants.SOURCE_FILE_NAME,
                    prop.getProperty(AppConstants.SOURCE_FILE_NAME));
            fileProperties.put(AppConstants.DESTINATION_FILE_NAME,
                    prop.getProperty(AppConstants.DESTINATION_FILE_NAME));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return fileProperties;
    }

    public static void main(String[] ar){
        FileOps fileOps = new FileOps();
        Map<String,String> fileProperties = fileOps.loadProperties();
        System.out.println("Done");
    }
}
