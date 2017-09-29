package sg.edu.nus.cs3205.subsystem3.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DBQueryParser {

    public static ResultSet query(String table, String[] columns, String[] conditions, Object[] variables)
            throws SQLException, Exception {
        return query(table, columns, conditions, variables, null);
    }

    public static ResultSet query(String table, String[] columns, String[] conditions, Object[] variables,
            String[] orderby) throws SQLException, Exception {

        String query = "SELECT ";
        // columns to retrieve
        if (columns != null && columns.length > 0) {
            for (String column : columns) {
                query +=  column + ", " ;
            }
            query += "''";
        } else {
            query += "* ";
        }
        query += "FROM cs3205." + table + " ";
        // conditions to apply
        if (conditions != null && conditions.length > 0 && variables != null && variables.length > 0
                && variables.length == conditions.length) {
            query += "WHERE 1=1 ";
            for (String condition : conditions) {
              //condition = (something = ?)
                query += "AND " + condition;
            }
        }
        // order by to apply
        if (orderby != null && orderby.length > 0) {
            query += "ORDER BY '' ";
            for (String order : orderby) {
                query += ", " + order;
            }
        }
        query += ";";
        PreparedStatement ps = DB.getConnection().prepareStatement(query);
        if(conditions != null && conditions.length > 0 && variables != null && variables.length > 0
                && variables.length == conditions.length){
          int i = 0;
          for (String condition : conditions) {
              ps = updateVariables(ps, variables[i], i);
              i++;
          }
        }
        ResultSet rs = ps.executeQuery();
        return rs;
    }


    /**
     * Insert a user record into server 3
     *
     * @param Object[] values, all the values for the user
     *
     * @return int value indicating how many rows are affected, 0 if insert fail.
     */
    public static int insertUser(Object[] values){
      String sql = "INSERT INTO cs3205.USER VALUES (?, ?, ?);";
      int result = 0;
      try{
        PreparedStatement ps = DB.getConnection().prepareStatement(sql);
        int i = 0;
        for (Object value : values) {
            ps = updateVariables(ps, values[i], i);
            i++;
        }
        result = ps.executeUpdate();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return result;
    }


    private static PreparedStatement updateVariables(PreparedStatement ps, Object argObj, int pt)
            throws SQLException, Exception {
        if (argObj == null) {
            ps.setNull(pt + 1, 0);
        } else if (String.class.isInstance(argObj)) {
            ps.setString(pt + 1, (String) argObj);
        } else if (Integer.class.isInstance(argObj)) {
            ps.setInt(pt + 1, (Integer) argObj);
        } else if (Long.class.isInstance(argObj)) {
            ps.setLong(pt + 1, (Long) argObj);
        } else if (Double.class.isInstance(argObj)) {
            ps.setDouble(pt + 1, (Double) argObj);
        } else if (Float.class.isInstance(argObj)) {
            ps.setFloat(pt + 1, (Float) argObj);
        } else if (Date.class.isInstance(argObj)) {
            java.sql.Date sqlDate = new java.sql.Date(((Date) argObj).getTime());
            ps.setDate(pt + 1, sqlDate);
        } else if (argObj instanceof byte[]) {
            ps.setBytes(pt + 1, (byte[]) argObj);
        } else if (java.sql.Blob.class.isInstance(argObj)) {
            ps.setBlob(pt + 1, (java.sql.Blob) argObj);
        } else {
            String argClassName = argObj.getClass().getName();
            throw new Exception("Unknown argument type (" + pt + ") : " + (argClassName));
        }
        return ps;
    }

}
