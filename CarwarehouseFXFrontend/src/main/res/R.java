/*
 * Copyright (c) 2020. Hasan Masum
 * Email : connectwithmasum@gmail.com
 * Github: https://github.com/Hmasum18
 * All rights reserved.
 */

package res;

import javafx.scene.paint.Color;
import res.css.CSS;
import res.font.FONT;
import res.fxml.FXML;
import res.image.IMAGE;

public class R {
    public static final FXML fxml = new FXML();
    public static final IMAGE image = new IMAGE();
    //public static final FONT font = new FONT();
    public static final CSS css = new CSS();
    public enum color {
        PRIMARY_COLOR("#ef6c00"),
        SECONDARY_COLOR("#fbc02d");

        private final Color color;
        color(String color) {
            this.color = Color.valueOf(color);
        }
        public Color getColor() {
            return color;
        }
    }


}
