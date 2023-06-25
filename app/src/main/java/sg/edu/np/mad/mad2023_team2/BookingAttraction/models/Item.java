package sg.edu.np.mad.mad2023_team2.BookingAttraction.models;


//////////////////////////////////////////////////////////
//        ITEMS CLASS stores (Trip)Object and Type      //
//////////////////////////////////////////////////////////

public class Item {

    private int type;
    private Object object;

    public Item(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
