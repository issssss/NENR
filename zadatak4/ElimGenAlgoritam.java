import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class ElimGenAlgoritam {

    public ElimGenAlgoritam(){}

    public static RjesenjeOblik[] elLoop(int velPop, Random rand, IFunction fun, int maxIter){
        RjesenjeOblik[] populacija = initPopul(velPop, rand);
        Double[] poljeGresaka = new Double[velPop];

        for(int i = 0; i<velPop; i++){
            poljeGresaka[i] = fun.kazna(populacija[i].kromosomi);
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
            RjesenjeOblik dijete = krizanje(populacija[rodInd[0]], populacija[rodInd[1]],rand);
            mutacija(dijete,rand, 0.5);
            for(int j = 0; j<populacija.length; j++){
                int zast = 0;
                if(populacija[j] == null) break;
                for(int k = 0; k<populacija[j].brjed(); k++){
                    if(populacija[j].kromosomi[k] != dijete.kromosomi[k])
                        break;
                    else if(j == populacija[j].brjed()-1)
                        zast = 1;
                }
                if(zast == 1) {
                    mutacija(dijete,rand,0.5);
                    break;
                }
            }
            kopijaKrom(dijete, populacija[indNajLos]);
            poljeGresaka = new Double[velPop];

            for(int j = 0; j < velPop; j++){
                poljeGresaka[j] = fun.kazna(populacija[j].kromosomi);
            }
        }
        for(int i = 0; i< populacija.length; i++) {
            System.out.println(populacija[i].kromosomi[0] + " " + populacija[i].kromosomi[1] + " " + populacija[i].kromosomi[2] + " " + populacija[i].kromosomi[3] + " " + populacija[i].kromosomi[4]);
            System.out.println(poljeGresaka[i]);
        }
        int najInd = nadiNaj(poljeGresaka);
        System.out.println("Najbolja jedinka: "+populacija[najInd].kromosomi[0] + " " + populacija[najInd].kromosomi[1] + " " + populacija[najInd].kromosomi[2] + " " + populacija[najInd].kromosomi[3] + " " + populacija[najInd].kromosomi[4]);
        System.out.println(poljeGresaka[najInd]);

        return populacija;

    }

    public static RjesenjeOblik[] initPopul(int vel, Random rand) {
        double min = -4;
        double max = 4;
        RjesenjeOblik[] populacija = new RjesenjeOblik[vel];
        for (int i = 0; i < vel; i++) {
            populacija[i] = new RjesenjeOblik(5);
        }
        for (int i = 0; i < vel; i++) {
            for (int j = 0; j < 5; j++) {
                populacija[i].kromosomi[j] = min + rand.nextDouble() * (max - min);
            }
        }
        return populacija;
    }

    public static RjesenjeOblik krizanje(RjesenjeOblik rod1, RjesenjeOblik rod2, Random rand){
        RjesenjeOblik dijete = new RjesenjeOblik(5);
        kopijaKrom(rod1,dijete);//.copy

        if(rand.nextInt(100) < 33){
            for(int i = 0; i<dijete.brjed(); i++){
                Double d = Math.abs(rod1.kromosomi[i]-rod2.kromosomi[i]);
                Double u = Math.min(rod1.kromosomi[i], rod2.kromosomi[i]) + 0.05*d + rand.nextDouble()*(Math.max(rod1.kromosomi[i], rod2.kromosomi[i])-0.05*d - (Math.min(rod1.kromosomi[i], rod2.kromosomi[i])) + 0.05*d);
                dijete.kromosomi[i] = u;
            }
        }
        else if(rand.nextInt(100)<66){
            for(int i = 0; i<dijete.brjed(); i++){
                dijete.kromosomi[i] = 0.5*(rod1.kromosomi[i]+rod2.kromosomi[i]);
            }
        }
        else {
            for (int i = 0; i < dijete.brjed(); i++) {
                dijete.kromosomi[i] = rand.nextBoolean() ? rod1.kromosomi[i] : rod2.kromosomi[i];
            }
        }

        return dijete;
    }

    public static void mutacija(RjesenjeOblik dijete, Random rand, double p){
        int ind = rand.nextInt(dijete.brjed());
        if(rand.nextDouble() < p) {
            dijete.kromosomi[ind] = -4 + rand.nextDouble()*(4-(-4));
        }

    }

    public static void main(String[] s){

        int br = 0;
        IFunction skup = new IFunction(250);
        try {
            File myObj = new File("C://Users/Izabela/Desktop/skup1.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] ulazi = data.split("\t");
                skup.x[br] = Double.parseDouble(ulazi[0]);
                skup.y[br] = Double.parseDouble(ulazi[1]);
                skup.rj[br] = Double.parseDouble(ulazi[2]);
                br++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        RjesenjeOblik[] rj = elLoop(50, new Random(), skup, 10000);

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
        for(int j = 0; j<prviKrom.brjed(); j++){
            kopijaKrom.kromosomi[j] = prviKrom.kromosomi[j];
        }
    }
}
