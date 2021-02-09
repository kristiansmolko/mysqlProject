package mysql;

import mysql.entity.CapitalCity;
import mysql.entity.City;
import mysql.entity.Country;
import mysql.entity.Monument;
import mysql.helper.Help;
import mysql.json.Server;
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

        /*List<CapitalCity> capitalCityList = dat.getCapitalCities("Asia");
        out.printCapitalCities(capitalCityList);*/

        //dat.insertNewMonument("gbr", "London", "London Eye");

        /*List<Monument> monuments = Help.getMonuments();
        out.printMonuments(monuments);*/

        Server server = new Server();
        String json = "{\"country\":\"Russian Federation\",\"city\":\"Moscow\",\"monument\":\"Kremlin\"}";
        System.out.println(server.insertNewMonument(json));
    }
}
