package res.database;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class DATABASE {
    public String getDataBase(){
        String path = getClass().getResource("carwarehouse.db").getPath();
        System.out.println(path);
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        System.out.println(path);
        return path;
    }
}
