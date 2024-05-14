import java.util.Scanner;
import db.DBCM;

public class Main {
    public static void main(String[] args) {
        DBCM dbcm = new DBCM();
        Scanner scanner = new Scanner(System.in);

        // Get the table name from the user
        System.out.print("Enter the table name: ");
        String tableName = scanner.nextLine();

        // Retrieve the schema of the table
        String tableSchema = dbcm.getTableSchema(tableName);
        System.out.println("Schema of table " + tableName + ":");
        System.out.println(tableSchema);

        // Generate input prompts for each column of the table
        String[] columns = tableSchema.split(", ");
        String[] values = new String[columns.length];
        String columnNames = "";
        String columnValues = "";

        for (int i = 0; i < columns.length; i++) {
            String columnName = columns[i].split(" ")[0];
            columnNames += columnName + ", ";
            System.out.print("Enter value for " + columnName + ": ");
            values[i] = scanner.nextLine();

            String columnType = columns[i].split(" ")[1];
            // Convert value to the appropriate data type
            if (columnType.startsWith("INT") || columnType.startsWith("TINYINT") ||
                    columnType.startsWith("SMALLINT") || columnType.startsWith("MEDIUMINT") ||
                    columnType.startsWith("BIGINT")) {
                columnValues += Integer.parseInt(values[i]) + ", ";
            } else if (columnType.startsWith("VARCHAR") || columnType.startsWith("CHAR") ||
                    columnType.startsWith("TEXT") || columnType.startsWith("ENUM") ||
                    columnType.startsWith("SET")) {
                columnValues += "'" + values[i] + "', ";
            } else if (columnType.startsWith("FLOAT") || columnType.startsWith("DOUBLE") ||
                    columnType.startsWith("DECIMAL")) {
                columnValues += Double.parseDouble(values[i]) + ", ";
            } else if (columnType.startsWith("DATE") || columnType.startsWith("DATETIME") ||
                    columnType.startsWith("TIMESTAMP")) {
                columnValues += "'" + values[i] + "', ";
            } else if (columnType.startsWith("BOOLEAN")) {
                // Convert "true" or "false" strings to 1 or 0
                columnValues += (values[i].equalsIgnoreCase("true") ? 1 : 0) + ", ";
            } else {
                // Handle other data types if necessary
            }
        }

        // Remove the trailing comma and space
        columnNames = columnNames.substring(0, columnNames.length() - 2);
        columnValues = columnValues.substring(0, columnValues.length() - 2);

        // Construct the SQL INSERT statement
        String sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + columnValues + ")";
        System.out.println("SQL query: " + sql);

        boolean success = dbcm.executeSQL(sql);
        if (success) {
            System.out.println("Data inserted successfully!");
        } else {
            System.out.println("Failed to insert data.");
        }
    }
}
