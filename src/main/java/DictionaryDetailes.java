
public class DictionaryDetailes {

    public String Name;
    public int NumOfDocsTermIN;  //df
    public int NumOfTermInCorpus;
    public int Pointer;

    public DictionaryDetailes(String name) {
        Name = name;
        NumOfTermInCorpus = 0;
        NumOfDocsTermIN = 1;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {Name = name;}

    public int getNumOfDocsTermIN() {
        return NumOfDocsTermIN;
    }

    public void setNumOfDocsTermIN(int numOfDocsTermIN) {NumOfDocsTermIN = numOfDocsTermIN; }

    public int getNumOfTermInCorpus() {
        return NumOfTermInCorpus;
    }

    public void setNumOfTermInCorpus(int numOfTermInCorpus) {NumOfTermInCorpus = numOfTermInCorpus; }

    public int getPointer() {
        return Pointer;
    }

    public void setPointer(int pointer) {
        Pointer = pointer;
    }

    public void UpdateNumOfTermInCorpus(int update){this.NumOfTermInCorpus = NumOfTermInCorpus + update;}

    public void UpdateNumOfDocsTermIN(){NumOfDocsTermIN++;}


}
