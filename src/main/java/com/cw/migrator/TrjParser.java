package com.cw.migrator;

import com.cw.database.JDBCConfig;
import com.cw.database.PostGISJDBC;
import com.cw.database.entries.GameEntry;
import com.cw.database.entries.PlayerEntry;
import com.cw.database.entries.PositionEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.postgis.Point;

import java.io.*;
import java.util.*;

/**
 * Created by Christoph on 07.12.2015.
 */
public class TrjParser {

    //CSV file header
    private final String[] FILE_HEADER_MAPPING = {"clientnum", "x", "y", "z", "yaw", "pitch", "roll", "dpos", "dyaw", "dpitch", "droll", "timecounter", "lastupdate", "callingmethod"};
    private final File trjFile;
    private final ArrayList<TrjData> trjDataList;

    public TrjParser(File trjFile) {
        this.trjFile = trjFile;
        this.trjDataList = parse(trjFile);
    }

    private ArrayList<TrjData> parse(File trjFile) {
        FileReader fileReader = null;
        CSVParser csvParser = null;
        try {
            fileReader = new FileReader(trjFile.getAbsolutePath());
            csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING));

            ArrayList<TrjData> trjDataList = new ArrayList<>();
            List<CSVRecord> csvRecords = csvParser.getRecords();
            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);

                TrjData trjData = new TrjData(
                        record.get("clientnum"),
                        record.get("x"),
                        record.get("y"),
                        record.get("z"),
                        record.get("yaw"),
                        record.get("pitch"),
                        record.get("roll"),
                        record.get("dpos"),
                        record.get("dyaw"),
                        record.get("dpitch"),
                        record.get("droll"),
                        record.get("timecounter"),
                        record.get("lastupdate"),
                        record.get("callingmethod"));

                trjDataList.add(trjData);
            }

            return trjDataList;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                csvParser.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void writeTrjToPostGIS(JDBCConfig jdbcConfig, PostGISJDBC postGISJDBC) {
        UUID gameUuid = UUID.randomUUID();

        GameEntry gameEntry = getGameEntry(jdbcConfig.getSchema(), gameUuid);
        postGISJDBC.executePreparedStatement(gameEntry.getPreparedSQL(), gameEntry.getPreparedObjects());

        ArrayList<PlayerEntry> playerEntries = getPlayerEntries(jdbcConfig.getSchema(), gameUuid);
        for (PlayerEntry playerEntry : playerEntries) {
            postGISJDBC.executePreparedStatement(playerEntry.getPreparedSQL(), playerEntry.getPreparedObjects());
        }

        for (PositionEntry positionEntry : getPositionEntries(jdbcConfig.getSchema(), gameUuid, playerEntries)) {
            postGISJDBC.executePreparedStatement(positionEntry.getPreparedSQL(), positionEntry.getPreparedObjects());
        }
    }


    private GameEntry getGameEntry(String schema, UUID gameUuid) {
        String filename = FilenameUtils.getBaseName(trjFile.getName());
        String mapName = "NULL";
        HashSet<Integer> clientNumSet = new HashSet<>();

        for (int i = 0; i < trjDataList.size(); i++) {
            TrjData trjData = trjDataList.get(i);
            String callingMethod = trjData.getCallingMethod();
            if (trjData.getCallingMethod().startsWith("parsemessages_n_mapchange")) {
                mapName = callingMethod.split(":")[1].trim();
            }

            clientNumSet.add(Integer.parseInt(trjData.getClientnum().trim()));
        }

        return new GameEntry(schema, gameUuid, filename, mapName, String.valueOf(clientNumSet.size()));
    }

    private ArrayList<PlayerEntry> getPlayerEntries(String schema, UUID gameUuid) {

        HashMap<String, Integer> playerRuns = new HashMap<>();

        for (int i = 0; i < trjDataList.size(); i++) {
            TrjData trjData = trjDataList.get(i);

            String clientNum = trjData.getClientnum();
            if (!playerRuns.containsKey(clientNum)) {
                playerRuns.put(clientNum, 1);
            }

            if (trjData.getCallingMethod().startsWith("parsemessages_n_died")) {
                playerRuns.put(clientNum, playerRuns.get(clientNum) + 1);
            }
        }

        ArrayList<PlayerEntry> playersEntries = new ArrayList<>();

        for (String key : playerRuns.keySet()) {
            UUID playerUuid = UUID.randomUUID();
            PlayerEntry playerEntry = new PlayerEntry(schema, playerUuid, key, gameUuid, 0, String.valueOf(playerRuns.get(key)));
            playersEntries.add(playerEntry);
        }

        return playersEntries;
    }

    private ArrayList<PositionEntry> getPositionEntries(String schema, UUID gameUuid, ArrayList<PlayerEntry> playerEntries) {
        ArrayList<PositionEntry> positionEntries = new ArrayList<>();
        for (int i = 0; i < trjDataList.size(); i++) {
            UUID posUuid = UUID.randomUUID();
            TrjData trjData = trjDataList.get(i);
            Point pos = null;
            double yaw = -1, pitch = -1, roll = -1, dpos = -1, dyaw = -1, dpitch = -1, droll = -1;
            String message = "NULL";
            try {
                pos = new Point(Double.parseDouble(trjData.getX()), Double.parseDouble(trjData.getY()), Double.parseDouble(trjData.getZ()));
                yaw = Double.parseDouble(trjData.getYaw());
                pitch = Double.parseDouble(trjData.getPitch());
                roll = Double.parseDouble(trjData.getRoll());
                dpos = Double.parseDouble(trjData.getDpos());
                dyaw = Double.parseDouble(trjData.getDyaw());
                dpitch = Double.parseDouble(trjData.getDpitch());
                droll = Double.parseDouble(trjData.getDroll());
                message = trjData.getCallingMethod();
            } catch (NumberFormatException nfe) {
                System.out.println("-- Error while parsing position: " + posUuid + " during game: " + gameUuid + "\nmessage: " + nfe.getMessage());
            }

            UUID playerUUID = null;
            for (int j = 0; j < playerEntries.size(); j++) {
                if (trjData.getClientnum().equals(playerEntries.get(j).getName())) {
                    playerUUID = playerEntries.get(j).getUuid();
                    break;
                }
            }

            PositionEntry positionEntry = new PositionEntry(schema, posUuid, playerUUID, 0, pos, yaw, pitch, roll, dpos, dyaw, dpitch, droll, 0, 0, message, 0, gameUuid);
            positionEntries.add(positionEntry);
        }

        return positionEntries;
    }

}
