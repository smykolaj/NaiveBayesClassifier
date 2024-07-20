import java.nio.file.Path;
import java.util.*;

public class Classifier
{

    String positive = "e";
    String negative = "p";
    Path trainSet = Path.of("agaricus-lepiota.data");
    Path testSet = Path.of("agaricus-lepiota.test.data");

    ArrayList<Vector> vectors = new ArrayList<>();
    HashMap<String, Integer> uniqueNamesAndCounts = new HashMap<>();
    HashMap<String, Integer> uniqueCombinationAndCount = new HashMap<>();
    HashMap<String, Double> probabilities = new HashMap<>();
    HashMap<String, Integer> outputs = new HashMap<>();


    public Classifier(){
        readFile();
        setClassesCount();
        setUniqueCombinationAndCount();
        setProbabilities();
//        System.out.println(uniqueNamesAndCounts);
//        System.out.println(uniqueCombinationAndCount);
//        System.out.println(probabilities);

        outputs.put("TP", 0);
        outputs.put("FP", 0);
        outputs.put("TN", 0);
        outputs.put("FN", 0);
        classifyTestSet();
    }

    private void readFile()
    {
        Scanner sc;
        try
        {
            sc = new Scanner(trainSet);
            while (sc.hasNextLine())
            {

                String data = sc.nextLine();
                Vector v = new Vector(data);
                vectors.add(v);

            }
        } catch (Exception e)
        {
            System.out.println("File not found");
        }
    }



    private void setClassesCount(){
        for(Vector v : vectors){
            uniqueNamesAndCounts.put(v.name, uniqueNamesAndCounts.getOrDefault(v.name, 0)+1);
        }
    }

    private void setUniqueCombinationAndCount(){
        for (Vector v : vectors){
            for (int i = 0; i<vectors.getFirst().getLength(); i++){
                uniqueCombinationAndCount.put((v.name+i+v.values.get(i)), uniqueCombinationAndCount.getOrDefault((v.name+i+v.values.get(i)), 0)+1);
            }
        }
    }

    private void setProbabilities(){
        for(String key : uniqueCombinationAndCount.keySet()){
            double probability = (double) uniqueCombinationAndCount.get(key) /uniqueNamesAndCounts.get(key.split("[0-9]+")[0]);
            probabilities.put(key, probability );
        }
    }

    private String classifyVector(Vector v){
        HashMap<String,Double> vectorProbabilities = new HashMap<>();
        int i = 0;
        for(String attribute : v.values){
            for (String decision : uniqueNamesAndCounts.keySet()){
                Double prob = probabilities.getOrDefault(decision+i+attribute, smoothedValue(decision, i) );
                vectorProbabilities.put(decision, vectorProbabilities.getOrDefault(decision, 1.0)*prob);
            }
            i++;
        }
        Double biggestProb = Collections.max(vectorProbabilities.values());
        String choice = "";
        for (String decicion : vectorProbabilities.keySet()){
            if (vectorProbabilities.get(decicion).equals(biggestProb))
                choice = decicion;

        }
        if(v.name.equals(negative) && choice.equals(negative))
            return "TN";
        if(v.name.equals(negative) && choice.equals(positive))
            return "FP";
        if (v.name.equals(positive) && choice.equals(positive))
            return "TP";
        return "FN";
    }

    private double smoothedValue(String decision, int i){
        int amount = 0;
        for(String keyInSet : uniqueCombinationAndCount.keySet()){
            if(keyInSet.contains(decision+i))
                amount++;
        }
        return (double) 1 /(uniqueNamesAndCounts.get(decision) + amount);
    }

    public void classifyTestSet()
    {
        double correct = 0.0;
        double total = 0.0;
        for (Vector v : returnTestSet())
        {
            String res = classifyVector(v);
            //System.out.println(res);
            outputs.put(res, outputs.get(res)+1);
        }
        System.out.println(outputs);
        double precision = ((double) outputs.get("TP") /(outputs.get("TP") + outputs.get("FP") ));
        double recall = ((double)outputs.get("TP")/(outputs.get("TP") + outputs.get("FN") ));
        double fMeasure = (2*precision*recall/(precision+recall));
        double accuracy = ((double)(outputs.get("TP") + outputs.get("TN"))/(outputs.get("TP") + outputs.get("FP") + outputs.get("TN") + outputs.get("FN") ));
        System.out.println("Accuracy for test set: " +String.format("%.2f", accuracy*100));
        System.out.println("Precision for test set: " +String.format("%.2f", precision*100));
        System.out.println("Recall for test set: " +String.format("%.2f", recall*100));
        System.out.println("F-Measure for test set: " +String.format("%.2f", fMeasure*100));

    }
    public ArrayList<Vector> returnTestSet()
    {
        Scanner sc = null;
        ArrayList<Vector> testData = new ArrayList<>();
        try
        {
            sc = new Scanner(testSet);
            while (sc.hasNextLine())
            {
                String data = sc.nextLine();
                testData.add(new Vector(data));
            }
        } catch (Exception e)
        {
            System.out.println("File not found");
        }
        return testData;
    }


}
