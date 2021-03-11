public class MutableFuzzySet implements IFuzzySet{

    double[] memberships;
    IDomain dome;
    public MutableFuzzySet(IDomain domena){
        memberships = new double[domena.getCardinality()];
        dome = domena;

    }

    @Override
    public IDomain getDomain() {
        return dome;
    }

    @Override
    public double getValueAt(DomainElement el) {
        int indEl;
        indEl = dome.indexOfElement(el);
        return memberships[indEl];
    }

    public MutableFuzzySet set(DomainElement el, double memb){
        int ind;
        ind = dome.indexOfElement(el);
        this.memberships[ind] = memb;
        return this;
    }
}
