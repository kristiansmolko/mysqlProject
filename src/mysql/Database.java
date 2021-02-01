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
                "Language, " +
                "JSON_UNQUOTE(JSON_EXTRACT(doc, '$.geography.Continent')) AS Continent, " +
                "JSON_EXTRACT(doc, '$.geography.SurfaceArea') AS SurfaceArea " +
                "FROM country " +
                "INNER JOIN city ON city.ID = country.Capital " +
                "INNER JOIN countryinfo ON country.Code = countryinfo._id " +
                "INNER JOIN countrylanguage ON country.Code = countrylanguage.CountryCode " +
                "WHERE country.Name LIKE ? AND IsOfficial = 'T'";
        Country countryInfo = null;
        String code3 = null, capital = null, continent = null;
        int area = 0;
        List<String> languages = new ArrayList<>(); //create list
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, country);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { //search for countries especially languages
                code3 = rs.getString("Code");
                capital = rs.getString("city.Name");
                continent = rs.getString("Continent");
                area = rs.getInt("SurfaceArea");
                //add language to list
                languages.add(rs.getString("Language"));
            }
            if (code3 == null && capital == null && continent == null) {
                //if there is no code, capital and continent
                System.out.println("This country does not exist!");
                connection.close();
                //return null (can be changed later)
                return null;
            } else {
                //otherwise return countryInfo like it should look like
                countryInfo = new Country(country, code3, capital, area, continent, languages);
            }
            connection.close();
        } catch (Exception e) {e.printStackTrace();}
        return countryInfo;
    }
}
