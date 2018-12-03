

public class TermDetailes {
    public String DocId;
    public int TF;
    public boolean isInTitle;
    //public ArrayList<Integer> Positions;


    public TermDetailes(String docId) {
        this.TF = 0;
        this.isInTitle = false;
    }

    public String getDocId() {
        return DocId;
    }

    public void SetDocId(String docID){
        this.DocId = docID;
    }

    public int getTF() {
        return TF;
    }

    public void UpdateTF() {
        this.TF = TF + 1;
    }
}
