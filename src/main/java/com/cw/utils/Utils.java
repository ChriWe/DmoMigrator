package com.cw.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Christoph on 01.12.2015.
 */
public class Utils {

    private static final SbConfig sbConfig;

    static {
        sbConfig = new SbConfig();
    }

    public static ArrayList<String> getDmoFiles() {
        File[] dmoFiles = new File(sbConfig.getDmoDirPath()).listFiles();
        ArrayList<String> dmoList = new ArrayList<>();
        for (File dmo : dmoFiles) {
            if (FilenameUtils.isExtension(dmo.getName(), "dmo")) {
                dmoList.add(FilenameUtils.getBaseName(dmo.getName()));
            }
        }
        return dmoList;
    }

    public static boolean checkRequirements() {
        return trjHomeCleaned();
    }

    private static boolean trjHomeCleaned() {
        File[] trjFiles = new File(sbConfig.getTrjHomePath()).listFiles();
        boolean removed = true;
        for (File trjFile : trjFiles) {
            if (trjFile.getName().equals(sbConfig.getTrjDefaultName())) {
                System.out.println("-- Remove " + sbConfig.getTrjDefaultName());
                removed = trjFile.delete();
            } else if (trjFile.getName().equals(sbConfig.getRecordEndFileName())) {
                System.out.println("-- Remove " + sbConfig.getRecordEndFileName());
                removed = trjFile.delete();
            }
        }
        return removed;
    }

    public static boolean renameTrj(String fileName, String newName) {
        File trjFile = new File(sbConfig.getTrjHomePath() + "/" + fileName);
        File newFile = new File(sbConfig.getTrjHomePath() + "/" + newName);

        if (newFile.exists()) {
            System.out.println("-- Overriding file " + newName);
            newFile.delete();
        }

        return trjFile.renameTo(newFile);
    }

}
