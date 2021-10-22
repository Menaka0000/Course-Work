package dao;

import db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    private static PreparedStatement getPreparedStatement (String sql ,Object... args) throws SQLException, ClassNotFoundException {
        Connection con= DbConnection.getInstance().getConnection();
        PreparedStatement pst = con.prepareStatement(sql);
        for(int i=0; i<args.length; i++){
            pst.setObject(i+1,args[i]);
        }
        return pst;
    }
    public static boolean executeUpdate(String sql ,Object... args) throws SQLException, ClassNotFoundException {
        PreparedStatement pst = getPreparedStatement(sql, args);
        return pst.executeUpdate()>0;
    }
    public static ResultSet executeQuery(String sql ,Object... args) throws SQLException, ClassNotFoundException {
        PreparedStatement pst = getPreparedStatement(sql, args);
        return pst.executeQuery();
    }
}
