package mysql.output;

import mysql.entity.City;
import mysql.entity.Country;

import java.util.List;

public class Output {
    public void printCities(List<City> cities){
        if (cities == null || cities.size() == 0)
            return;
        System.out.println("\033[33m------------------------------------\033[0m");
        System.out.println("List of cities: ");
        for (City city : cities)
            System.out.println("        - " + city.getName() + " ( " + city.getPopulation() + " )");
        System.out.println("\033[33m--------------------------------------\033[0m");
    }

    public void printCountryInfo(Country country){
        if (country == null)
            return;
        System.out.println("\033[35m~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\033[0m");
        System.out.println("Country: " + country.getName());
        System.out.println("Capital: " + country.getCapitalCity());
        System.out.println("Code: " + country.getCode3());
        System.out.println("Continent: " + country.getContinent());
        System.out.println("Surface area: " + country.getArea());
        System.out.println("Official languages: ");
        for (int i = 0; i < country.getLanguages().size(); i++){
            if (i == country.getLanguages().size()-1)
                System.out.print(country.getLanguages().get(i));
            else
                System.out.print(country.getLanguages().get(i) + ", ");
        }
        System.out.println();
    }
}
