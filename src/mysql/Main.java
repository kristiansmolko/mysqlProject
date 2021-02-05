package mysql;

import mysql.entity.CapitalCity;
import mysql.entity.City;
import mysql.entity.Country;
import mysql.output.Output;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Database dat = new Database();
        Output out = new Output();
        String countryName = "Slovakia";
        /*Country country = dat.getCountryInfo(countryName);
        out.printCountryInfo(country);
        List<City> cities = dat.getCities(countryName);
        out.printCities(cities);*/
        /*City newCity = new City("Ploske", 68, "Revuca Okres", "Slovakia");
        dat.insertCity(newCity);*/
        //dat.updatePopulation("Slovakia", "Ploske", 68);
        List<CapitalCity> capitalCityList = dat.getCapitalCities("Europe");
        dat.printCapitalCities(capitalCityList);
    }
}
