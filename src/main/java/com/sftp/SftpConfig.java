package com.sftp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SftpConfig {
    private String host;
    private int port;
    private String username;
    private String password;
    private String remoteOutPath;
    private String remoteOutArchivePath;
    private String remoteInPath;
    private String localOutPath;
    private String localOutArchivePath;
    private String localInPath;

    public String getHost() {
        return this.host;
    }
    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemoteOutPath() {
        return this.remoteOutPath;
    }

    public void setRemoteOutPath(String remoteOutPath) {
        this.remoteOutPath = remoteOutPath;
    }

    public String getRemoteOutArchivePath() {
        return this.remoteOutArchivePath;
    }

    public void setRemoteOutArchivePath(String remoteOutArchivePath) {
        this.remoteOutArchivePath = remoteOutArchivePath;
    }

    public String getRemoteInPath() {
        return this.remoteInPath;
    }

    public void setRemoteInPath(String remoteInPath) {
        this.remoteInPath = remoteInPath;
    }

    public String getLocalOutPath() {
        return this.localOutPath;
    }

    public void setLocalOutPath(String localOutPath) {
        this.localOutPath = localOutPath;
    }

    public String getLocalOutArchivePath() {
        return this.localOutArchivePath;
    }

    public void setLocalOutArchivePath(String localOutArchivePath) {
        this.localOutArchivePath = localOutArchivePath;
    }

    public String getLocalInPath() {
        return this.localInPath;
    }

    public void setLocalInPath(String localInPath) {
        this.localInPath = localInPath;
    }

    SftpConfig readConfigurations()
    {
        SftpConfig sftpConfig = new SftpConfig();
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("sftp_config.properties")) {
            properties.load(input);
            
            // Set properties to the config class
            sftpConfig.setHost(properties.getProperty("host"));
            sftpConfig.setUsername(properties.getProperty("username"));
            sftpConfig.setPort(Integer.parseInt(properties.getProperty("port")));
            sftpConfig.setPassword(properties.getProperty("password"));
            sftpConfig.setLocalInPath(properties.getProperty("localInPath"));
            sftpConfig.setLocalOutPath(properties.getProperty("localOutPath"));
            sftpConfig.setLocalOutArchivePath(properties.getProperty("localOutArchivePath"));
            sftpConfig.setRemoteInPath(properties.getProperty("remoteInPath"));
            sftpConfig.setRemoteOutPath(properties.getProperty("remoteOutPath"));
            sftpConfig.setRemoteOutArchivePath(properties.getProperty("remoteOutArchivePath"));
        }
        catch (IOException e) {
            System.out.println("Unable to read Server details");
            e.printStackTrace();
            return null;
        }

        //----redaing Paths from Path config----
//        try (InputStream input = getClass().getClassLoader().getResourceAsStream(pathConfig)) {
//            properties.load(input);
//            sftpConfig.setLocalInPath(properties.getProperty("localInPath"));
//            sftpConfig.setLocalOutPath(properties.getProperty("localOutPath"));
//            sftpConfig.setLocalOutArchivePath(properties.getProperty("localOutArchivePath"));
//            sftpConfig.setRemoteInPath(properties.getProperty("remoteInPath"));
//            sftpConfig.setRemoteOutPath(properties.getProperty("remoteOutPath"));
//            sftpConfig.setRemoteOutArchivePath(properties.getProperty("remoteOutArchivePath"));
//        }
//        catch (IOException e) {
//            System.out.println("Config file is missing in parameters!");
//            System.out.println("-----Try java -jar jarfile.jar configFilename----");
//            e.printStackTrace();
//            return null;
//        }
        return sftpConfig;
    }
}

