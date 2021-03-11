import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class ElimGenAlgoritam {

    static int indeksNajboljeg;
    public ElimGenAlgoritam(){}

    public static RjesenjeOblik[] elLoop(NeuronskaMreza mreza, Dataset podaci, int velPop, Random rand, IFunction fun, int maxIter, double p1, double p2, double[] t){
        RjesenjeOblik[] populacija = initPopul(mreza.brTez, velPop, rand);
        Double[] poljeGresaka = new Double[velPop];

        for(int i = 0; i<velPop; i++){
            poljeGresaka[i] = fun.kazna(populacija[i].kromosomi, podaci.ulazi1, podaci.ulazi2,podaci.izlazi, mreza);
        }

        for(int i = 0; i < maxIter; i++){
            int ind1 = rand.nextInt(velPop);
            int ind2 = rand.nextInt(velPop);
            int ind3 = rand.nextInt(velPop);
            int indNajLos;
            int[] rodInd = new int[2];
            if(poljeGresaka[ind1]>poljeGresaka[ind2]){
                if(poljeGresaka[ind1] > poljeGresaka[ind3]) {
                    indNajLos = ind1;
                    rodInd[0] = ind2;
                    rodInd[1] = ind3;
                }
                else {
                    indNajLos = ind3;
                    rodInd[0] = ind2;
                    rodInd[1] = ind1;
                }
            }
            else if(poljeGresaka[ind2]>poljeGresaka[ind3]) {
                indNajLos = ind2;
                rodInd[0] = ind1;
                rodInd[1] = ind3;
            }
            else {
                indNajLos = ind3;
                rodInd[0] = ind2;
                rodInd[1] = ind1;
            }
            RjesenjeOblik dijete = krizanje(mreza.brTez, populacija[rodInd[0]], populacija[rodInd[1]],rand, poljeGresaka[rodInd[0]], poljeGresaka[rodInd[1]]);
            RjesenjeOblik mutiranoDijete = mutacija(dijete,rand, p1, p2, t);
            kopijaKrom(mutiranoDijete, populacija[indNajLos]);
            poljeGresaka[indNajLos] = fun.kazna(mutiranoDijete.kromosomi, podaci.ulazi1, podaci.ulazi2,podaci.izlazi, mreza);
            int najInd = nadiNaj(poljeGresaka);
            System.out.println(i + " " +poljeGresaka[najInd]);
            if(poljeGresaka[najInd] > poljeGresaka[indeksNajboljeg]){
                System.out.println("Greska se povecala...");
                break;
            }
            indeksNajboljeg = najInd;
            if(poljeGresaka[najInd] < 0.0000001){
                return populacija;
            }


        }

        int najInd = nadiNaj(poljeGresaka);
        populacija[najInd].kazna = poljeGresaka[najInd];
        //System.out.println("Najbolja jedinka: "+populacija[najInd].kromosomi[0] + " " + populacija[najInd].kromosomi[1] + " " + populacija[najInd].kromosomi[2] + " " + populacija[najInd].kromosomi[3] + " " + populacija[najInd].kromosomi[4]);
        System.out.println(poljeGresaka[najInd]);


        return populacija;

    }

    public static RjesenjeOblik[] initPopul(int brTez, int vel, Random rand) {
        double min = 0;
        double max = 1;
        RjesenjeOblik[] populacija = new RjesenjeOblik[vel];
        for (int i = 0; i < vel; i++) {
            populacija[i] = new RjesenjeOblik(brTez);
        }
        for (int i = 0; i < vel; i++) {
            for (int j = 0; j < brTez; j++) {
                populacija[i].kromosomi.add(min + rand.nextDouble() * (max - min));
            }
        }
        return populacija;
    }

    public static RjesenjeOblik krizanje(int brKrom, RjesenjeOblik rod1, RjesenjeOblik rod2, Random rand, double kaz1, double kaz2){
        RjesenjeOblik dijete = new RjesenjeOblik(brKrom);
        //kopijaKrom(rod1,dijete);//.copy

        if(rand.nextInt(100) < 25){
            for(int i = 0; i<rod1.kromosomi.size(); i++){
                if(rand.nextDouble()<0.5){
                    dijete.kromosomi.add(rod1.kromosomi.get(i));
                }
                else{
                    dijete.kromosomi.add(rod2.kromosomi.get(i));
                }
                //Double d = Math.abs(rod1.kromosomi.get(i) - rod2.kromosomi.get(i));
                //Double u = Math.min(rod1.kromosomi.get(i), rod2.kromosomi.get(i)) + 0.05*d + rand.nextDouble()*(Math.max(rod1.kromosomi.get(i), rod2.kromosomi.get(i))-0.05*d - (Math.min(rod1.kromosomi.get(i), rod2.kromosomi.get(i))) + 0.05*d);
               // dijete.kromosomi.set(i, u);
            }
        }
        else if(rand.nextInt(100)< 85){
            for(int i = 0; i<rod1.kromosomi.size(); i++){
                dijete.kromosomi.add(0.5 * (rod1.kromosomi.get(i) + rod2.kromosomi.get(i)));
            }
        }
        else{
            if(kaz1 < kaz2) {
                for (int i = 0; i < rod1.kromosomi.size(); i++) {
                    dijete.kromosomi.add(0.5*(rod1.kromosomi.get(i)-rod2.kromosomi.get(i))+0.5*rod1.kromosomi.get(i));
                }
            }else{
                for (int i = 0; i < rod2.kromosomi.size(); i++) {
                    dijete.kromosomi.add(0.5*(rod2.kromosomi.get(i)-rod1.kromosomi.get(i))+0.5*rod2.kromosomi.get(i));
                }
            }
        }

        return dijete;
    }

    public static RjesenjeOblik mutacija(RjesenjeOblik dijete, Random rand, double pm1, double pm2, double[] t){
        Double sig;
        RjesenjeOblik novoDijete = new RjesenjeOblik(dijete.brjed());
        if(rand.nextDouble() < (t[0]/(t[0]+t[1]+t[2]))){
            sig = 3.0;
            for(int i = 0; i< dijete.kromosomi.size(); i++) {
                if (rand.nextDouble() < pm1) {
                    novoDijete.kromosomi.add(rand.nextGaussian()*sig);
                }
                else novoDijete.kromosomi.add(dijete.kromosomi.get(i));
            }
        }else if(rand.nextDouble() < (t[1]/(t[0]+t[1]+t[2]))){
            sig = 7.0;
            for(int i = 0; i< dijete.kromosomi.size(); i++) {
                if (rand.nextDouble() < pm1) {
                    novoDijete.kromosomi.add(rand.nextGaussian()*sig);
                }
                else novoDijete.kromosomi.add(dijete.kromosomi.get(i));
            }
        }else {
            sig = 30.0;
            for (int i = 0; i < dijete.kromosomi.size(); i++) {
                if (rand.nextDouble() < pm2) {
                    novoDijete.kromosomi.add(rand.nextGaussian()*sig);
                }
                else novoDijete.kromosomi.add(dijete.kromosomi.get(i));
            }
        }
        return novoDijete;


    }

    public static void main(String[] s){

        Dataset podaci = new Dataset("C://Users/Izabela/Desktop/nenr7/zaNeuro.txt");
        NeuronskaMreza mreza = new NeuronskaMreza(2,8,4,3);
        IFunction fun = new IFunction();
        //dosad najbolje {0.8,0.1,0.0001}
        double[] t = {0.8, 0.1, 0.0001};
        RjesenjeOblik naj = new RjesenjeOblik(mreza.brTez);
        int najInd;
        for(int j = 0; j<1; j++) {
            RjesenjeOblik[] rjesenje = elLoop(mreza, podaci, 100, new Random(), fun, 1000000, 0.009, 0.007, t);//50, 0.009, 0.007
            RjesenjeOblik najboljaJedinka = rjesenje[indeksNajboljeg];
            if(j == 0) {
                naj = najboljaJedinka;
                najInd = indeksNajboljeg;
            }else if(najboljaJedinka.kazna < naj.kazna){
                naj = najboljaJedinka;
                najInd = indeksNajboljeg;
            }
            for (int i = 0; i < najboljaJedinka.kromosomi.size(); i++) {
                System.out.print(najboljaJedinka.kromosomi.get(i) + " ");
            }
            System.out.println();
        }

        System.out.println("Najbolja jedinka u 5 pokretanja.");
        System.out.println(naj.kazna);
        for (int i = 0; i < naj.kromosomi.size(); i++) {
            System.out.print(naj.kromosomi.get(i) + " ");
        }

        try{
            FileWriter najboljaJedinka= new FileWriter("w.txt");
            FileWriter cijelaNajboljaJedinka= new FileWriter("sveTez.txt");

            //rezultati4pop.write("Pop30,Pop50,Pop70,Pop100,Pop200\n");
            for(int i = 0; i< naj.kromosomi.size(); i++){

                if(i%2 == 0 && i < mreza.tezine1) {
                    najboljaJedinka.write(naj.kromosomi.get(i) + "\n");
                }
                cijelaNajboljaJedinka.write(naj.kromosomi.get(i) +"\n");
            }
            najboljaJedinka.close();
            cijelaNajboljaJedinka.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static int nadiNaj(Double[] greske){
        int najInd = 0;
        Double najmanjaGre = greske[0];
        for (int i = 1; i < greske.length; i++){
            if(najmanjaGre > greske[i]) {
                najmanjaGre = greske[i];
                najInd = i;
            }
        }
        return najInd;
    }

    public static void kopijaKrom(RjesenjeOblik prviKrom, RjesenjeOblik kopijaKrom){
        kopijaKrom.kromosomi.clear();
        for(int j = 0; j<prviKrom.brjed(); j++){
            kopijaKrom.kromosomi.add(prviKrom.kromosomi.get(j));
        }
    }
}
