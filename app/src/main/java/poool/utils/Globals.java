package poool.utils;

import java.util.function.BiFunction;

public class Globals {

    public static int MAX_THREADS = Runtime.getRuntime().availableProcessors() + 1;

    public static double MIN_VELOCITY = 0.001;

    public static double FRICTION_COEFFICIENT = 0.25;

    public static double RESTITUTION_COEFFICIENT = 1;

    public static int GRID_ROWS = extractGridComponent((rows, cols) -> rows);

    public static int GRID_COLS = extractGridComponent((rows, cols) -> cols);

    public static double HOLE_RADIUS = 0.2;

    public static double PLAYER_SPPED = 0.8;

    private Globals() {}

    private static int extractGridComponent(final BiFunction<Integer, Integer, Integer> extractor) {
        int rows = 1;
        int cols = MAX_THREADS;
        for (int i = (int) Math.sqrt(MAX_THREADS); i >= 1; i--) {
            if (MAX_THREADS % i == 0) {
                rows = i;
                cols = MAX_THREADS / i;
                break;
            }
        }
        if (rows > cols) {
            int temp = rows;
            rows = cols;
            cols = temp;
        }
        return extractor.apply(rows, cols);
    }

}
