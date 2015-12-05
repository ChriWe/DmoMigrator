package com.cw.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

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

    public static boolean checkRequirements(Config config) {
        File trjDir = new File(config.getSbHome() + "/trajectories");

        return trjCleaned(trjDir);
    }

    private static boolean trjCleaned(File trjDir) {
        File[] trjFiles = trjDir.listFiles();
        for (File trjFile : trjFiles) {
            if (trjFile.getName().equals("trajectorie.csv")) {
                System.out.println("-- Remove trajectorie.csv");
                return trjFile.delete();
            }
        }
        return true;
    }

}
