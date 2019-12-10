<h2>Copy Content from all Nodes to Local Nodes</h2>

`sudo chmod +x target/scptransport-1.0.jar`

`sudo java -jar scptransport-1.0.jar [Path_local_directory_save] [path_host_file] [userName] [port] [Path_host_list_ip] [Path_rsa_file]`

Example:

`sudo java -jar scptransport-1.0.jar /app/CountLogProject/log/ /log/apache-tomcat/access_log.log.1 app 22 /app/CountLogProject/host_ip.txt /app/CountLogProject/app_id_rsa`
