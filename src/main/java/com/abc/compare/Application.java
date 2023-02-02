package com.abc.compare;

import com.abc.compare.ops.FileOps;

import java.io.File;
import java.io.IOException;

public class Application {

    public static void main(String[] ar) {

        Boolean result = Boolean.FALSE;
        try {
            FileOps fileOps = new FileOps();
            if(fileOps.pullFilesToLocal()){
                System.out.println("Compare Application Execution..");
                result = fileOps.compareFileLinesCount();
            }

            System.out.println("File Comparison Completed.");
            System.out.println("===========================");
            System.out.println("Is File Content Same?: "+result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
