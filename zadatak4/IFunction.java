public class IFunction {

    Double[] x, y;
    Double[] rj;
    public  IFunction(int vel){
        this.x = new Double[vel];
        this.y = new Double[vel];
        this.rj = new Double[vel];

    }
    public IFunction(Double[] x, Double[] y, Double[] rje){
        this.x = x;
        this.y = y;
        this.rj = rje;
    }

    public double kazna(Double[] vrijednosti){
        double sum = 0;
        for(int i = 0; i < x.length; i++){
            double sin = Math.sin(vrijednosti[0] + vrijednosti[1]*x[i]);
            double cos = vrijednosti[2]*Math.cos(x[i]*(vrijednosti[3]+y[i]));
            double exp = (1+Math.exp(Math.pow((x[i]-vrijednosti[4]),2)));
            double ny =  sin + cos*1/exp;
            sum += Math.pow((rj[i]-ny),2);
        }
        return sum/250;
    }

    public double dobrota(RjesenjeOblik[] pop, RjesenjeOblik tren){

        double maxKazna = pop[0].kazna;
        Double dob;
        for(int i = 1; i<pop.length; i++){
            if(maxKazna < pop[i].kazna) {
                maxKazna = pop[i].kazna;
            }
        }
        dob = maxKazna - tren.kazna;
        return dob;
    }

}
