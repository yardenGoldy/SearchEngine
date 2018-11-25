import java.util.ArrayList;
import java.util.HashMap;

public class CityDetailes {

    public String CityName;
    public String Country;
    public String Crrency;
    public String PopulationSize;
    public HashMap<String,ArrayList<Integer>> CityInDoc;  // <DocID,PositionsInDoc>

    public void InitiateCityDetailes(String cityName, String country, String crrency, String populationSize) {
        CityName = cityName;
        Country = country;
        Crrency = crrency;
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

    public String getCrrency() {
        return Crrency;
    }

    public void setCrrency(String crrency) {
        Crrency = crrency;
    }

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
