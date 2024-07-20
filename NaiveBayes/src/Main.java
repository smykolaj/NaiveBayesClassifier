public class Main
{
    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();
        new Classifier();
        long endTime = System.currentTimeMillis(); // End time in milliseconds
        long duration = endTime - startTime; // Compute duration in milliseconds

        System.out.println("Execution time in milliseconds: " + duration);
    }
}