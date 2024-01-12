package com.sftp;

import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.File;
public class SftpFileDownload {
    private static final Logger LOGGER = Logger.getLogger(SftpFileDownload.class.getName());
    public static void main(String[] args) {
//        String filename = "";
//        if (args.length > 1)
//        {
//            filename = args[0];
//        }

        try {
            String logFile = "Output_" + new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date())+".log";
            // Path for the Log files
            String folderPath = "./logs/";
            File logFolder = new File(folderPath);
            // Create the directory if it doesn't exist
            if (!logFolder.exists()) {
                logFolder.mkdirs();
            }
            FileHandler fileHandler = new FileHandler(folderPath + logFile, true);
            LOGGER.addHandler(fileHandler);

            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            SftpConfig sftpConfig = new SftpConfig().readConfigurations();
            String host = sftpConfig.getHost();
            int port = sftpConfig.getPort();
            String username = sftpConfig.getUsername();
            String password = sftpConfig.getPassword();

            Sftp_Remote_to_Host remoteToHost = new Sftp_Remote_to_Host();
            Sftp_Host_to_Remote hostToRemote = new Sftp_Host_to_Remote();

            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            LOGGER.info("Initiating upload process at " + timeStamp + "...");
            hostToRemote.pushToSftpServer(sftpConfig, username, host, port, password, fileHandler);
            LOGGER.info("Upload process completed...\n");

            timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            LOGGER.info("Initiating download process at " + timeStamp + "...");
            remoteToHost.pullFromSftpServer(sftpConfig, username, host, port, password , fileHandler);
            LOGGER.info("Download process completed...\n\n");

        }
        catch (Exception e) {
            System.out.println("Unable to fetch Config details");
            e.printStackTrace();
        }
    }

}
