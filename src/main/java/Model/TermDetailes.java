package Model;

import java.util.ArrayList;

public class TermDetailes {
    public String DocId;
    public int TF;
    public ArrayList<Integer> Positions;


    public TermDetailes(String docId) {
        DocId = docId;
        Positions = new ArrayList<>();
        TF = 0;
    }

    public String getDocId() {
        return DocId;
    }

    public int getTF() {return TF;}

    public void UpdateTF(){this.TF = TF+1;}
}
