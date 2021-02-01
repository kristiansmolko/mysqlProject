package mysql;

import mysql.entity.City;
import mysql.entity.Country;

import java.sql.*;
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
            Connection connection = getConnection();
            if (connection != null) {
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

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DatabaseID.getUrl(), DatabaseID.getUsername(), DatabaseID.getPassword());
    }

    public Country getCountryInfo(String country){
        String query = "SELECT country.Name, Code, city.Name, " +
                "JSON_EXTRACT(doc, '$.geography.Continent') AS Continent, " +
                "JSON_EXTRACT(doc, '$.geography.SurfaceArea') AS SurfaceArea " +
                "FROM country " +
                "INNER JOIN city ON city.ID = country.Capital " +
                "INNER JOIN countryinfo ON country.Code = countryinfo._id " +
                "WHERE country.Name LIKE ?";
        Country countryInfo = null;
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, country);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                String code3 = rs.getString("Code");
                String capital = rs.getString("city.Name");
                String continent = rs.getString("Continent");
                int area = rs.getInt("SurfaceArea");
                System.out.println(code3 + " " + capital + " " + continent + " " + area);
                countryInfo = new Country(country, code3, capital, area, continent);
            }

        } catch (Exception e) {e.printStackTrace();}
        return countryInfo;
    }
}
