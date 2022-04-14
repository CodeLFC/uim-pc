package gaozhi.online.uim.example.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/4/13 19:21
 */
public class FileUtil {
    public static void write(String path, String content) throws IOException {
        write(path,content,false);
    }
    public static void write(String path, String content,boolean append) throws IOException {
        File file= new File(path);
        if(!file.exists()){
            if(!file.createNewFile()){
                return;
            }
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8));
        bw.write(content);
        bw.close();
    }

    public static String read(String path) throws IOException {
        File file= new File(path);
        if(!file.exists()){
            if(!file.createNewFile()){
                return "";
            }
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        StringBuilder res = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            res.append(line);
        }
        return res.toString();
    }
}
