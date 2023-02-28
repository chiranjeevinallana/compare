package com.abc.compare.ops;

import com.abc.compare.util.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public class FileOps {

    private final FileUtil fileUtil;

    public FileOps() {
        fileUtil = new FileUtil();
    }

    public Boolean pullFilesToLocal(String sourceFileLocation,
                                    String destinationFileLocation,
                                    String fileName) {
        if (StringUtils.isEmpty(sourceFileLocation)
                || StringUtils.isEmpty(destinationFileLocation)
                || fileName.isEmpty()
        ) {
            System.out.println("Properties not defined properly.");
            return Boolean.FALSE;
        }
        System.out.println("Coping files to local..");
        try {
            fileUtil.copyFile(sourceFileLocation + fileName,
                    destinationFileLocation + fileName);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Boolean.TRUE;
    }

    public Boolean compareFileLinesCount(String sourceFileLocation,
                                         String destinationFileLocation, String fileName) throws IOException {
        Boolean result = Boolean.FALSE;
        if (StringUtils.isEmpty(sourceFileLocation)
                || StringUtils.isEmpty(destinationFileLocation)
                || StringUtils.isEmpty(fileName)) {
            System.out.println("Properties not defined properly.");
            return Boolean.FALSE;
        }

        final long lineNo = fileUtil.filesCompareByLine(
                Path.of(sourceFileLocation
                        + fileName),
                Path.of(destinationFileLocation
                        + fileName)
        );

        if (lineNo == -1) {
            result = Boolean.TRUE;
        }
        return result;
    }

    public void writeResult(Map<String, Boolean> resultMap, String filePath) {
        File resultFile = new File(filePath+"result_"+System.currentTimeMillis()+".txt");
        try {
            resultFile.createNewFile();
            OutputStream os = new FileOutputStream(resultFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            for(Map.Entry<String,Boolean> entry : resultMap.entrySet()){
                bw.write(entry.getKey()+" : "+entry.getValue());
                bw.newLine();
            }
            bw.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
