import java.util.Iterator;

public abstract class Domain implements IDomain {

    public void Domain() {
    }

    public static IDomain intRange(int beg, int end) {
        IDomain domena = new SimpleDomain(beg, end);
        return domena;
    }

    public static Domain combine(IDomain el, IDomain ele) {
        Domain domena = new CompositeDomain((SimpleDomain) el, (SimpleDomain) ele);
        return domena;
    }

    public int indexOfElement(DomainElement element) {
        int brojac = 0;
        int indeks = -1;
        for (DomainElement i : this) {
            if (i.equals(element)) {
                indeks = brojac;
                break;
            } else brojac++;
        }
        return indeks;

    }

    public DomainElement elementForIndex(int ind) {
        int brojac = 0;
        DomainElement element = new DomainElement();
        for (DomainElement e : this) {
            if (ind == brojac) {
                return e;
            }
            else {
                brojac++;
            }
        }
    return element;
    }
}

class DomainIterator<DomainElement> implements Iterator{

    public DomainIterator (Domain domena){

    }
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }
}