package com.sftp;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

public class Sftp_Host_to_Remote {
    private static final Logger LOGGER = Logger.getLogger(Sftp_Host_to_Remote.class.getName());
    public void pushToSftpServer(SftpConfig sftpConfig, String username, String host, int port, String password, FileHandler fileHandler) {
        try {
            String remoteInPath = sftpConfig.getRemoteInPath();
            final String localOutpath = sftpConfig.getLocalOutPath();
            final String localOutArchivePath = sftpConfig.getLocalOutArchivePath();

            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(username, host, port);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig("PreferredAuthentications", "password");
                session.connect();
//                System.out.println("Connected to Server");
                ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.connect();

                //Adding Log to output.log file
                LOGGER.addHandler(fileHandler);
                SimpleFormatter formatter = new SimpleFormatter();
                fileHandler.setFormatter(formatter);

                //upload all files from 'output' folder to remote machine's 'input' folder
                File[] _files = new File(localOutpath).listFiles();
                if( _files.length == 0)
                {
                    LOGGER.info("Uploaded 0 files to CredAble, 'OUT' folder is Empty!! ");
                }
                else {
//                    System.out.println("----Uploading files----");
                    for (File file : _files) {
                        if (!file.isDirectory()) {
                            channelSftp.put(file.getAbsolutePath(), remoteInPath + "/" + file.getName());
                            String message = file.getName() + " pushed to CredAble at " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                            LOGGER.info(message);
                        }
                    }
//                    System.out.println("All File Uploaded successfully!");
//                    System.out.println("Total files uploaded: " + _files.length);
//                    System.out.println();
                }
                // Move all files from "output" to "output_archive" directory
                try {
                    Path sourcePath = Paths.get(localOutpath);
                    Path destinationPath = Paths.get(localOutArchivePath);
                    try (Stream<Path> files = Files.list(sourcePath)) {
                        files.forEach(file -> {
                            try {
                                Path destFile = destinationPath.resolve(file.getFileName());
                                Files.move(file, destFile, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
                catch (IOException e) {
                    LOGGER.info("Unable to move files to 'output_archive'");
                    e.printStackTrace();
                }

                //close connections
//                fileHandler.close();
                channelSftp.disconnect();
                session.disconnect();
            }
            catch (Exception e) {
                LOGGER.info("Could not able to Upload the file");
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOGGER.info("Unable to set config details");
            e.printStackTrace();
        }
    }
}

