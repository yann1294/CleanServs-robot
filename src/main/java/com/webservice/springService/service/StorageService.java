package com.webservice.springService.service;

import com.webservice.springService.config.AppProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {

    private final AppProperties appProperties;

    public StorageService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /*
    * iterates over the list of servers from the AppProperties class and for each server,
    * it runs the df command to check the storage usage of the specified directory.
    * The output of the command is parsed to get the storage usage in percentage,
    * which is then compared with the threshold value from the properties.
    * If the usage is greater than the threshold,
    * the find command is executed to remove all files older than 2 days
    */
    public String checkAndCleanStorage() {
        for (AppProperties.Server server : appProperties.getServers()) {
            String[] command = {"bash", "-c", "df -h " + server.getDirectory() + " | awk '{print $5}' | tail -1 | tr -d '%'"};
            Process process;
            try {
                process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output = reader.readLine();
                if (Integer.parseInt(output) > appProperties.getThreshold()) {
                    String[] removeCommand = {"bash", "-c", "find " + server.getDirectory() + " -type f -mtime +2 -exec rm -f {} +"};
                    process = Runtime.getRuntime().exec(removeCommand);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Failure";
            }
        }
        return "Success";
    }
    /*iterates over the list of servers and for each server,
    it runs the du command to get the disk usage of all folders in the specified directory.
    The output of the command is sorted in descending order and the top 10 folders are printed
    */
    public List<String> getTopTenFolders() {
        List<String> topTenFolders = new ArrayList<>();
        for (AppProperties.Server server : appProperties.getServers()) {
            String[] command = {"bash", "-c", "du -a " + server.getDirectory() + " | sort -n -r | head -n " + appProperties.getMaxFolders()};
            Process process;
            try {
                process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output;
                while ((output = reader.readLine()) != null) {
                    String folder = output.split("\\s+")[1];
                    topTenFolders.add(folder);
                }
                for (String folder : topTenFolders) {
                    String[] removeCommand = {"bash", "-c", "find " + folder + " -type f -mtime +2 -exec rm -f {} +"};
                    process = Runtime.getRuntime().exec(removeCommand);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return topTenFolders;
    }
}

