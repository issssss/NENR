public class AkceleracijaBaza {

   Defuzzifier def;
    public AkceleracijaBaza(Defuzzifier def){
        this.def = def;
    }

    SimpleDomain oriDom = (SimpleDomain) Domain.intRange(0,2);
    SimpleDomain udaljDom = (SimpleDomain) Domain.intRange(0,1300); //1300/10
    SimpleDomain brizDom = (SimpleDomain) Domain.intRange(0,350);
    SimpleDomain kutDom = (SimpleDomain) Domain.intRange(-90,91);
    SimpleDomain ubrzDom = (SimpleDomain) Domain.intRange(-35,36);

    CalculatedFuzzySet prevelika_brzina = new CalculatedFuzzySet(brizDom, StandardFuzzySets.IFunction(80,350));
    CalculatedFuzzySet premala_brzina = new CalculatedFuzzySet(brizDom, StandardFuzzySets.IFunction(20, 80));

    CalculatedFuzzySet ubrzaj = new CalculatedFuzzySet(ubrzDom, StandardFuzzySets.gammaFuntion(50, 70));
    CalculatedFuzzySet uspori = new CalculatedFuzzySet(ubrzDom, StandardFuzzySets.IFunction(0, 12));

    Pravilo povecajBrzinu = new Pravilo("", ubrzaj, premala_brzina);
    Pravilo umanjiBrzinu = new Pravilo("", uspori, prevelika_brzina);

}
