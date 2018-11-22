package Model;

import java.util.HashMap;

public class PostingDetailes {

    public int NumOfTermInDoc; //tf
    public StringBuilder LocationInDoc; // location of the terms in a doc

    public StringBuilder getLocationInDoc() {return LocationInDoc; }

    public void setLocationInDoc(StringBuilder locationInDoc) {LocationInDoc = locationInDoc;}

    public int getNumOfTermInDoc() {
        return NumOfTermInDoc;
    }

    public void setNumOfTermInDoc(int numOfTermInDoc) {
        NumOfTermInDoc = numOfTermInDoc;
    }


}
