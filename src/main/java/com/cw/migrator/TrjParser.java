package com.cw.migrator;

import com.cw.database.JDBCConfig;
import com.cw.database.PostGISJDBC;
import com.cw.database.entries.GameEntry;
import com.cw.database.entries.PositionEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.postgis.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Christoph on 07.12.2015.
 */
public class TrjParser {

    //CSV file header
    private final String[] FILE_HEADER_MAPPING = {"clientnum", "x", "y", "z", "yaw", "pitch", "roll", "dpos", "dyaw", "dpitch", "droll", "timecounter", "lastupdate", "msg"};
    private final File trjFile;
    private final ArrayList<TrjData> trjDataList;

    public TrjParser(File trjFile) {
        this.trjFile = trjFile;
        this.trjDataList = parse(trjFile);
    }

    private ArrayList<TrjData> parse(File trjFile) {
        FileReader fileReader = null;
        CSVParser csvParser = null;
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);

        try {
            fileReader = new FileReader(trjFile.getAbsolutePath());
            csvParser = new CSVParser(fileReader, csvFormat);

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
                        record.get("msg"));

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

        GameEntry gameEntry = new GameEntry(jdbcConfig.getSchema(), gameUuid, FilenameUtils.getBaseName(trjFile.getName()), "NULL", "0");
        postGISJDBC.executePreparedStatement(gameEntry.getPreparedSQL(), gameEntry.getPreparedObjects());

        for (PositionEntry positionEntry : getPositionEntries(jdbcConfig.getSchema(), gameUuid)) {
            postGISJDBC.executePreparedStatement(positionEntry.getPreparedSQL(), positionEntry.getPreparedObjects());
        }
    }

    private ArrayList<PositionEntry> getPositionEntries(String schema, UUID gameUuid) {
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
                message = trjData.getMsg();
            } catch (NumberFormatException nfe) {
                System.out.println("-- Error happend while parsing position: " + posUuid + " during game: " + gameUuid + "\nmessage: " + nfe.getMessage());
            }
            PositionEntry positionEntry = new PositionEntry(schema, posUuid, 0, 0, pos, yaw, pitch, roll, dpos, dyaw, dpitch, droll, 0, 0, message, 0, gameUuid);
            positionEntries.add(positionEntry);
        }

        return positionEntries;
    }

}
