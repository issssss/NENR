import java.util.ArrayList;
import java.util.List;

public class IFunction {

    public IFunction(){}


    public double kazna(List<Double> vrijednosti, List<Double> ulazi1, List<Double> ulazi2, List<Double[]> izlazi, NeuronskaMreza mreza){
        double sum = 0;
        for(int i = 0; i < ulazi1.size(); i++){
            double[] ulaz = new double[2];
            ulaz[0] = ulazi1.get(i);
            ulaz[1] = ulazi2.get(i);
            List<Double> dobiveni = calcOutPut(ulaz, vrijednosti, mreza);
            double suma = 0;
            for(int j = 0; j<dobiveni.size(); j++){
                suma+=Math.pow((izlazi.get(i)[j]-dobiveni.get(j)),2);
            }
            sum+=suma;
        }

        return sum/ulazi1.size();

    }


    public static List<Double> calcOutPut(double [] ulazi, List<Double> tezine, NeuronskaMreza mreza){
        List<Double> izlaz = new ArrayList<>();
        List<Double> tezineZaPrviSloj;
        List<Double> tezineZaOstSloj;
        tezineZaPrviSloj = List.copyOf(tezine.subList(0, mreza.tezine1));
        tezineZaOstSloj = List.copyOf(tezine.subList(mreza.tezine1, tezine.size()));
        List<Double> tezZaRac;
        List<Double> izlazi = new ArrayList<>();
        for(int i = 0; i<tezineZaPrviSloj.size(); i+=mreza.brUlNeur*2){
            tezZaRac = List.copyOf(tezineZaPrviSloj.subList(i, (i+mreza.brUlNeur*2)));
            double y = funkcijaSlicnosti(tezZaRac, ulazi);
            izlazi.add(y);
        }

        List<Double> tezinePoSloju;
        List<Double> tezineZaRac;
        int j = 0;
        List<Double> noviUlazi = List.copyOf(izlazi);
        List<Double> noviIzlazi = new ArrayList<>();
        for(int i = 0; i<mreza.brSlNeur2.length; i++){
            if(i == 0){
                j += mreza.brSlNeur2[i]*(mreza.brNeur1+1);
                tezinePoSloju = List.copyOf(tezineZaOstSloj.subList(0, j));
                for(int k = 0; k<tezinePoSloju.size(); k+=(mreza.brNeur1+1)) {
                    tezineZaRac = List.copyOf(tezinePoSloju.subList(k, k+(mreza.brNeur1 + 1)));
                    Double y = sigmoida(tezineZaRac, noviUlazi);
                    noviIzlazi.add(y);
                }
            }else{
                int d = j;
                j += mreza.brSlNeur2[i]*(mreza.brSlNeur2[i-1]+1);
                tezinePoSloju = List.copyOf(tezineZaOstSloj.subList(d, j));
                for(int k = 0; k<tezinePoSloju.size(); k+=(mreza.brSlNeur2[i-1]+1)) {
                    tezineZaRac = List.copyOf(tezinePoSloju.subList(k, k+(mreza.brSlNeur2[i-1]+1)));
                    noviIzlazi.add(sigmoida(tezineZaRac, noviUlazi));
                }
            }
            noviUlazi = List.copyOf(noviIzlazi);
            noviIzlazi.clear();

        }
        izlaz = List.copyOf(noviUlazi);

        return izlaz;
    }

    public static double funkcijaSlicnosti(List<Double> tezine, double[] ulazi){
        double izlaz = 0;
        double suma = 0;
        int j = 0;
        for(int i = 0; i<tezine.size(); i+=2){
            suma+= Math.abs(ulazi[j]- tezine.get(i))/Math.abs(tezine.get(i+1));
            j++;
        }
        izlaz = 1/(1+suma);
        return izlaz;
    }

    public static double sigmoida(List<Double> tezine, List<Double> ulazi){
        double x = 0;
        double izlaz;

        for(int i = 0; i< ulazi.size(); i++){
            x+=tezine.get(i)*ulazi.get(i);
        }
        x+=tezine.get(tezine.size()-1);
        izlaz = 1/(1+Math.exp(-x));
        return izlaz;
    }


}
