import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dataset {

    List<Double> ulazi1 = new ArrayList<>();
    List<Double> ulazi2 = new ArrayList<>();
    List<Double[]> izlazi = new ArrayList<>();
    public Dataset(String path){
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] ulazi = data.split("\t");
                ulazi1.add(Double.parseDouble(ulazi[0]));
                ulazi2.add(Double.parseDouble(ulazi[1]));
                Double[] izlaz = new Double[3];
                izlaz[0] = Double.parseDouble(ulazi[2]);
                izlaz[1] = Double.parseDouble(ulazi[3]);
                izlaz[2] = Double.parseDouble(ulazi[4]);
                izlazi.add(izlaz);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
