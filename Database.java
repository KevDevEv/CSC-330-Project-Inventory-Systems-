import java.sql.*; // By Waleed Mansour
import java.util.Map;
import java.util.HashMap;

public class Database {
    // A HashMap to store inventory data, mapping item names to their data
    private Map<String, Data> inventory = new HashMap<>();

     //sets the inventory
    public void setInventory(Map<String, Data> inventory) {this.inventory = inventory;}

    // Database connection details
    private final String url = "jdbc:sqlserver://wooboys.tplinkdns.com:1434;database=HRMainData;trustServerCertificate=true;user=BaseLogin;password=D@l34218912";
    private final String user = "BaseLogin";
    private final String password = "D@l34218912";

    /**
     * Constructor of Database class that initializes the database connection by
     * checking whether necessary tables exist or not.
     */
    public Database() {
        initializeDatabase();
    }

    /**
     * Establishes a connection to the database using JDBC URL, user, and password.
     *
     * @return A Connection object representing the connection to the specified
     *         database.
     * @throws SQLException if a database access error occurs or the URL is
     *                      {@code null}
     */
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Tests the database connection by executing a simple SQL query to fetch a
     * constant value.
     *
     * @return true if the connection to the database is successful, false
     *         otherwise.
     */
    public boolean testConnection() {
        try (Connection conn = connect()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeQuery("SELECT 1"); // Simple query to test connection viability
                System.out.println("Database connection test successful.");
                return true;
            } catch (SQLException e) {
                System.out.println("Query execution failed: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Initializes the database schema by creating necessary tables if they do not
     * exist.
     * Specifically, it checks for the 'inventory_items' table and creates it if
     * it's missing.
     */
    private void initializeDatabase() {
        try (Connection conn = connect()) {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            // Check if the "inventory_items" table already exists
            try (ResultSet rs = dbMetaData.getTables(null, null, "inventory_items", null)) {
                if (!rs.next()) {
                    // If the table does not exist, create it using SQL DDL
                    try (Statement stmt = conn.createStatement()) {
                        String sqlCreate = "CREATE TABLE inventory_items (" +
                                "item_name VARCHAR(255) PRIMARY KEY," +
                                "location VARCHAR(255)," +
                                "stock INT," +
                                "in_count INT," +
                                "out_count INT," +
                                "in_rate INT," +
                                "out_rate INT," +
                                "base_line INT," +
                                "lower_bound INT," +
                                "upper_bound INT" +
                                ");";
                        stmt.execute(sqlCreate);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }

    /**
     * Uploads inventory data from a local structure to the database.
     * If an item already exists, its record is updated. Otherwise, a new record is
     * inserted.
     */
    public void uploadData() {
        // SQL query using MERGE statement for SQL Server to handle upsert logic
        String query = "MERGE INTO inventory_items AS TARGET USING (SELECT ? AS item_name, ? AS location, ? AS stock, ? AS in_count, ? AS out_count, ? AS in_rate, ? AS out_rate, ? AS base_line, ? AS lower_bound, ? AS upper_bound) AS SOURCE " +
                "ON TARGET.item_name = SOURCE.item_name " +
                "WHEN MATCHED THEN " +
                "UPDATE SET TARGET.location = SOURCE.location, TARGET.stock = SOURCE.stock, TARGET.in_count = SOURCE.in_count, TARGET.out_count = SOURCE.out_count, TARGET.in_rate = SOURCE.in_rate, TARGET.out_rate = SOURCE.out_rate, TARGET.base_line = SOURCE.base_line, TARGET.lower_bound = SOURCE.lower_bound, TARGET.upper_bound = SOURCE.upper_bound " +
                "WHEN NOT MATCHED BY TARGET THEN " +
                "INSERT (item_name, location, stock, in_count, out_count, in_rate, out_rate, base_line, lower_bound, upper_bound) " +
                "VALUES (SOURCE.item_name, SOURCE.location, SOURCE.stock, SOURCE.in_count, SOURCE.out_count, SOURCE.in_rate, SOURCE.out_rate, SOURCE.base_line, SOURCE.lower_bound, SOURCE.upper_bound);";
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (Map.Entry<String, Data> entry : inventory.entrySet()) {
                Data d = entry.getValue();
                pstmt.setString(1, entry.getKey());
                pstmt.setString(2, d.getLocation());
                pstmt.setInt(3, d.getStock());
                pstmt.setInt(4, d.getIn());
                pstmt.setInt(5, d.getOut());
                pstmt.setInt(6, d.getInRate());
                pstmt.setInt(7, d.getOutRate());
                pstmt.setInt(8, d.getBaseLine());
                pstmt.setInt(9, d.getLowerBound());
                pstmt.setInt(10, d.getUpperBound());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Failed to upload data: " + e.getMessage());
        }
    }

    /**
     * Downloads inventory data from the database and populates the local data
     * structure.
     */
    public void downloadData() {
        String query = "SELECT * FROM inventory_items;";
        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            // Iterates through the result set and populates the inventory map
            while (rs.next()) {
                String itemName = rs.getString("item_name");
                Data data = new Data(
                        rs.getString("location"),
                        rs.getInt("stock"),
                        rs.getInt("in_count"),
                        rs.getInt("out_count"),
                        rs.getInt("in_rate"),
                        rs.getInt("out_rate"),
                        rs.getInt("base_line"),
                        rs.getInt("lower_bound"),
                        rs.getInt("upper_bound"));
                inventory.put(itemName, data);
            }
        } catch (SQLException e) {
            System.out.println("Failed to download data: " + e.getMessage());
        }
    }
}
