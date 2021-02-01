package mysql.output;

import mysql.entity.City;

import java.util.List;

public class Output {
    public void printCities(List<City> cities){
        for (City city : cities)
            System.out.println(city.getName() + " ( " + city.getPopulation() + " )");

    }
}
