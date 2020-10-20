package Util;

import Model.City;
import Model.Country;
import Model.User;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;

public class DBConnector {

    private static final String dbName = "U07ezV";
    private static final String dbURL = "jdbc:mysql://52.206.157.109:3306";
    private static final String user = "U07ezV";
    private static final String pass = "53689007988";
    private static final String driver = "com.mysql.jdbc.Driver";


    public static Connection getConnection() throws Exception{
        Connection connection = DriverManager.getConnection("jdbc:mysql://3.227.166.251:3306/U07ezV", "U07ezV", "53689007988");
        return connection;
    }

    public static void populateDBFinal() {
       populateDBUsers();
       populateDBCountries();
       populateDBCities();
    }

    public static void populateDBCountries(){
        try {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO country(countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy)" + "VALUES (?, ?, ?, 'Mike G', ?, 'Watashi Wa Omae Wa')");
            ps.setInt(1, 1);
            ps.setString(2, "USA");
            ps.setString(3, getCurrentDateTime());
            ps.setString(4, getCurrentDateTime());
            ps.executeUpdate();
            ps.setInt(1, 2);
            ps.setString(2, "UK");
            ps.setString(3, getCurrentDateTime());
            ps.setString(4, getCurrentDateTime());
            ps.executeUpdate();
            ps.setInt(1, 3);
            ps.setString(2, "Canada");
            ps.setString(3, getCurrentDateTime());
            ps.setString(4, getCurrentDateTime());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void populateDBCities(){
        try{
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO city(cityId, city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy)" + "VALUES (?, ?, ?, ?, 'Mike G', '2020-11-11 11:11:11', 'Watashi Wa omae wa')");
            ps.setInt(1, 602);
            ps.setString(2, "Phoenix, AZ");
            ps.setInt(3, 1);
            ps.setString(4, getCurrentDateTime());
            ps.executeUpdate();
            ps.setInt(1, 718);
            ps.setString(2, "New York, New York");
            ps.setInt(3, 1);
            ps.setString(4, getCurrentDateTime());
            ps.executeUpdate();
            ps.setInt(1, 200);
            ps.setString(2, "London, England");
            ps.setInt(3, 2);
            ps.setString(4, getCurrentDateTime());
            ps.executeUpdate();
            ps.setInt(1, 514);
            ps.setString(2, "Montreal, Canada");
            ps.setInt(3, 3);
            ps.setString(4, getCurrentDateTime());
            ps.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void populateDBUsers(){
        try{
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO user(userId, userName, password, active, createDate, createdBy, lastUpdate, lastUpdateBy)" + "VALUES(?, ?, ?, 0, ?,'database', ?, 'database')");
            ps.setInt(1, 1111);
            ps.setString(2, "test");
            ps.setString(3, "test");
            ps.setString(4, DBConnector.getCurrentDateTime());
            ps.setString(5, DBConnector.getCurrentDateTime());
            ps.executeUpdate();
            ps.setInt(1, 4182);
            ps.setString(2, "Michael");
            ps.setString(3, "Gass");
            ps.setString(4, DBConnector.getCurrentDateTime());
            ps.setString(5, DBConnector.getCurrentDateTime());
            ps.executeUpdate();ps.setInt(1, 4444);
            ps.setString(2, "TestUser");
            ps.setString(3, "TestPass");
            ps.setString(4, DBConnector.getCurrentDateTime());
            ps.setString(5, DBConnector.getCurrentDateTime());
            ps.executeUpdate();ps.setInt(1, 7777);
            ps.setString(2, "username");
            ps.setString(3, "password");
            ps.setString(4, DBConnector.getCurrentDateTime());
            ps.setString(5, DBConnector.getCurrentDateTime());
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String getCurrentDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String formattedTimestamp = sdf.format(timestamp);
        return formattedTimestamp;
    }

    public static int randomId(){
            Random random = new Random();
            int rand = random.nextInt(9999999 - 1000000) + 1000000;
            return rand;
    }
}
