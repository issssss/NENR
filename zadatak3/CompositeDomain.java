import java.util.Iterator;

public class CompositeDomain extends Domain {

    private SimpleDomain[] dom;

    public CompositeDomain(SimpleDomain... domene){
        dom = domene;
    }

    @Override
    public int getCardinality() {
        int card = 1;
        for(int i = 0; i< dom.length; i++){
            card*= dom[i].getCardinality();
        }
        return card;
    }

    @Override
    public IDomain getComponent(int indeks) {
        return dom[indeks];
    }

    @Override
    public int getNumberOfComponents() {
        return dom.length;
    }

    @Override
    public Iterator<DomainElement> iterator() {
        CompositeDomainIterator iterator = new CompositeDomainIterator(dom);
        return iterator;
    }
}
class CompositeDomainIterator implements Iterator{
    SimpleDomain[] dome;
    int[] trenutni;
    int zast = 0;
    public CompositeDomainIterator (SimpleDomain... domene){
        dome = new SimpleDomain[domene.length];
        dome = domene;
        trenutni = new int[dome.length];
        for(int i = 0; i<dome.length; i++){
            trenutni[i]=dome[i].getFirst();
        }

    }
    @Override
    public boolean hasNext() {
        for(int i = 0; i<dome.length; i++){
            if(trenutni[i] != dome[i].getLast()-1)
                return true;
        }
        return false;
    }

    @Override
    public DomainElement next() {
        if (zast == 0) {
            zast = 1;
            DomainElement element = new DomainElement(trenutni);
            return element;

        } else {
            for (int i = dome.length - 1; i >= 0; i--) {
                if (trenutni[i] < dome[i].getLast()-1) {
                    trenutni[i]++;
                    DomainElement element = new DomainElement(trenutni);
                    return element;
                }
                else{
                    for(int j = dome.length - 1; j >= i; j--){
                        trenutni[j] = dome[j].getFirst();
                    }
                }
            }
        }
        DomainElement element = new DomainElement(trenutni);
        return element;
    }

}