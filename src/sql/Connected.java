package sql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class Connected {
    //methods with req -> established connection
    private static Connection connection;


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public static ResultSet grabdata(String selected) throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+selected);
        return rs;
    }

    public static ResultSet showTables() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("show tables");
        return rs;
    }

    public static ResultSet searchWhere(String selected,String command,String id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+selected+" where "+id +"= "+ '\u0022'+ command+'\u0022');
        return rs;
    }

    public static ResultSet queryExecute(String selected, String id,String command,int choice) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = null;
        switch(choice) {
            case 1:
                rs = statement.executeQuery("select * from "+selected);
                break;
            case 2:
                rs = statement.executeQuery("show tables");
                break;
            case 3:
                rs = statement.executeQuery("select * from " + selected + " where " + id + "= " + '\u0022' + command + '\u0022');
                break;
            case 4:
                rs = statement.executeQuery(command);
                break;
            case 5:
                rs = statement.executeQuery("show databases");
                break;
            case 6:
                rs = statement.executeQuery("drop table "+selected);
                break;
            case 7:
                //same as case 4 -> for statements that dont produce result set
                statement.executeUpdate(command);
                break;
        }
        return rs;
    }

/*
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
    */

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





    public static String getColumnType(ResultSetMetaData rsm, int colnum) throws SQLException {
        return rsm.getColumnTypeName(colnum);
    }

    public static String getColumnType(String selected, ObservableList colnames) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+selected);
        ResultSetMetaData rsm= rs.getMetaData();
        ObservableList fullColnames=getColumnNames(selected);
        ObservableList indexes = FXCollections.observableArrayList();



        return null;
    }

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
        if(type=="fillBox"){

        }
        return null;
    }
    public static ObservableList getColumnNames(String selected) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+selected);
        ResultSetMetaData rsm= rs.getMetaData();
        ObservableList lst = FXCollections.observableArrayList();
        for(int i=1;i<=getColumnCount(rsm);i++){
            lst.add(getColumnName(rsm,i));
        }
        return lst;
    }
    public static String getColumnName(ResultSetMetaData rsm, int colnum) throws SQLException {
        return rsm.getColumnName(colnum);
    }
    public static int getColumnCount(ResultSetMetaData rsm) throws SQLException {
        return rsm.getColumnCount();
    }

    public static ObservableList getColumnType(String selected) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from "+selected);
        ResultSetMetaData rsm= rs.getMetaData();
        ObservableList lst = FXCollections.observableArrayList();
        for(int i=1;i<=getColumnCount(rsm);i++){
            lst.add(rsm.getColumnClassName(i));
        }
        return lst;
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

    public static String getCurrentDb() throws SQLException {
        Statement statement = connection.createStatement();
        String db = connection.getCatalog();
        return db;
    }

}
