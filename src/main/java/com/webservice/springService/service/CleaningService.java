package com.webservice.springService.service;

import com.jcraft.jsch.*;
import com.webservice.springService.config.ServerProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;


@Service
public class CleaningService {

    private final ServerProperties serverProperties;


    public CleaningService( ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }


//    public void scanAndCleanStorage(){
//        JSch jsch = new JSch();
//        Session session = null;
//
//        try{
//
//            session = jsch.getSession(serverProperties.getUsername(), serverProperties.getHostname(), 22);
//            session.setPassword(serverProperties.getPassword());
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking","no");
//            session.setConfig(config);
//            session.connect();
//
//            Channel channel = session.openChannel("sftp");
//            channel.connect();
//            ChannelSftp sftpChannel = (ChannelSftp) channel;
//
//            for (String directory : serverProperties.getDirectory()){
//                Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(directory);
//                long totalSize = 0;
//                for(ChannelSftp.LsEntry entry : list) {
//                    if(!entry.getAttrs().isDir()){
//                        totalSize += entry.getAttrs().getSize();
//                    }
//                }
//                long thresholdSize = (long) (serverProperties.getThreshold() * 0.01 * totalSize);
//                for(ChannelSftp.LsEntry entry : list) {
//                    if(!entry.getAttrs().isDir() && totalSize > thresholdSize) {
//                        sftpChannel.rm(entry.getFilename());
//                        totalSize -= entry.getAttrs().getSize();
//                    }
//                }
//            }
//        }catch (JSchException e) {
//            e.printStackTrace();
//        } catch (SftpException e) {
//            e.printStackTrace();
//        } finally {
//            if(session != null && session.isConnected()){
//                session.disconnect();
//            }
//        }
//    }
    public boolean connect() throws Exception{
        JSch jsch = new JSch();
        Session session = jsch.getSession(serverProperties.getUsername(), serverProperties.getHostname(), 22);
        session.setPassword(serverProperties.getPassword());

        Properties config = new Properties();
        config.put("StrictHostKeyChecking","no");
        session.setConfig(config);
        session.connect();
        return true;
    }

    public boolean scanStorage(String directory) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(serverProperties.getUsername(), serverProperties.getHostname(), 22);
        try{
            session.setPassword(serverProperties.getPassword());

            Properties config = new Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("du -sh " + directory);
            channel.setInputStream(null);
            channel.connect();

            // Read the output from the server
            InputStream in = channel.getInputStream();
            byte[] buffer = new byte[1024];
            while (in.read(buffer) > 0) {
                System.out.println(new String(buffer));
            }

            channel.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            session.disconnect();
        }

        return true;
    }

    public boolean removeFiles(String directory, int threshold) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(serverProperties.getUsername(), serverProperties.getHostname(), 22);
        try{
            session.setPassword(serverProperties.getPassword());

            Properties config = new Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("find " + directory + " -type f -size +" + threshold + "M -delete");
            channel.setInputStream(null);
            channel.connect();

            // Read the output from the server
            InputStream in = channel.getInputStream();
            byte[] buffer = new byte[1024];
            while (in.read(buffer) > 0) {
                System.out.println(new String(buffer));
            }

            channel.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            session.disconnect();
        }

        return true;
    }

    public List<String> findTop10(String directory) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(serverProperties.getUsername(), serverProperties.getHostname(), 22);
        try{
            session.setPassword(serverProperties.getPassword());

            Properties config = new Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("du -h --max-depth=1 " + directory + " | sort -hr | head -n 10");
            channel.setInputStream(null);
            channel.connect();

            // Read the output from the server
            InputStream in = channel.getInputStream();
            byte[] buffer = new byte[1024];
            while (in.read(buffer) > 0) {
                System.out.println(new String(buffer));
            }

            channel.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            session.disconnect();
        }

        return null;
    }
}
