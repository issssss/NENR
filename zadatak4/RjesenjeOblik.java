public class RjesenjeOblik {

    Double[] kromosomi = new Double[5];
    double kazna;
    double dobrota;

    public RjesenjeOblik(Double[] jed){
        this.kromosomi = jed;
    }
    public RjesenjeOblik(int brJed){
        Double[] kromosomi = new Double[brJed];
    }

    public int brjed(){
        return kromosomi.length;
    }

}
