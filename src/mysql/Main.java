package mysql;

import mysql.entity.City;
import mysql.output.Output;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Database dat = new Database();
        List<City> cities = dat.getCities("Italy");
        Output out = new Output();
        out.printCities(cities);
    }
}
