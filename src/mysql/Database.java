package mysql;

import mysql.entity.CapitalCity;
import mysql.entity.City;
import mysql.entity.Country;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static mysql.helper.Help.*;

public class Database {
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

    public void updatePopulation(String country, String city, int pop){
        String query = "UPDATE city " +
                "SET Info = ? " +
                "WHERE Name like ?";
        try {
            Connection connection = getConnection();
            if (connection != null) {
                if (!isCityInCountry(city, country)) {
                    System.out.println("\033[31mWrong country or city name!\033[0m");
                    return;
                }
                if (pop <= 0){
                    System.out.println("\033[31mBad number!\033[0m");
                    return;
                }
                int previousPop = getPreviousPop(country, city);
                String json = "{\"Population\": " + pop + "}";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, json);
                ps.setString(2, city);
                ps.executeUpdate();
                System.out.println("\033[32mSuccessfully updated population of " + city + " from " + previousPop + " to " + pop + ".\033[0m");
                connection.close();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<String> getCountryInContinent(String continent){
        List<String> countries = new ArrayList<>();
        if (continent == null || continent.equalsIgnoreCase("")){
            System.out.println("Wrong continent");
            return null;
        }
        String query = "SELECT JSON_EXTRACT(doc, '$._id') AS Code, JSON_EXTRACT(doc, '$.geography.Continent') AS Con " +
                "FROM countryinfo " +
                "WHERE JSON_EXTRACT(doc, '$.geography.Continent') = ?";
        try {
            Connection connection = getConnection();
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1 , continent);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    countries.add(rs.getString("Code"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return countries;
    }

    public CapitalCity getCapital(String countryCode){
        if (countryCode == null || countryCode.equalsIgnoreCase(""))
            return null;
        String query = "SELECT Capital, country.Name, city.Name, JSON_EXTRACT(Info, '$.Population') AS Population " +
                "FROM country " +
                "INNER JOIN city ON country.Capital = city.ID " +
                "WHERE country.Code LIKE ?";
        try {
            Connection connection = getConnection();
            if (connection != null){
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, countryCode);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String name = rs.getString("city.Name");
                    String country = rs.getString("country.Name");
                    int pop = rs.getInt("Population");
                    return new CapitalCity(name, country, pop);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<CapitalCity> getCapitalCities(String continent){
        List<CapitalCity> capitals = new ArrayList<>();
        for (String country : getCountryInContinent(continent)){
            capitals.add(getCapital(country.replace("\"", "")));
        }
        return capitals;
    }

    public boolean insertNewMonument(String code3, String city, String name){
        String query = "INSERT INTO monument(name, city) " +
                "VALUES(?, ?)";
        if (city == null || code3 == null || code3.equalsIgnoreCase("") || city.equals("")){
            System.out.println("\033[31mIncorrect city or country!\033[0m");
            return false;
        }
        if (name == null || name.equals("")){
            System.out.println("\033[31mIncorrect name!\033[0m");
            return false;
        }
        if (!isCityInCountryCode(city, code3)){
            System.out.println("\033[31mWrong country or city!\033[0m");
            return false;
        }
        try {
            Connection connection = getConnection();
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, name);
                ps.setInt(2, getCityId(city));
                ps.executeUpdate();
                System.out.println("Added " + name + " to " + city + " with ID: " + getCityId(city));
            }
        } catch (Exception e) { e.printStackTrace(); }

        return true;
    }
}
