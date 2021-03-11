public class KormiloBaza {
    Defuzzifier def;
    public KormiloBaza(Defuzzifier def){
        this.def = def;
    }

    SimpleDomain oriDom = (SimpleDomain) Domain.intRange(0,2);
    SimpleDomain udaljDom = (SimpleDomain) Domain.intRange(0,130); //1300/10
    SimpleDomain brizDom = (SimpleDomain) Domain.intRange(0,130);
    SimpleDomain kutDom = (SimpleDomain) Domain.intRange(-91,91);

    CalculatedFuzzySet kriticno_blizu= new CalculatedFuzzySet(udaljDom, StandardFuzzySets.IFunction(20, 55));
    CalculatedFuzzySet blizu= new CalculatedFuzzySet(udaljDom, StandardFuzzySets.IFunction(45, 55));
    CalculatedFuzzySet krivi_smjer = new CalculatedFuzzySet(oriDom, StandardFuzzySets.IFunction(0,1));

    CalculatedFuzzySet skreni_ostro_lijevo = new CalculatedFuzzySet(kutDom, StandardFuzzySets.gammaFuntion(175, 180));
    CalculatedFuzzySet skreni_ostro_desno = new CalculatedFuzzySet(kutDom, StandardFuzzySets.IFunction(0,5));
    CalculatedFuzzySet promijeni_smjer = new CalculatedFuzzySet(oriDom, StandardFuzzySets.gammaFuntion(179, 180));

    Pravilo skreniLijevo = new Pravilo("",  skreni_ostro_lijevo, blizu);
    Pravilo skreniDesno = new Pravilo("", skreni_ostro_desno, blizu);
    Pravilo promijeniSmjer = new Pravilo("", promijeni_smjer, krivi_smjer);
}
