package replication;


import Util.Printer;
import com.jcraft.jsch.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SFTP {
    public static final String REMOTE_HOST = "104.198.190.245";
//    private static final String REMOTE_HOST = "35.203.108.224";
    private static final String USERNAME = "damanaulakh1994";
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;


    public void replicate(String localFile, boolean isMetaData, boolean isFileTransger, boolean isMakeDirectory, boolean isRemoveFile, boolean isRemoveDirectory)  {
        Session jschSession = null;

        try {

            JSch jsch = new JSch();
            jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);

//             authenticate using private key
            jsch.addIdentity("/home/damanaulakh1994/gcpkey.ppk");
//            jsch.addIdentity("/home/elizabethj596/private_key_7_open.ppk");

            // authenticate using password
//            jschSession.setPassword(PASSWORD);

            // 10 seconds session timeout
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");

            // 5 seconds timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;
            // transfer file from local to remote server
            if (isFileTransger) {
                transferFile(channelSftp, localFile, isMetaData);

            } else if (isMakeDirectory) {
                transferDirectory(channelSftp, localFile, isMetaData);
            }

            if (isRemoveFile) {
                removeFile(channelSftp, isMetaData, localFile);
            } else if (isRemoveDirectory) {
                removeDirectory(channelSftp, localFile, isMetaData);
            }

            channelSftp.exit();
        } catch (JSchException | SftpException e) {
            Printer.printContent(e.getMessage());
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
        Printer.printContent("Done");
    }



    private void removeDirectory(ChannelSftp channelSftp, String file, boolean isMetaData) throws SftpException {

        // String localFile = "E:\\Winter22\\Data\\project\\distributed_database\\a\\item.txt";
        String remoteFile = "/home/damanaulakh1994/data/database/" + file;

        channelSftp.rm(remoteFile + "/metadata/*");
        channelSftp.rmdir(remoteFile + "/metadata");
        channelSftp.rm(remoteFile + "/*");
        channelSftp.rmdir(remoteFile);
    }

    private void removeFile(ChannelSftp channelSftp, boolean isMetaData, String localFile) throws SftpException {
        String pathArray[] = localFile.split("/");
        String fileName = pathArray[pathArray.length - 1];
        String dbName = "";
        String location = "";
        if (isMetaData) {
            dbName = pathArray[pathArray.length - 3];
            location = dbName + "/metadata/" + fileName;
        } else {
            dbName = pathArray[pathArray.length - 2];
            location = dbName + "/" + fileName;
        }
        // String localFile = "E:\\Winter22\\Data\\project\\distributed_database\\a\\item.txt";
        String remoteFile = "/home/damanaulakh1994/data/database/" + location;

        channelSftp.rm(remoteFile);
    }

    private void transferDirectory(ChannelSftp channelSftp, String localFile, boolean isMetaData) throws SftpException {
        channelSftp.cd("data/database");
        if (!isMetaData) {
            channelSftp.mkdir(localFile);
        }
        if (isMetaData) {
            channelSftp.cd(localFile);
            channelSftp.mkdir("metadata");
        }
    }

    private void transferFile(ChannelSftp channelSftp, String localFile, boolean isMetaData) throws SftpException {
        String pathArray[] = localFile.split("/");
        String fileName = pathArray[pathArray.length - 1];
        String dbName = "";
        String location = "";
        if (isMetaData) {
            dbName = pathArray[pathArray.length - 3];
            location = dbName + "/metadata/" + fileName;
        } else {
            dbName = pathArray[pathArray.length - 2];
            location = dbName + "/" + fileName;
        }
        String remoteFile = "/home/damanaulakh1994/data/database/" + location;

        channelSftp.put(localFile, remoteFile);
    }
}


//reference: https://mkyong.com/java/file-transfer-using-sftp-in-java-jsch/