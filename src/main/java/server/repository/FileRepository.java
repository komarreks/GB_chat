package server.repository;

import java.io.*;

public class FileRepository implements RepositoryInt{
    private final String LOG_FILE_NAME = "chat_log.txt";

    @Override
    public String uploadLog() throws IOException {
        File file = new File(LOG_FILE_NAME);

        if (!file.exists()){
            file.createNewFile();
        }

        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder sb = new StringBuilder();

        String line;
        while (true){
            line = br.readLine();

            if (line != null){
                sb.append(line + System.lineSeparator());
            }else {
                return sb.toString();
            }
        }
    }

    @Override
    public void safeLog(String log){
        try {
            File file = new File(LOG_FILE_NAME);

            if (!file.exists()){
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);

            fw.write(log);

            fw.flush();
        }catch (Exception e){

        }
    }
}
