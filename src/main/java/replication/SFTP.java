package replication;


import com.jcraft.jsch.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SFTP {
    //    private static final String REMOTE_HOST = "34.152.57.185";
//    private static final String REMOTE_HOST = "34.152.36.90";
    private static final String HOST_1 = "10.162.0.5";
    private static final String HOST_2 = "10.162.0.4";
    private  String  currentRemoteHost = "";
    private static final String USERNAME = "rsa-key-20220407";
    private static final String PASSWORD = "";
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;



    public void replicate(String localFile, boolean isMetaData, boolean isFileTransger, boolean isMakeDirectory, boolean isRemoveFile, boolean isRemoveDirectory) {
        Session jschSession = null;

        try {
            currentRemoteHost = getRemoteHost();


            JSch jsch = new JSch();
          jschSession = jsch.getSession(USERNAME, currentRemoteHost, REMOTE_PORT);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);

//             authenticate using private key
//            jsch.addIdentity("keys\\privatedemo_open.ppk");
            jsch.addIdentity("/home/elizabethj596/keys/privatedemo_open.ppk");

//            jsch.addIdentity(getClass().getResourceAsStream("privatedemo_open.ppk"));

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

            }
            else if (isMakeDirectory){
                transferDirectory(channelSftp,localFile,isMetaData);
            }

            if(isRemoveFile){
                removeFile(channelSftp, isMetaData,localFile);
            }

            else if (isRemoveDirectory){
                removeDirectory(channelSftp, localFile, isMetaData);
            }
            // download file from remote server to local
            // channelSftp.get(remoteFile, localFile);
            channelSftp.exit();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }
        System.out.println("Done");
    }

    private String getRemoteHost() {
        InetAddress ip;
        String currentRemoteHost = null;
        try {
            ip = InetAddress.getLocalHost();
            System.out.println(ip);
            String localHost = ip.toString().split("/")[1];

            if (localHost.equals(HOST_1)){
                currentRemoteHost = HOST_2;
            }
            else {
                currentRemoteHost = HOST_1;
            }

        } catch (UnknownHostException e) {

            e.printStackTrace();
        }
        return currentRemoteHost;
    }

    private void removeDirectory(ChannelSftp channelSftp, String file, boolean isMetaData) throws SftpException {

        // String localFile = "E:\\Winter22\\Data\\project\\distributed_database\\a\\item.txt";
        String remoteFile = "/home/rsa-key-20220407/" + file;

        channelSftp.rm(remoteFile + "/metadata/*");
        channelSftp.rmdir(remoteFile + "/metadata");
        channelSftp.rm(remoteFile + "/*");
        channelSftp.rmdir(remoteFile);
    }

    private void removeFile(ChannelSftp channelSftp, boolean isMetaData, String localFile) throws SftpException {
        String pathArray[] = localFile.split("/");
        String fileName =  pathArray[pathArray.length -1];
        String dbName = "";
        String location  = "";
        if (isMetaData){
            dbName = pathArray[pathArray.length -3];
            location = dbName + "/metadata/" +fileName;
        }
        else {
            dbName = pathArray[pathArray.length -2];
            location =  dbName + "/" + fileName;
        }
        // String localFile = "E:\\Winter22\\Data\\project\\distributed_database\\a\\item.txt";
        String remoteFile = "/home/rsa-key-20220407/" + location;

        channelSftp.rm(remoteFile);
    }

    private void transferDirectory(ChannelSftp channelSftp, String localFile, boolean isMetaData) throws SftpException {
        if (!isMetaData) {
            channelSftp.mkdir(localFile);
        }
        if (isMetaData){
            channelSftp.cd(localFile);
            channelSftp.mkdir("metadata");
        }
    }

    private void transferFile(ChannelSftp channelSftp, String localFile, boolean isMetaData) throws SftpException {
        String pathArray[] = localFile.split("/");
        String fileName =  pathArray[pathArray.length -1];
        String dbName = "";
        String location  = "";
        if (isMetaData){
            dbName = pathArray[pathArray.length -3];
            location = dbName + "/metadata/" +fileName;
        }
        else {
            dbName = pathArray[pathArray.length -2];
            location =  dbName + "/" + fileName;
        }
        // String localFile = "E:\\Winter22\\Data\\project\\distributed_database\\a\\item.txt";
        String remoteFile = "/home/rsa-key-20220407/" + location;

        channelSftp.put(localFile, remoteFile);
    }
}


//reference ; https://mkyong.com/java/file-transfer-using-sftp-in-java-jsch/