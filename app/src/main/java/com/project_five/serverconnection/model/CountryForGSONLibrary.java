package com.project_five.serverconnection.model;

import java.util.List;

public class CountryForGSONLibrary {
    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public CountryForGSONLibrary(List<Country> countries){
        this.countries = countries;
    }
    /*
    also we can use array ...
    private Country[] countries;

    public Country[] getCountries() {
        return countries;
    }

    public void setCountries(Country[] countries) {
        this.countries = countries;
    }

    public CountryForGSONLibrary(Country[] countries){
        this.countries = countries;
    }
     */

}
