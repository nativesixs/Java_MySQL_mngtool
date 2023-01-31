package sql;

import javafx.collections.ObservableList;
import main.Controller;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class Connected {
    //methods with req -> established connection
    private static Connection connection;



    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public static ObservableList<Tables> showtables(Tables tables, ObservableList<Tables> prod, String query, int colnum) throws SQLException {
        //shows all tables ->output to tableview
        Statement statement = connection.createStatement();
        ResultSet rs = null;
        if(query=="showtables") {
            rs = statement.executeQuery("show tables");
        }
        if(query=="showtablecontent") {
            //Controller Controller = new Controller();
            //String selected=Controller.getSelectedElement();
            rs = statement.executeQuery("select * from "+"closet");
        }
        ResultSetMetaData rsm= rs.getMetaData();
        while(rs.next()){
            tables=new Tables(rs.getString(colnum));
            prod.add(tables);
            System.out.println(rs.getString(colnum));
        }
        return prod;
    }

    public static void pickdatabase(String db) throws SQLException {
        //eq to use <databasename>
        Statement statement = connection.createStatement();
        connection.setCatalog(db);
    }

    public static ArrayList getTableColumn(String selected,int colnum) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+selected);
        ResultSetMetaData rsm= rs.getMetaData();
        ArrayList<String> data = new ArrayList<String>();
        while(rs.next()) {
            data.add(rs.getString(colnum));
        }
        return data;
    }




/*
    public static String getColumnType(ResultSetMetaData rsm, int colnum) throws SQLException {
        return rsm.getColumnTypeName(colnum);
    }
    */
    public static String getColumnInfo(String selected, int colnum,String type) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+selected);
        ResultSetMetaData rsm= rs.getMetaData();
        if(type=="type") {
            return rsm.getColumnTypeName(colnum);
        }
        if(type=="count") {
            return String.valueOf(rsm.getColumnCount());
        }
        return null;
    }

    public static String getColumnName(ResultSetMetaData rsm, int colnum) throws SQLException {
        return rsm.getColumnName(colnum);
    }
/*
    public static <coltype> ResultSet getColumnData(ResultSetMetaData rsm, int colnum, ResultSet rs) throws SQLException {
        coltype type= (coltype) getColumnType(rsm,colnum);
        ArrayList<coltype> data = new ArrayList<coltype>();
        while(rs.next()) {
            data.add((coltype) rs.getString(colnum));
        }
        return (ResultSet) data;
    }
*/

}
