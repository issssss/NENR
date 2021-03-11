import java.util.ArrayList;
import java.util.List;

public class RjesenjeOblik {

    List<Double> kromosomi;
    double kazna;
    double dobrota;
    int size;

    public RjesenjeOblik(int brKrom){
        this.size = brKrom;
        this.kromosomi = new ArrayList<>(brKrom);

    }

    public int brjed(){
        return this.size;
    }

}
