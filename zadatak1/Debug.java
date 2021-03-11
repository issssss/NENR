public class Debug {

    public static void main(String[] args) {
        IDomain d1 = Domain.intRange(0, 5); // {0,1,2,3,4}
        Debug.print(d1, "Elementi domene d1:");
        IDomain d2 = Domain.intRange(0, 3); // {0,1,2}
        Debug.print(d2, "Elementi domene d2:");
        IDomain d3 = Domain.combine(d1, d2);
        Debug.print(d3, "Elementi domene d3:");
        System.out.println(d3.elementForIndex(0));
        System.out.println(d3.elementForIndex(5));
        System.out.println(d3.elementForIndex(14));
        System.out.println(d3.indexOfElement(DomainElement.of(4,1)));
    }

    public static void print(IDomain domain, String headingText) {
        if(headingText!=null) {
            System.out.println(headingText);
        }
        for(DomainElement e : domain) {
            System.out.println("Element domene: " + e);
        }
        System.out.println("Kardinalitet domene je: " + domain.getCardinality());
        System.out.println();
    }

    public static void print(IFuzzySet set, String headingText) {
        if(headingText!=null) {
            System.out.println(headingText);
        }
        if(set instanceof MutableFuzzySet){
            for(DomainElement e : ((MutableFuzzySet) set).dome){
                System.out.format("d("+e+") = "+ "%.5f\n", ((MutableFuzzySet) set).memberships[((MutableFuzzySet) set).dome.indexOfElement(e)]);
            }
        }
        else if (set instanceof CalculatedFuzzySet){
            for (DomainElement e : ((CalculatedFuzzySet) set).domena){
                System.out.println("d("+e+") = "+((CalculatedFuzzySet) set).funkcija.valueAt(((CalculatedFuzzySet) set).domena.indexOfElement(e)));
            }
        }
        System.out.println();
    }
}
