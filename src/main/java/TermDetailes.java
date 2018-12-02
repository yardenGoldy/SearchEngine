import java.util.ArrayList;

public class TermDetailes {
    public String DocId;
    public int TF; //tf
    //public ArrayList<Integer> Positions;


    public TermDetailes(String docId) {
        //DocId = docId;
        // Positions = new ArrayList<>();
        TF = 1;
        boolean isInTitle;

    }

    public String getDocId() {return DocId; }

    public int getTF() {return TF;}

    public void UpdateTF(){this.TF = TF+1;}

    //public void AddPosition(int p){Positions.add(p);}

    //public ArrayList<Integer> getPositions() {return Positions;}
}
