public class Pravilo {


    CalculatedFuzzySet [] premisa;
    CalculatedFuzzySet zakljucak;
    String vez = "";
    private int[] ulazi;

    public Pravilo(String veznik, CalculatedFuzzySet zakljucak, CalculatedFuzzySet... premise){
        this.premisa = premise;
        this.vez = veznik;

        this.zakljucak = zakljucak;
    }
    private double minStroj(CalculatedFuzzySet ... premise){
        double memb = premise[0].getValueAt(premise[0].getDomain().elementForIndex(0));
        for(int i = 0; i<premise.length; i++){
            //for(int j = 0; j < premise[i].getDomain().getCardinality(); j++){
            if(memb > premise[i].getValueAt(premise[i].getDomain().elementForIndex(ulazi[i]))){
                memb = premise[i].getValueAt(premise[i].getDomain().elementForIndex(ulazi[i]));
            }
        }
        return memb;
    }

    private double produktStroj(CalculatedFuzzySet... premise){
        double memb = 1;
        for(int i = 0; i<premise.length; i++){
            //for(int j = 0; j < premise[i].getDomain().getCardinality(); j++){
            int br = this.ulazi[i];
            //System.out.println("Trenutni broj "+ br);
            //System.out.println( premise[i].getValueAt(premise[i].getDomain().elementForIndex(this.ulazi[i])));
            memb *= premise[i].getValueAt(premise[i].getDomain().elementForIndex(this.ulazi[i]));
        }
        return memb;
    }
    public MutableFuzzySet zakljucivanje(int pilim,  int... ulazi){
        this.ulazi = ulazi;
        MutableFuzzySet noviZaklj = new MutableFuzzySet(zakljucak.domena);
        double memb = 1; // saznati kada pozvati koju funkciju
        if(pilim == 0)
            memb = minStroj(premisa);
        else if (pilim == 1)
            memb = produktStroj(premisa);
        for(int i = 0; i < zakljucak.getDomain().getCardinality(); i++){
            //System.out.println(memb + " "+ zakljucak.getDomain().elementForIndex(i).values[0]+ " Rez zaklj " + zakljucak.getValueAt(zakljucak.getDomain().elementForIndex(i)));
            noviZaklj.set(zakljucak.getDomain().elementForIndex(i), memb* zakljucak.getValueAt(zakljucak.getDomain().elementForIndex(i))); //
        }

        return noviZaklj;

    }
}
