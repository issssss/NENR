public interface IDomain extends Iterable<DomainElement> {

    public int getCardinality();
    public IDomain getComponent(int indeks);
    public int getNumberOfComponents();
    public int indexOfElement(DomainElement element);
    public DomainElement elementForIndex(int ind);
}
