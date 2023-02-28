package com.abc.compare;

import com.abc.compare.service.CompareService;
import com.abc.compare.service.CompareServiceImpl;

public class Application {

    public static void main(String[] ar) {
        CompareService compareService = new CompareServiceImpl();
        compareService.compareFiles();
    }
}
