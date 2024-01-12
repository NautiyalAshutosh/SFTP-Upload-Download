package com.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Sftp_Remote_to_Host {
    private static final Logger LOGGER = Logger.getLogger(Sftp_Remote_to_Host.class.getName());
    public void pullFromSftpServer(SftpConfig sftpConfig, String username, String host, int port, String password, FileHandler fileHandler) {
        try {
            String outPath = sftpConfig.getRemoteOutPath();
            String inPath = sftpConfig.getLocalInPath();
            String outArchivePath = sftpConfig.getRemoteOutArchivePath();
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(username, host, port);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig("PreferredAuthentications", "password");
                session.connect();
//                System.out.println("Connected to Server Machine");
                ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
                channelSftp.connect();

                //Adding Log to output.log file
                LOGGER.addHandler(fileHandler);
                SimpleFormatter formatter = new SimpleFormatter();
                fileHandler.setFormatter(formatter);

                @SuppressWarnings("unchecked")
                Vector<ChannelSftp.LsEntry> fileList = channelSftp.ls(outPath);
                int folderSize = fileList.size();
                if( folderSize == 2 )  // if nothing is present to download
                {
                    LOGGER.info("Recieved 0 files from CredAble, 'OUT' folder is Empty!! ");
                }
                else {
//                    System.out.println("----Downloading files---- \n");
                    for (ChannelSftp.LsEntry entry : fileList) {
                        String filename = entry.getFilename();
                        if (!entry.getAttrs().isDir()) {
                            //push file from "output" folder to local folder "input"
                            channelSftp.get(outPath + "/" + filename, inPath + "/" + filename);
                            //move file to "output_archive" folder from "output folder"
                            channelSftp.rename(outPath + "/" + filename, outArchivePath + "/" + filename);
                            //Printing logs
//                            System.out.println(filename + " at " + new Timestamp(System.currentTimeMillis()));
                            String message = filename + " recieved from CredAble at " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                            LOGGER.info(message);
                        }
                    }
//                    System.out.println("\n----Files downloaded successfully!----");
//                    System.out.println("Total files downloaded: " + (folderSize -2) );
                }

                //close connections
                channelSftp.disconnect();
                session.disconnect();
            }
            catch (Exception e) {
                System.out.println("Unable to download the files");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Unable to set Session");
            e.printStackTrace();
        }
    }

}
