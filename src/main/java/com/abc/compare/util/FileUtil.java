package com.abc.compare.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtil {

    /**
     * Method to get the no of lines in the given file.
     *
     * @param file
     * @return Integer
     */
    public Integer getNoOfLines(File file) {
        List<String> lines;
        try {
            lines = FileUtils.readLines(file, Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("Invalid file");
            throw new RuntimeException(e);
        }
        return lines.size();
    }

    /**
     * Method to validate the file, this method require file name with extension to validate
     * returns true if file is of type .csv else false
     *
     * @param fullName
     * @return Boolean
     */
    public Boolean isCsvFile(String fullName) {
        Boolean result = Boolean.FALSE;
        if (StringUtils.isEmpty(fullName)) {
            System.out.println("Invalid file type, file name is null or empty");
            return result;
        }

        int dotIndex = fullName.lastIndexOf('.');

        if (dotIndex == -1) {
            System.out.println("Invalid file name, no file extension found");
            return result;
        }
        if (fullName.substring(dotIndex + 1).equals("csv")) {
            result = Boolean.TRUE;
        }
        return result;

    }

    public void copyFile(String sourceFileName, String destinationFileName) throws IOException{
        InputStream is = null;
        OutputStream os = null;
        try {
            File destinationFile = new File(destinationFileName);
            destinationFile.createNewFile();
            is = new FileInputStream(sourceFileName);
            os = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if(is != null) is.close();
            if(os != null) os.close();
        }
    }

    public long filesCompareByLine(Path path1, Path path2) throws IOException {
        try (BufferedReader bf1 = Files.newBufferedReader(path1);
             BufferedReader bf2 = Files.newBufferedReader(path2)) {

            long lineNumber = 1;
            String line1 = "";
            String line2 = "";
            while ((line1 = bf1.readLine()) != null) {
                line2 = bf2.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return lineNumber;
                }
                lineNumber++;
            }
            if (bf2.readLine() == null) {
                return -1;
            }
            else {
                return lineNumber;
            }
        }
    }

    /*public InputStream getFileFromResource(String fileName) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
    public InputStream getFileFromS3(String fileName){return null;}
    public InputStream getFileFromOneDrive(String fileName){return null;}

    public static void main(String[] ar){
        FileUtil fileUtil = new FileUtil();
        try {
            fileUtil.copyFile("C:\\ga\\sample_s.csv", "C:\\ga_copy\\sample_s.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
