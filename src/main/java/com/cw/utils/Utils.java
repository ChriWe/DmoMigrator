package com.cw.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Christoph on 01.12.2015.
 */
public class Utils {


    public static ArrayList getDmoFiles(String sbPath) {
        String dmoPath = sbPath + "/dmo";
        File dmoDir = new File(dmoPath);
        ArrayList<String> dmoList = new ArrayList<>();
        for (File dmo : dmoDir.listFiles()) {
            if (FilenameUtils.isExtension(dmo.getName(), "dmo")) {
                dmoList.add(FilenameUtils.getBaseName(dmo.getName()));
            }
        }
        return dmoList;
    }

}
