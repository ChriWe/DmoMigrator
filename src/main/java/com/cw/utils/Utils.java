package com.cw.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Christoph on 01.12.2015.
 */
public class Utils {

    private static OSConfig OSConfig;

    static {
        OSConfig = new OSConfig();
    }

    public static ArrayList getDmoFiles() {
        File[] dmoFiles = OSConfig.getDmoFile().listFiles();
        ArrayList<String> dmoList = new ArrayList<>();
        for (File dmo : dmoFiles) {
            if (FilenameUtils.isExtension(dmo.getName(), "dmo")) {
                dmoList.add(FilenameUtils.getBaseName(dmo.getName()));
            }
        }
        return dmoList;
    }

    public static boolean checkRequirements() {
        return trjCleaned();
    }

    private static boolean trjCleaned() {
        File[] trjFiles = OSConfig.getTrjFile().listFiles();
        for (File trjFile : trjFiles) {
            if (trjFile.getName().equals(OSConfig.getTrjName())) {
                System.out.println("-- Remove " + OSConfig.getTrjName());
                return trjFile.delete();
            }
        }
        return true;
    }

    public static boolean renameTrj(String fileName, String newName) {
        File trjFile = new File(OSConfig.getTrjHome() + "/" + fileName);
        File newFile = new File(OSConfig.getTrjHome() + "/" + newName);

        if (newFile.exists()) {
            System.out.println("-- Overriding file " + newName);
            newFile.delete();
        }

        return trjFile.renameTo(newFile);
    }

}
