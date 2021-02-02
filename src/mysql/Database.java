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
                if (getCountryCode(country) == null){
                    System.out.println("\033[31mWrong country!\033[31m");
                    return null;
                }
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
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, country);
                ResultSet rs = ps.executeQuery();
                if (getCountryCode(country) == null) {
                    System.out.println("\033[31mWrong country!\033[0m");
                    return null;
                }
                while (rs.next()) { //search for countries especially languages
                    code3 = rs.getString("Code");
                    capital = rs.getString("city.Name");
                    continent = rs.getString("Continent");
                    area = rs.getInt("SurfaceArea");
                    //add language to list
                    languages.add(rs.getString("Language"));
                }
                countryInfo = new Country(country, code3, capital, area, continent, languages);
                connection.close();
            }
        } catch (Exception e) {e.printStackTrace();}
        return countryInfo;
    }

    public String getCountryCode(String country){
        if (country == null || country.equalsIgnoreCase(""))
            return null;
        String query = "SELECT Code FROM country WHERE Name LIKE ?";
        try {
            Connection connection = getConnection();
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, country);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String code = rs.getString("Code");
                    connection.close();
                    return code;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertCity(City newCity){
        String country = newCity.getCountry();
        String code3 = getCountryCode(country);
        if (code3 == null)
            System.out.println("\033[31mWarning! Country " + country + " does not exist!\033[0m");
        else{
            newCity.setCode3(code3);
            String query = "INSERT INTO city (Name, CountryCode, District, Info) " +
                    "VALUES(?, ?, ?, ?)";
            try {
                Connection connection = getConnection();
                if (connection != null) {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, newCity.getName());
                    ps.setString(2, newCity.getCode3());
                    ps.setString(3, newCity.getDistrict());
                    String json = "{\"Population\": " + newCity.getPopulation() + "}";
                    ps.setString(4, json);
                    ps.executeUpdate();
                    connection.close();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
