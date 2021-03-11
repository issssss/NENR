import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MyFuzzyControlSystem {

    //sve domene
    SimpleDomain oriDom = (SimpleDomain) Domain.intRange(0,2);
    SimpleDomain udaljDom = (SimpleDomain) Domain.intRange(0,130); //1300/10
    SimpleDomain brizDom = (SimpleDomain) Domain.intRange(0,130);
    SimpleDomain kutDom = (SimpleDomain) Domain.intRange(-91,91);

    /* moguca pravila:
    stavim sve u neke granice; brzina ne smije biti < 90 niti > 80
    preblizu je ako se priblizim granici do 11m
    krivi smjer - okreni se za 75 stupnjeva
     */

    //set premisa
    CalculatedFuzzySet kriticno_blizu_lijevo= new CalculatedFuzzySet(udaljDom, StandardFuzzySets.gammaFuntion(100, 110));
    CalculatedFuzzySet kriticno_blizu_desno= new CalculatedFuzzySet(udaljDom, StandardFuzzySets.gammaFuntion(100, 110));
    CalculatedFuzzySet krivi_smjer = new CalculatedFuzzySet(oriDom, StandardFuzzySets.gammaFuntion(0,1));
    CalculatedFuzzySet prevelika_brzina = new CalculatedFuzzySet(brizDom, StandardFuzzySets.gammaFuntion(90,130));
    CalculatedFuzzySet premala_brzina = new CalculatedFuzzySet(brizDom, StandardFuzzySets.IFunction(0, 50));

    //zakljucci
    CalculatedFuzzySet skreni_ostro_lijevo = new CalculatedFuzzySet(kutDom, StandardFuzzySets.gammaFuntion(75, 90));
    CalculatedFuzzySet skreni_ostro_desno = new CalculatedFuzzySet(kutDom, StandardFuzzySets.gammaFuntion(75,90));
    CalculatedFuzzySet ubrzaj = new CalculatedFuzzySet(brizDom, StandardFuzzySets.IFunction(20, 70));
    CalculatedFuzzySet uspori = new CalculatedFuzzySet(brizDom, StandardFuzzySets.gammaFuntion(-20, -60));
    CalculatedFuzzySet promijeni_smjer = new CalculatedFuzzySet(oriDom, StandardFuzzySets.gammaFuntion(179, 180));

    public static void main(String[] args) throws IOException {

        String lnIn, lnOut;
        //int L, D, LK, DK, S, V;
        Scanner scanner = new Scanner(System.in);
        Defuzzifier def = new COAdefuzzifier();
        AkceleracijaBaza pravilaBaze = new AkceleracijaBaza(def);
        KormiloBaza pravilaKormila = new KormiloBaza(def);
        /*while(true){
            int K = 12;
            int A = 55;
            String[] komponente;
            lnIn = scanner.nextLine();
            //System.out.println(lnIn);
            if(lnIn.equals("KRAJ")) break;
            komponente = lnIn.split(" ");
            L = Integer.parseInt(komponente[0]);
            D = Integer.parseInt(komponente[1]);
            LK = Integer.parseInt(komponente[2]);
            DK = Integer.parseInt(komponente[3]);
            V = Integer.parseInt(komponente[4]);
            S = Integer.parseInt(komponente[5]);
            K = MyFuzzyControlSystem.sprovediZakljucivanjeKormilo(L,D,LK,DK,S,pravilaKormila);
            A = MyFuzzyControlSystem.sprovediZakljucivanjeAkc(V,pravilaBaze);
            lnOut = String.valueOf(K) +" "+ String.valueOf(A);
            //A = 15;
            //K = 25;
            System.out.println(A + " " + K);
            System.out.flush();
        }

         */
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        int L=0,D=0,LK=0,DK=0,V=0,S=0,akcel,kormilo;
        String line = null;
        while(true){
            if((line = input.readLine())!=null){
                if(line.charAt(0)=='K') break;
                Scanner s = new Scanner(line);
                L = s.nextInt();
                D = s.nextInt();
                LK = s.nextInt();
                DK = s.nextInt();
                V = s.nextInt();
                S = s.nextInt();
            }

            // fuzzy magic ...

            akcel = 10; kormilo = 5;
            kormilo = MyFuzzyControlSystem.sprovediZakljucivanjeKormilo(L,D,LK,DK,S,pravilaKormila);
            akcel = MyFuzzyControlSystem.sprovediZakljucivanjeAkc(V,pravilaBaze);
            System.out.println(akcel + " " + kormilo);
            System.out.flush();
        }
    }

    public static int sprovediZakljucivanjeKormilo(int L, int D, int LK, int DK, int S, KormiloBaza pravilaKormila){

        Pravilo[] svaPravila = {pravilaKormila.skreniDesno, pravilaKormila.skreniLijevo, pravilaKormila.promijeniSmjer};
        int[] brojevi = {LK, DK, S};
        MutableFuzzySet zaklj = svaPravila[0].zakljucivanje(1, brojevi[0]);
        MutableFuzzySet prav2;
        //MutableFuzzySet rj = new MutableFuzzySet();
        IBinaryFunction zadOr = Operations.zadehOr();
        for(int i=1; i < svaPravila.length; i++){
            prav2 = svaPravila[i].zakljucivanje(1, brojevi[i]);
            for(int j = 0; j< zaklj.getDomain().getCardinality(); j++){
                zaklj.set(zaklj.getDomain().elementForIndex(j), zadOr.valueAt(zaklj.getValueAt(zaklj.getDomain().elementForIndex(j)), prav2.getValueAt(prav2.getDomain().elementForIndex(j))));
            }

    }
        int rj = pravilaKormila.def.defuzzy(zaklj);
        return rj;
    }

    public static int sprovediZakljucivanjeAkc(int V, AkceleracijaBaza pravilaAkc){

        Pravilo[] svaPravila = {pravilaAkc.umanjiBrzinu, pravilaAkc.povecajBrzinu};
        MutableFuzzySet zaklj = svaPravila[0].zakljucivanje(1, V);
        MutableFuzzySet prav2;
        IBinaryFunction zadOr = Operations.zadehOr();
        if(svaPravila.length > 1)
            for(int i=1; i < svaPravila.length; i++){
                prav2 = svaPravila[i].zakljucivanje(1, V);
                for(int j = 0; j< zaklj.getDomain().getCardinality(); j++){
                    //System.out.println(zaklj.getValueAt(zaklj.getDomain().elementForIndex(j)));
                    //System.out.println(prav2.getValueAt(prav2.getDomain().elementForIndex(j)));
                    //System.out.println("Zadeh "+zadOr.valueAt(zaklj.getValueAt(zaklj.getDomain().elementForIndex(j)), prav2.getValueAt(prav2.getDomain().elementForIndex(j))));
                    zaklj.set(prav2.getDomain().elementForIndex(j), zadOr.valueAt(zaklj.getValueAt(zaklj.getDomain().elementForIndex(j)), prav2.getValueAt(prav2.getDomain().elementForIndex(j))));
                }

            }
        int rj = pravilaAkc.def.defuzzy(zaklj);
        //S ystem.out.println(rj);
        return rj;
    }


}
