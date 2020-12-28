/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package res.font;

import javafx.scene.text.Font;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FONT {
    public Font getFontByName(String name, int fontSize){
        String path = getClass().getResource(name).getPath();
        System.out.println(path);
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        return Font.loadFont(path,fontSize);
    }
}
