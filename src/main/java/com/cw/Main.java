package com.cw;

import org.apache.commons.io.FilenameUtils;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws AWTException, IOException {
        final String sbPath = System.getenv("SB_HOME");
        final String dmoPath = sbPath;

        Process sb = Main.runSauerbraten(sbPath, dmoPath);

        DmoRobot dmoRobot = new DmoRobot();
        dmoRobot.executeRobot(Main.getDmoFiles(sbPath));
    }

    private static ArrayList getDmoFiles(String dmoPath) {
        File dmoDir = new File(dmoPath);
        ArrayList<String> dmoList = new ArrayList<>();
        for (File dmo : dmoDir.listFiles()) {
            if (FilenameUtils.isExtension(dmo.getName(), "dmo")) {
                dmoList.add(FilenameUtils.getBaseName(dmo.getName()));
            }
        }
        return dmoList;
    }

    private static Process runSauerbraten(String sbPath, String dmoPath) {
        File sbDir = new File(sbPath);
        String[] exec = {"cmd", "/c", "sauerbraten.bat"};
        java.util.List<String> cmdAndArgs = Arrays.asList(exec);

        ProcessBuilder pb = new ProcessBuilder(cmdAndArgs);
        pb.directory(sbDir);
        pb.redirectErrorStream(true); // merge stdout, stderr of process

        Process p = null;
        try {
            System.out.println("starte sauerbraten");
            p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p;
    }
}
