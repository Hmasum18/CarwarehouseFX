/*
 * Copyright (c) 2020. Hasan Masum
 * Email : connectwithmasum@gmail.com
 * Github: https://github.com/Hmasum18
 * All rights reserved.
 */

package res.image;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class IMAGE {
    public URL getURLByName(String name){
        return getClass().getResource(name);
    }

    public String getPathByName(String name){
        return getClass().getResource(name).toString();
    }

    public InputStream getInputStreamByName(String name){
        return getClass().getResourceAsStream(name);
    }

    public byte[] getByteArrayByName(String name) throws IOException {
        return getClass().getResourceAsStream(name).readAllBytes();
    }
}
