
public class DocDetailes {
    private String DocId;
    private int MaxTermFrequency; //max_tf
    private int NumOfSpecialWords;
    private int DocLength;
    private String DocText;
    private String DocDate;
    private String DocTitle;
    private String DocCity;


    public DocDetailes(String docid,String docText, String docDate, String docTitle, String city) {
        DocId = docid;
        DocText = docText;
        DocDate = docDate;
        DocTitle = docTitle;
        DocCity = city;
        MaxTermFrequency = 0;
    }

    public String getDocID() {return DocId;}

    public void setDocID(String docId) {DocId = docId; }

    public int getMaxTermFrequency() {return MaxTermFrequency;}

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

    public String getDocCity() {return DocCity;}

    public void setDocCity(String docCity) {DocCity = docCity; }
}
