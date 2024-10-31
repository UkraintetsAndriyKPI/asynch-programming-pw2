import java.util.Arrays;
import java.util.concurrent.Callable;

public class SquareArray implements Callable<Double[]> {
    private Double[] array;

    public SquareArray(Double[] array) {
        this.array = array;
    }

    @Override
    public Double[] call() {
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.pow(array[i], 2);
            array[i] = Math.round(array[i] * 100.) / 100.;
        }
        Main.resultSet.addAll(Arrays.asList(array));
        return array;
    }
}
