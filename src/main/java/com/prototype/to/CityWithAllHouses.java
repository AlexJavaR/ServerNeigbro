package com.prototype.to;

import java.util.ArrayList;
import java.util.List;

public class CityWithAllHouses {
    private Integer total;
    private List<HouseLocation> houses;

    public CityWithAllHouses() {
        this.total = 0;
        this.houses = new ArrayList<>();
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<HouseLocation> getHouses() {
        return houses;
    }

    public void setHouses(List<HouseLocation> houses) {
        this.houses = houses;
    }
}
