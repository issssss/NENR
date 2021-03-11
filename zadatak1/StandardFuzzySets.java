public class StandardFuzzySets {

    public StandardFuzzySets(){}

    public static IIntUnaryFunction IFunction(int i, int j){
        IIntUnaryFunction lFunkcija = new IIntUnaryFunction() {
            @Override
            public double valueAt(int ind) {
                if(ind < i){
                    return 1;
                }
                if(ind > j){
                    return 0;
                }
                else{
                    return (double)(j-ind)/(double)(j-i);
                }
            }
        };
        return lFunkcija;
    }

    public static IIntUnaryFunction gammaFuntion(int i, int j){
        IIntUnaryFunction gFunk = new IIntUnaryFunction() {
            @Override
            public double valueAt(int ind) {
                if(ind < i)
                    return 0;
                if(ind > j)
                    return 1;
                return (double)(ind - i)/(double)(j-i);
            }
        };
        return gFunk;

    }

    public static IIntUnaryFunction lambdaFunction(int i, int j, int k){
        IIntUnaryFunction lamFunc = new IIntUnaryFunction() {
            @Override
            public double valueAt(int ind) {
                double vrij = 0;
                if(ind < i || ind > k){
                    return 0;
                }
                if(ind>= i && ind < j){
                    vrij = ((double)ind - i)/(j-i);
                    return vrij;
                }
                else{
                    vrij = ((double)k - ind)/(k-j);
                    return vrij;
                }
            }
        };
        return lamFunc;

    }


}

