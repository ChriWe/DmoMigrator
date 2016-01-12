package com.cw.utils;

import com.cw.migrator.MigratorConfig;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Christoph on 01.12.2015.
 */
public class Utils {

    private static final MigratorConfig MIGRATOR_CONFIG;

    static {
        MIGRATOR_CONFIG = new MigratorConfig();
    }

    public static ArrayList<String> getDmoFiles() {
        File[] dmoFiles = new File(MIGRATOR_CONFIG.getDmoDirPath()).listFiles();
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
        File[] trjFiles = new File(MIGRATOR_CONFIG.getTrjHomePath()).listFiles();
        boolean removed = true;
        for (File trjFile : trjFiles) {
            if (trjFile.getName().equals(MIGRATOR_CONFIG.getTrjDefaultName())) {
                System.out.println("-- Remove " + MIGRATOR_CONFIG.getTrjDefaultName());
                removed = trjFile.delete();
            } else if (trjFile.getName().equals(MIGRATOR_CONFIG.getRecordEndFileName())) {
                System.out.println("-- Remove " + MIGRATOR_CONFIG.getRecordEndFileName());
                removed = trjFile.delete();
            }
        }
        return removed;
    }

    public static boolean renameTrj(String fileName, String newName) {
        File trjFile = new File(MIGRATOR_CONFIG.getTrjHomePath() + "/" + fileName);
        File newFile = new File(MIGRATOR_CONFIG.getTrjHomePath() + "/" + newName);

        if (newFile.exists()) {
            System.out.println("-- Overriding file " + newName);
            newFile.delete();
        }

        return trjFile.renameTo(newFile);
    }

}
