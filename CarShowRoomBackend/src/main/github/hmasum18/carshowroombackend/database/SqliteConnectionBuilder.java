package github.hmasum18.carshowroombackend.database;

import res.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SqliteConnectionBuilder {
    public static final String TAG = "SqliteConnectionBuilder->";
    public static SqliteConnectionBuilder instance;
    public static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private Connection connection;

    private SqliteConnectionBuilder(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+R.database.getDataBase());
            System.out.println(TAG + "connection established");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("sqlite connection Denied");
        }
    }

    public static SqliteConnectionBuilder getInstance() {
        if(instance == null)
            instance = new SqliteConnectionBuilder();
        return instance;
    }

    public synchronized Connection getConnection()  {
        return connection;
    }
}
