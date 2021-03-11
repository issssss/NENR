import java.util.Iterator;

public class SimpleDomain extends Domain {

    private int first;
    private int last;

    public SimpleDomain(int fir, int las){
        first = fir;
        last = las;
    }
    //prilikom implementacije vidjeti što treba overrideati iz Domain razreda
    @Override
    public int getCardinality() {

        int br=0;
        for(int i = first; i < last; i++){
            br++;
        }
        return br;
    }

    @Override
    public IDomain getComponent(int indeks) {
       return this;
    }

    @Override
    public int getNumberOfComponents() {
        return 1;
    }

    @Override
    public Iterator<DomainElement> iterator() {
        SimpleDomainIterator iterator = new SimpleDomainIterator(getFirst(),getLast());
        return iterator;
    }

    public int getFirst() {
        return first;
    }

    public int getLast(){
        return last;
    }
}

class SimpleDomainIterator implements Iterator{
    int first;
    int last;
    int trenutni;
    int zast = 0;
    public SimpleDomainIterator (int prvi, int zadnji){
        first = prvi;
        last = zadnji-1;

    }
    @Override
    public boolean hasNext() {
       if(trenutni < first){
           System.out.println("Element nije u domeni.");
           return false;
       }
       if(trenutni < last){
           return true;
       }
       return false;
    }

    @Override
    public DomainElement next() {
        if(trenutni == 0 && zast == 0){
            trenutni = first;
            zast = 1;
        }
        else if(trenutni < last){
            trenutni+=1;
        }
        DomainElement element = new DomainElement(trenutni);
        return element;
    }
}
