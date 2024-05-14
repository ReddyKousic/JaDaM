package db;

import java.sql.*;

public class DBCM {
    static String url = "jdbc:mysql://localhost:3307/JavaL";
    static String username = "root";
    static String password = "kousic";

    public boolean createTable(String sql) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement stmt = con.createStatement();
            int rowsAffected = stmt.executeUpdate(sql); // Use executeUpdate instead of execute

            stmt.close();
            con.close();

            // Check if the table was created or not (0 indicates success)
            return rowsAffected == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean executeSQL(String sql) {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement stmt = con.createStatement();
            int affectedRows = stmt.executeUpdate(sql); // Use executeUpdate for INSERT, UPDATE, DELETE operations

            stmt.close();
            con.close();
            return affectedRows > 0; // Return true if there are affected rows (i.e., if the statement executed
                                     // successfully)
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getTableSchema(String tableName) {
        StringBuilder schema = new StringBuilder();

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, null);

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");

                schema.append(columnName).append(" ").append(dataType);
                if (columnSize > 0) {
                    schema.append("(").append(columnSize).append(")");
                }
                schema.append(", ");
            }

            // Remove the trailing comma and space
            if (schema.length() > 0) {
                schema.setLength(schema.length() - 2);
            }

            columns.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schema.toString();
    }
}