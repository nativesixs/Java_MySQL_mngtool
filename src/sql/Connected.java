package sql;

import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

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

    public static ResultSet getSelTable(String selected) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+selected);
        ResultSetMetaData rsm= rs.getMetaData();
        getColumnData(rsm,5,rs);
        return rs;
    }





    public static String getColumnType(ResultSetMetaData rsm, int colnum) throws SQLException {
        return rsm.getColumnTypeName(colnum);
    }

    public static String getColumnName(ResultSetMetaData rsm, int colnum) throws SQLException {
        return rsm.getColumnName(colnum);
    }

    public static <coltype> ArrayList getColumnData(ResultSetMetaData rsm, int colnum,ResultSet rs) throws SQLException {
        String coltype=getColumnType(rsm,colnum);
        ArrayList<coltype> data = new ArrayList<coltype>();
        while(rs.next()) {
            data.add((coltype) rs.getString(colnum));
        }
        return data;
    }


}
