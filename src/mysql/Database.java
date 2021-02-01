package mysql;

import mysql.entity.City;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public List<City> getCities(String country){
        //prepare statement
        List<City> cities = new ArrayList<>();
        String query = "SELECT city.Name, JSON_EXTRACT(Info, '$.Population') AS Population " +
                "FROM city " +
                "INNER JOIN country ON country.Code = city.CountryCode " +
                "WHERE country.Name LIKE ? ORDER BY Population DESC";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //connect
            Connection connection = DriverManager.getConnection(DatabaseID.getUrl(), DatabaseID.getUsername(), DatabaseID.getPassword());
            if (connection != null) {
                System.out.println("Success");
                //make statement
                PreparedStatement ps = connection.prepareStatement(query);
                //insert, update, delete executeUpdate
                //store to ResultSet
                ps.setString(1, country);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    String name = rs.getString("Name");
                    int pop = rs.getInt("Population");
                    City newCity = new City(name, pop);
                    cities.add(newCity);
                }
                connection.close();
            }
        }catch (Exception e) { e.printStackTrace(); }
        return cities;
    }
}
