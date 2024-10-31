import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static CopyOnWriteArraySet<Double> resultSet = new CopyOnWriteArraySet<>();
    public final static Double[] valueRange = {0.5, 99.5};
    public final static Integer[] sizeRange = {40, 60};

    public static Double[] getRandomArray() {
        int size = (int) (Math.random() * (sizeRange[1] - sizeRange[0] + 1)) + sizeRange[0];
        Double[] array = new Double[size];
        for (int i = 0; i < size; i++) {
            array[i] = Math.random() * (valueRange[1] - valueRange[0]) + valueRange[0];
            array[i] = Math.round(array[i] * 10.) / 10.;
        }
        return array;
    }


    public static List<Double[]> splitArray(Double[] inputArray, int parts) {
        List<Double[]> subArrays = new ArrayList<>();
        int partSize = (int) Math.ceil((double) inputArray.length / parts);
        for (int i = 0; i < inputArray.length; i += partSize) {
            int end = Math.min(i + partSize, inputArray.length);
            Double[] subArray = Arrays.copyOfRange(inputArray, i, end);
            subArrays.add(subArray);
        }
        return subArrays;
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(2);
        Double[] inputArray = getRandomArray();
        System.out.println("inputArray[" + inputArray.length + "] = " + Arrays.toString(inputArray));

        Map<Double, Integer> map = new HashMap<>();
        Arrays.stream(inputArray).forEach(i -> map.put(i, map.getOrDefault(i, 0) + 1));
        System.out.println("duplicates = "
                + Arrays.toString(
                map.keySet().stream()
                        .mapToDouble(i -> i)
                        .filter(i -> map.get(i) > 1)
                        .toArray())
        );

        List<SquareArray> counterList = new ArrayList<>();
        List<Double[]> arrayParts = splitArray(inputArray, 2);
        for (Double[] arrayPart : arrayParts) {
            counterList.add(new SquareArray(arrayPart));
        }
        List<Future<Double[]>> futures = new ArrayList<>();

        // START TIME MARK
        long startTime = System.currentTimeMillis();
        for (SquareArray counter : counterList) {
            futures.add(es.submit(counter));
        }

        for (Future<Double[]> future : futures) {
            while (!future.isDone()) ;
        }

        // END TIME MARK
        long endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime) / 1000.0;

        es.shutdown();
        System.out.println("resultArray[" + resultSet.size() + "] = " + resultSet);
        System.out.println("Time in seconds : " + elapsedTime);
    }
}