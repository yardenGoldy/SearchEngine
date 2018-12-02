
import java.util.ArrayList;
import java.util.HashMap;

public class CityDetailes {
    public String CityName;
    public String Country;
    public String Currency;
    public String Capital;
    public String PopulationSize;
    public HashMap<String,ArrayList<Integer>> CityInDoc;  // <DocID,PositionsInDoc>

    public CityDetailes(){}

    public void InitiateCityDetailes(String cityName, String country,String capital, String currency, String populationSize) {
        CityName = cityName;
        Country = country;
        Capital = capital;
        Currency = currency;
        PopulationSize = populationSize;
        CityInDoc = new HashMap<>();
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCurrency() {return Currency; }

    public void setCurrency(String currency) {Currency = currency; }

    public String getCapital() {return Capital; }

    public void setCapital(String capital) {Capital = capital;}

    public String getPopulationSize() {
        return PopulationSize;
    }

    public void setPopulationSize(String populationSize) {
        PopulationSize = populationSize;
    }

    public void AddNewDoc(String docid,ArrayList<Integer> positionsindoc){
        CityInDoc.put(docid,positionsindoc);
    }
}
