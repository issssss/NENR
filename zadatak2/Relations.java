public class Relations {

    public static boolean isUtimesURelation(IFuzzySet relation){
        if(relation.getDomain() instanceof CompositeDomain && relation.getDomain().getNumberOfComponents() == 2)
            if(relation.getDomain().getComponent(0).getCardinality() == relation.getDomain().getComponent(1).getCardinality())
                if(relation.getDomain().getComponent(0) == relation.getDomain().getComponent(1))
                    return true;
        return false;
    }

    public static boolean isSymmetric(IFuzzySet relation){
        for(DomainElement e: relation.getDomain()){
            for(DomainElement el:relation.getDomain()){
                if(e.values[0] == el.values[1] && e.values[1] == el.values[0]){
                    if(relation.getValueAt(e) != relation.getValueAt(el))
                        return false;
                }
            }
        }
        return true;
    }

    public static boolean isReflexive(IFuzzySet relation){

        for(DomainElement e : relation.getDomain()){
            if(e.values[0] == e.values[1]){
                if(relation.getValueAt(e) != 1){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isMaxMinTransitive(IFuzzySet relation){
        int prvi, drugi;
        double tnorma;
        IBinaryFunction tNorma = Operations.hamacherTNorm(1.0);
        for(DomainElement e: relation.getDomain()){
            prvi = e.values[0];
            drugi = e.values[1];
            for(DomainElement el: relation.getDomain()){
                if(el.values[0] == prvi){
                    for(DomainElement ele : relation.getDomain()){
                        if(drugi == ele.values[1] && el.values[1] == ele.values[0]){
                            tnorma = tNorma.valueAt(relation.getValueAt(el), relation.getValueAt(ele));
                            if (relation.getValueAt(e) < tnorma)
                                return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static IFuzzySet compositionOfBinaryRelations(IFuzzySet set1, IFuzzySet set2){
        IFuzzySet noviSet = new IFuzzySet() {
            @Override
            public IDomain getDomain() {
                if(set1.getDomain().getComponent(1) == set2.getDomain().getComponent(0)){
                    IDomain nova = new CompositeDomain((SimpleDomain)set1.getDomain().getComponent(0),(SimpleDomain)set2.getDomain().getComponent(1));
                    return nova;

                }
                return null;
            }

            @Override
            public double getValueAt(DomainElement el) {
                IBinaryFunction tNorma = Operations.hamacherTNorm(1.0);
                IBinaryFunction sNorma = Operations.hamacherSNorm(1.0);
                double value = 0, trenutno = 0;
                for(DomainElement e: set1.getDomain()){
                    for (DomainElement ele: set2.getDomain()){
                        if(el.values[0] == e.values[0] && el.values[1] == ele.values[1] && e.values[1]==ele.values[0]){
                            trenutno = set1.getValueAt(e);
                            if(set2.getValueAt(ele) < trenutno)
                                trenutno = set2.getValueAt(ele);
                            if(trenutno > value)
                                value = trenutno;
                        }
                    }
                }
                return value;
            }
        };
        return noviSet;
    }

    public static boolean isFuzzyEquivalence(IFuzzySet set){
        if(isUtimesURelation(set)){
            if(isSymmetric(set) && isReflexive(set) && isMaxMinTransitive(set))
                return true;
        }
        return false;
    }
}
