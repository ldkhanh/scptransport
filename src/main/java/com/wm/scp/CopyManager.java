package com.wm.scp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CopyManager {
    public static void main(String[] arg) {
        // Command java CopyManager [local_directory] [remote_directory_file] [userName] [port] [path_host_file] [path_rsa_file]
        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            //arg = new String[]{"/Users/d0l0278/tmp_log/", "/log/apache-tomcat/testCopy.txt", "app", "22", "host_ip.txt", "~/.ssh/app_id_rsa"};
            if (arg.length < 6) {
                System.err.println("Wrong argument");
                return;
            }
            String local = (arg[0].isEmpty())? "/Users/d0l0278/tmp_log/" : arg[0];
            String remote = (arg[1].isEmpty())? "/log/apache-tomcat/testCopy.txt" : arg[1];
            local += createDir(local) + "/";
            String userName = (arg[2].isEmpty())? "app" : arg[2];
            int port = Integer.parseInt(((arg[3].isEmpty())? "22" : arg[3]));
            String pathIPHosts = arg[4];
            List<String> hostIPs = extractHost(pathIPHosts);

            String keyFilePath = (arg[5].isEmpty())? "src/main/resources/app_id_rsa" : arg[5];
            String keyPassword = null;

            for (String host : hostIPs) {
                executor.execute(new CopyTask(userName,host, port, keyFilePath, keyPassword, remote, local));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static String createDir(String pathDir) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.now();
        String newDirDate = localDate.format(dtf);
        Path path = Paths.get(pathDir+"/"+newDirDate);
        //if directory exists?
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
        }
        return newDirDate;
    }
    private static List<String> extractHost(String hostFile) {
        List<String> result = new ArrayList<>();
        try{
            FileInputStream fstream = new FileInputStream(hostFile);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                String[] tokens = strLine.split(" ");
                for (String tk : tokens) {
                    if (!tk.isEmpty())
                        result.add(tk);
                }
            }
            in.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
        return result;
    }
}
