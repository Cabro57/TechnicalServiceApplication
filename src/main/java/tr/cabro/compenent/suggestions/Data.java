package tr.cabro.compenent.suggestions;

public class Data {
    int ID;
    String text;

    public Data(int ID, String name) {
        this.ID = ID;
        this.text = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
