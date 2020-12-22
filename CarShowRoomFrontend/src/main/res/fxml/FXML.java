/*
 * Copyright (c) 2020. Hasan Masum
 * Email : connectwithmasum@gmail.com
 * Github: https://github.com/Hmasum18
 * All rights reserved.
 */

package res.fxml;

import java.net.URL;

public class FXML {
    public URL getURLByName(String name){
        return getClass().getResource(name);
    }
}
