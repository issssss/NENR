public class Operations {

    public Operations(){}

    public static IFuzzySet unaryOperation(IFuzzySet set1, IUnaryFunction fun){
        MutableFuzzySet noviSet = new MutableFuzzySet(set1.getDomain());
        double vrije1, novaVri;
        for(DomainElement e : set1.getDomain()){
            vrije1 = set1.getValueAt(e);
            novaVri = fun.valueAt(vrije1);
            noviSet.set(e,novaVri);

        }
        return noviSet;

    }

    public static IFuzzySet binaryOperation(IFuzzySet set, IFuzzySet set2, IBinaryFunction bin){
        MutableFuzzySet drugiSet = new MutableFuzzySet(set.getDomain());
        double vri1, vri2;
        for(DomainElement e : set.getDomain()){
            vri1 = set.getValueAt(e);
            vri2 = set2.getValueAt(e);
            drugiSet.set(e, bin.valueAt(vri1,vri2));

        }
        return drugiSet;

    }

    public static IUnaryFunction zadehNot(){
        IUnaryFunction funkcija = new IUnaryFunction() {
            @Override
            public double valueAt(double n) {
                double vrij = 1.0 - n;
                return vrij;
            }
        };
        return funkcija;

    }

    public static IBinaryFunction zadehAnd(){
        IBinaryFunction funkcija = new IBinaryFunction() {
            @Override
            public double valueAt(double n, double m) {
                if(n < m) return n;
                else return m;
            }
        };
        return funkcija;

    }

    public static IBinaryFunction zadehOr(){
        IBinaryFunction funkcija = new IBinaryFunction() {
            @Override
            public double valueAt(double n, double m) {
                if(n > m)
                    return n;
                else
                    return m;
            }
        };
        return funkcija;

    }

    public static IBinaryFunction hamacherTNorm(double p){
        IBinaryFunction funkcija = new IBinaryFunction() {
            @Override
            public double valueAt(double n, double m) {
                return (double) n * m/(p + (1-p)*(n + n*m + m));
            }
        };
        return funkcija;

    }

    public static IBinaryFunction hamacherSNorm(double p){
        IBinaryFunction funkcija = new IBinaryFunction() {
            @Override
            public double valueAt(double n, double m) {
                return (double)(n + m -(2-p)*m*n)/(1-(1-p)*n*m);
            }
        };
        return funkcija;

    }
}
