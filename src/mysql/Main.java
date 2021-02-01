package mysql;

import mysql.entity.City;
import mysql.entity.Country;
import mysql.output.Output;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Database dat = new Database();
        Output out = new Output();
        Country country = dat.getCountryInfo("Italy");
        out.printCountryInfo(country);
        List<City> cities = dat.getCities("Italy");
        //out.printCities(cities);
    }
}
