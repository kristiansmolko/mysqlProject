package mysql.entity;

public class City {
    private String name;
    private int population;
    private String district;
    private String code3;
    private String country;

    public City(String name, int population, String district, String country) {
        this.name = name;
        this.population = population;
        this.district = district;
        this.country = country;
    }

    public City(String name, int population) {
        this.name = name;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public String getDistrict() {
        return district;
    }

    public String getCode3() {
        return code3;
    }

    public String getCountry() {
        return country;
    }
}
