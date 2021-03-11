public class CalculatedFuzzySet implements IFuzzySet {

    IDomain domena;
    IIntUnaryFunction funkcija;

    public CalculatedFuzzySet(IDomain dom, IIntUnaryFunction fun){
        domena = dom;
        funkcija = fun;

    }
    @Override
    public IDomain getDomain() {
        return domena;
    }

    @Override
    public double getValueAt(DomainElement el) {
        int indEl;
        indEl = domena.indexOfElement(el);
        return funkcija.valueAt(indEl);
    }
}
