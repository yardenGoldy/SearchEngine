package Model;

public class TermDetailes {
    public String DocId;
    public int TF;

    public TermDetailes(String docId, int TF) {
        DocId = docId;
        this.TF = TF;
    }

    public String getDocId() {
        return DocId;
    }

    public void setDocId(String docId) {
        DocId = docId;
    }

    public int getTF() {
        return TF;
    }

    public void setTF(int TF) {
        this.TF = TF;
    }
}
