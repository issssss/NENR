import java.util.Iterator;
public class DomainElement {

    int[] values;
    public DomainElement(int... vrijednosti)
    {
        values = new int[vrijednosti.length];
        values = vrijednosti; //provjeriti treba li ovo iterativno dodati
    }

    public int getNumberOfComponents()
    {
        return values.length;
    }

    public int getComponentValue(int ind){
        return values[ind];
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(DomainElement obj) {

        for(int i = 0; i < obj.values.length; i++){
            if(this.values[i] != obj.values[i])
                return false;
        }
            return true;
    }

    @Override
    public String toString() {
        String str = new String();
        if(this.values.length == 1){
            str = Integer.toString(values[0]);
            return str;

        }
        str+= "(";
        for(int i = 0; i< this.values.length; i++){
            if(i == this.values.length-1){
                str += this.values[i] + ")";
            }
            else{
                str += this.values[i] + ",";
            }

        }
        return str;
    }

    public static DomainElement of(int... vrij)
    {
        DomainElement el = new DomainElement(vrij);
        return el;
    }
}
