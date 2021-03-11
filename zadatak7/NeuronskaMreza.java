import java.util.ArrayList;

public class NeuronskaMreza {

    int brUlNeur;
    int brNeur1;
    int brSlNeur2[];
    int brTez;
    int tezine1;
    int tezine2;


    public NeuronskaMreza(int... neuroni){
        if(neuroni.length < 3){
            System.out.println("Nedovoljan broj slojeva");
        }
        else{
            this.brUlNeur = neuroni[0];
            this.brNeur1 = neuroni[1];
            int brOstalihSlojeva = neuroni.length - 2;
            this.brSlNeur2 = new int[brOstalihSlojeva];
            for(int i = 0; i < brOstalihSlojeva; i++){
                brSlNeur2[i] = neuroni[i+2];
            }
            stvoriPoljeDoublova(this.brUlNeur, this.brNeur1, this.brSlNeur2);
        }
    }

    public void stvoriPoljeDoublova(int ulaz, int prviSloj, int[] ostaliSlojevi){
        this.tezine1 = prviSloj*2*ulaz;
        int privTezine2 = 0;
        for(int i = 0; i < ostaliSlojevi.length; i++){
            int tezPoSloju = 0;
            if(i == 0) {
                tezPoSloju = ostaliSlojevi[i] * (this.brNeur1 + 1);
            } else{
                tezPoSloju = ostaliSlojevi[i] * (ostaliSlojevi[i-1] + 1);
            }
            privTezine2+=tezPoSloju;
        }
        this.tezine2 = privTezine2;
        this.brTez = tezine1 + tezine2;
    }
}
