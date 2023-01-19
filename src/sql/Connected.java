package sql;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connected {
    //methods with req -> established connection
    private static Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public static ObservableList<Tables> showtables(Tables tables, ObservableList<Tables> prod) throws SQLException {
        //shows all tables ->output to tableview
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("show tables");
        while(rs.next()){
            tables=new Tables(rs.getString(1));
            prod.add(tables);
        }
        return prod;
    }

    public static void pickdatabase(String db) throws SQLException {
        //eq to use <databasename>
        Statement statement = connection.createStatement();
        connection.setCatalog(db);
    }

    public static void getSelTable(String selected) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+selected);

    }
}
