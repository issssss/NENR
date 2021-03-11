import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class GenerGenAlgoritam {

    public GenerGenAlgoritam(){}

    public static RjesenjeOblik krizanje(RjesenjeOblik rod1, RjesenjeOblik rod2, Random rand){
        RjesenjeOblik dijete = new RjesenjeOblik(5); //.copy
        kopijaKrom(rod1, dijete);
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

    public static RjesenjeOblik[] proporcionalnaSelekcija(RjesenjeOblik[] populacija, Random rand, int kolicina, boolean elit){
        @SuppressWarnings("unchecked")
        RjesenjeOblik[] roditelji = new RjesenjeOblik[kolicina];
        for(int i = 0; i<kolicina; i++){
            roditelji[i] = new RjesenjeOblik(5);
        }

        double sum = 0;

        for(int i = 0; i<populacija.length; i++){
            sum += populacija[i].dobrota; //implementirati
        }

        for(int ind = 0; ind < kolicina; ind++){
            double r = rand.nextDouble();
            double lim = r*sum;
            int chosen = 0;
            double gornjaGran = populacija[chosen].dobrota;
            while(lim > gornjaGran && chosen < populacija.length-1){
                chosen++;
                gornjaGran+= populacija[chosen].dobrota;
            }
            kopijaKrom(populacija[chosen], roditelji[ind]);

            for(int j = 0; j<roditelji.length; j++){
                int zast = 0;
                for(int k = 0; k<roditelji[j].brjed(); k++){
                    if(roditelji[j].kromosomi[k] != roditelji[ind].kromosomi[k])
                        break;
                    else if(j == roditelji[j].brjed()-1)
                        zast = 1;
                }
                if(zast == 1) {
                    ind--;
                    break;
                }
            }
        }
        return roditelji;

    }

    public static RjesenjeOblik[] initPopul(int vel, Random rand){
        double min = -4;
        double max = 4;
        RjesenjeOblik[] populacija = new RjesenjeOblik[vel];
        for(int i = 0; i < vel; i++){
            populacija[i] = new RjesenjeOblik(5);
        }
        for(int i = 0; i < vel; i++){
            for(int j = 0; j < 5; j++){
                populacija[i].kromosomi[j] = min + rand.nextDouble()*(max-min);
            }
        }
        return populacija;
    }

    public static RjesenjeOblik gaLoop(IFunction fun, int vel, double p, Random rand, int genLim, boolean el){
        RjesenjeOblik[] populacija = initPopul(vel, rand);
        for(int i = 0; i < vel; i++){
            populacija[i].kazna = fun.kazna(populacija[i].kromosomi);
        }
        for(int j = 0; j < vel; j++){
            populacija[j].dobrota = fun.dobrota(populacija,populacija[j]);
        }


        for(int gen = 1; gen <= genLim; gen++){
            RjesenjeOblik[] novaPop = new RjesenjeOblik[vel];
            if(el){
                RjesenjeOblik najbolji = nadiNaj(populacija);
                kopijaKrom(najbolji, novaPop[0] = new RjesenjeOblik(5));
                System.out.println(gen);
                for (int k = 0; k < najbolji.brjed(); k++) {
                    System.out.print(najbolji.kromosomi[k] + " ");
                }
                System.out.println();
                System.out.println("Dobrota: "+najbolji.dobrota+ " kazna: "+najbolji.kazna);

                for (int i = 1; i< vel; i++){
                    RjesenjeOblik[] roditelji = proporcionalnaSelekcija(populacija, rand, 2, el);
                    RjesenjeOblik dijete = krizanje(roditelji[0], roditelji[1], rand);
                    mutacija(dijete, rand, p);
                    for(int j = 0; j<novaPop.length; j++){
                        int zast = 0;
                        if(novaPop[j] == null) break;
                        for(int k = 0; k<novaPop[j].brjed(); k++){
                            if(novaPop[j].kromosomi[k] != dijete.kromosomi[k])
                                break;
                            else if(j == novaPop[j].brjed()-1)
                                zast = 1;
                        }
                        if(zast == 1) {
                            mutacija(dijete,rand,p);
                            break;
                        }
                    }
                    kopijaKrom(dijete, novaPop[i] = new RjesenjeOblik(5));
            }
            }
            else{
                for (int i = 0; i< vel; i++){
                    RjesenjeOblik[] roditelji = proporcionalnaSelekcija(populacija, rand, 2, el);
                    RjesenjeOblik dijete = krizanje(roditelji[0], roditelji[1], rand);
                    mutacija(dijete, rand, p);
                    for(int j = 0; j<novaPop.length; j++){
                        int zast = 0;
                        if(novaPop[j] == null) break;
                        for(int k = 0; k<novaPop[j].brjed(); k++){
                            if(novaPop[j].kromosomi[k] != dijete.kromosomi[k])
                                break;
                            else if(j == novaPop[j].brjed()-1)
                                zast = 1;
                        }
                        if(zast == 1) {
                            mutacija(dijete,rand,p);
                            break;
                        }
                    }
                    kopijaKrom(dijete, novaPop[i] = new RjesenjeOblik(5));
                }

            }
            kopijaPop(novaPop, populacija); //populacija = novaPop;
            for(int i = 0; i < vel; i++){
                populacija[i].kazna = fun.kazna(populacija[i].kromosomi);
            }
            for(int i = 0; i < vel; i++){
                populacija[i].dobrota = fun.dobrota(populacija, populacija[i]);
            }

        }
        return nadiNaj(populacija);
    }

    public static RjesenjeOblik nadiNaj(RjesenjeOblik[] pop){
        RjesenjeOblik najbolji = new RjesenjeOblik(5);
        kopijaKrom(pop[0], najbolji);
        najbolji.dobrota = pop[0].dobrota;
        najbolji.kazna = pop[0].kazna;
        for (int i = 1; i < pop.length; i++){
            if(najbolji.dobrota < pop[i].dobrota) {
                kopijaKrom(pop[i],najbolji);
                najbolji.dobrota = pop[i].dobrota;
                najbolji.kazna = pop[0].kazna;
            }
        }
        return najbolji;
    }

    public static void kopijaPop(RjesenjeOblik[] prva, RjesenjeOblik[] kopija){
        for(int i = 0; i<prva.length; i++){
            kopijaKrom(prva[i], kopija[i]);
        }
    }

    public static void kopijaKrom(RjesenjeOblik prviKrom, RjesenjeOblik kopijaKrom){
        for(int j = 0; j<prviKrom.brjed(); j++){
            kopijaKrom.kromosomi[j] = prviKrom.kromosomi[j];
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

        RjesenjeOblik rj = gaLoop(skup, 50,0.5, new Random(), 10000, true);

        System.out.println("Najbolja: " + rj.kromosomi[0]+ " "+rj.kromosomi[1]+ " "+ rj.kromosomi[2]+ " "+ rj.kromosomi[3]+ " "+ rj.kromosomi[4]);
        System.out.println(rj.kazna);
    }



}
