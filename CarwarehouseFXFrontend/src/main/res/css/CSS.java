/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package res.css;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class CSS {

    public static final String TAG = "CSS->";

    public String getPathByName(String name){
        String path = getClass().getResource(name).getPath();
        System.out.println(path);
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        System.out.println(TAG +path);
        return path;
    }

    public URL getByName(String name){
        return getClass().getResource(name);
    }
}
