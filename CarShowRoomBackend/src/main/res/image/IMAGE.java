/*
 * Copyright (c) 2020. Hasan Masum
 * Email : connectwithmasum@gmail.com
 * Github: https://github.com/Hmasum18
 * All rights reserved.
 */

package res.image;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class IMAGE {

    public static final String TAG = "IMAGE->";

    public URL getURLByName(String name){
        return getClass().getResource(name);
    }

    public String getPathByName(String name){
        return getClass().getResource(name).toString();
    }

    public InputStream getInputStreamByName(String name){
        try {
            return getClass().getResourceAsStream(name);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getByteArrayByName(String name) throws IOException {
        System.out.println(TAG+" loading="+name);
        return getClass().getResourceAsStream(name).readAllBytes();
    }

    public boolean storeImage(String name, byte[] bytes){
        String path = getClass().getResource("icon.png").getPath();
        System.out.println(path);
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        int idx = path.lastIndexOf("/");
        path = path.substring(0,idx+1);

        File file = new File(path+name);
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bufferedOutputStream.write(bytes);
            System.out.println(TAG+ name+" image stored successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(TAG+ name+" image was not stored successfully");
            return false;
        }
        return true;
    }
}
