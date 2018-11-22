package Model;

public class DocDetailes {
    public int MaxTermFrequency;
    public int NumOfSpecialWords;
    public int DocLength;
    public String DocText;
    public String DocDate;
    public String DocTitle;
    public String City;


    public DocDetailes(String docText, String docDate, String docTitle, String city ,int docLength) {
        DocText = docText;
        DocDate = docDate;
        DocTitle = docTitle;
        City = city;
        DocLength = docLength;
    }

    public int getMaxTermFrequency() {
        return MaxTermFrequency;
    }

    public void setMaxTermFrequency(int maxTermFrequency) {
        MaxTermFrequency = maxTermFrequency;
    }

    public int getNumOfSpecialWords() {
        return NumOfSpecialWords;
    }

    public void setNumOfSpecialWords(int numOfSpecialWords) {
        NumOfSpecialWords = numOfSpecialWords;
    }

    public int getDocLength() {
        return DocLength;
    }

    public void setDocLength(int docLength) {
        DocLength = docLength;
    }

    public String getDocText() {
        return DocText;
    }

    public void setDocText(String docText) {
        DocText = docText;
    }

    public String getDocDate() {
        return DocDate;
    }

    public void setDocDate(String docDate) {
        DocDate = docDate;
    }

    public String getDocTitle() {
        return DocTitle;
    }

    public void setDocTitle(String docTitle) {
        DocTitle = docTitle;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
