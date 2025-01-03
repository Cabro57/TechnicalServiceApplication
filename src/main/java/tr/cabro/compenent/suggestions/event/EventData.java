package tr.cabro.compenent.suggestions.event;

import tr.cabro.compenent.suggestions.Data;

import java.util.List;


public interface EventData {

    public default List<Data> getData() {
        return null;
    }

    public void itemClick(Data data);
}
