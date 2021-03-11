public class COAdefuzzifier implements Defuzzifier {
    @Override
    public int defuzzy(MutableFuzzySet set) {
        double sumBroj = 0;
        double sumNaz = 0;
        for(int i = 0; i < set.getDomain().getCardinality(); i++) {
            sumBroj += set.dome.elementForIndex(i).values[0] * set.getValueAt(set.getDomain().elementForIndex(i));
            //System.out.println(set.dome.elementForIndex(i).values[0] + " " +set.getValueAt(set.getDomain().elementForIndex(i)));
            sumNaz += set.getValueAt(set.getDomain().elementForIndex(i));
        }
        if(sumNaz == 0){
            //System.out.println("Nope");
            return 0;
        }
        int def = (int)Math.round(sumBroj/sumNaz);
        return def;
    }
}
