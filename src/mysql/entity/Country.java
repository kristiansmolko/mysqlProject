package mysql.entity;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private String name;
    private String code3;
    private String capitalCity;
    private int area;
    private String continent;
    private List<String> languages;

    public Country(String name, String code3, String capitalCity, int area, String continent, List languages) {
        this.name = name;
        this.code3 = code3;
        this.capitalCity = capitalCity;
        this.area = area;
        this.continent = continent;
        this.languages = languages;
    }

    public String getName() {
        return name;
    }

    public String getCode3() {
        return code3;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public int getArea() {
        return area;
    }

    public String getContinent() {
        return continent;
    }

    public List<String> getLanguages() {
        return languages;
    }
}
