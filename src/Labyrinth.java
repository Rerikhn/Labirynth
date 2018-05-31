import java.util.ArrayList;

/**
 * В этой программе реализовано два способа поиска пути
 * 1 - обычный (поиск по 4-ем смежным клеткам)
 * 2 - ортогонально-диагональный (+ ещё 4 проверки по клеткам)
 */
public class Labyrinth {

    private final static int W = 10;         // ширина рабочего поля
    private final static int H = 10;         // высота рабочего поля
    private final static int WALL = -1;      // непроходимая ячейка
    private final static int BLANK = -2;     // свободная непомеченная ячейка
    private static int LENGTH = 1;           // длина пути

    private static int grid[][] = {
            // рабочее поле
            // -1 стена
            // -2 клетки, по которым можно пройти
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -2, -2, -1, -2, -2, -2, -2, -2, -1},
            {-1, -2, -2, -1, -2, -2, -1, -1, -2, -1},
            {-1, -2, -2, -1, -2, -2, -1, -2, -2, -1},
            {-1, -2, -2, -2, -2, -2, -1, -2, -2, -1},
            {-1, -2, -2, -1, -2, -2, -1, -1, -2, -1},
            {-1, -2, -2, -1, -2, -2, -1, -1, -2, -1},
            {-1, -2, -2, -1, -2, -2, -1, -2, -2, -1},
            {-1, -2, -2, -1, -2, -2, -2, -2, -2, -1},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
    };

    public static void main(String[] args) {
        //int ax, ay, bx, by;

        // распространим волну
        Wave(2, 6, 7, 3);
        // печать результата волны
        printWave(grid);
        // печать наикратчайшего пути
        findPath(2, 6, 7, 3);
    }

    private static void printWave(int[][] grid) {
        for (int[] aGrid : grid) {
            System.out.print("\n");
            for (int x = 0; x < grid.length; x++) {
                System.out.print((aGrid[x] == -1 ? "#" : aGrid[x]) + "\t");
            }
        }
    }

    /**
     * Нахождение кратчайшего пути
     */
    private static void findPath(int ax, int ay, int bx, int by) {
        ArrayList<Integer> ix = new ArrayList<>();
        ArrayList<Integer> iy = new ArrayList<>();

        // запоминаем координаты финиша
        ix.add(0, bx);
        iy.add(0, by);

        int carrot = grid[by][bx] - 1;
        int rabbit = grid[ay][ax];

        System.out.println("\nCarrot = " + carrot);

        do {
            if ((iy.get(0) - 1) >= 0 && grid[iy.get(0) - 1][ix.get(0)] == carrot) {
                grid[iy.get(0) - 1][ix.get(0)] = -5;
                iy.add(0, iy.get(0) - 1);
                ix.add(0, ix.get(0));
                carrot--;
                LENGTH++;
            } else if ((iy.get(0) + 1) < grid.length && grid[iy.get(0) + 1][ix.get(0)] == carrot) {
                grid[iy.get(0) + 1][ix.get(0)] = -5;
                iy.add(0, iy.get(0) + 1);
                ix.add(0, ix.get(0));
                carrot--;
                LENGTH++;
            } else if ((ix.get(0) - 1) >= 0 && grid[iy.get(0)][ix.get(0) - 1] == carrot) {
                grid[iy.get(0)][ix.get(0) - 1] = -5;
                iy.add(0, iy.get(0));
                ix.add(0, ix.get(0) - 1);
                carrot--;
                LENGTH++;
            } else if ((ix.get(0) + 1) < grid.length && grid[iy.get(0)][ix.get(0) + 1] == carrot) {
                System.out.println(grid[iy.get(0)][ix.get(0) + 1]);
                grid[iy.get(0)][ix.get(0) + 1] = -5;
                iy.add(0, iy.get(0));
                ix.add(0, ix.get(0) + 1);
                carrot--;
                LENGTH++;
            }
        } while (grid[iy.get(0)][ix.get(0)] != rabbit && carrot != 0);

        grid[by][bx] = 255;
        System.out.print("\nShortest path: S - Start, F - finish. "
                + "\nLength of path =  " + LENGTH);
        for (int[] aGrid : grid) {
            System.out.print("\n");
            for (int x = 0; x < grid.length; x++) {
                if (aGrid[x] == -1) System.out.print("#" + "\t");
                else if (aGrid[x] == -5) System.out.print("." + "\t");
                else if (aGrid[x] == 0) System.out.print("S" + "\t");
                else if (aGrid[x] == 255) System.out.print("F" + "\t");
                else System.out.print(" " + "\t");
            }
        }
    }

    /**
     * Распространение волны от кролика до морковки
     *
     * @param ax координаты кролика
     * @param ay
     * @param bx координаты морковки
     * @param by
     * @return существует ли путь?
     */
    private static boolean Wave(int ax, int ay, int bx, int by)   // поиск пути из ячейки (ax, ay) в ячейку (bx, by)
    {
        int dx[] = {1, 0, -1, 0};   // смещения, соответствующие соседям ячейки
        int dy[] = {0, 1, 0, -1};   // справа, снизу, слева и сверху
        int d, x, y, k;
        boolean stop;

        if (grid[ay][ax] == WALL || grid[by][bx] == WALL) return false;  // ячейка (ax, ay) или (bx, by) - стена

        // распространение волны
        d = 0;
        grid[ay][ax] = 0;            // стартовая ячейка помечена 0
        do {
            stop = true;               // предполагаем, что все свободные клетки уже помечены
            for (y = 0; y < H; ++y)
                for (x = 0; x < W; ++x)
                    if (grid[y][x] == d) {                        // ячейка (x, y) помечена числом
                        for (k = 0; k < 4; ++k) {                   // проходим по всем непомеченным соседям
                            int iy = y + dy[k], ix = x + dx[k];
                            if (iy >= 0 && iy < H && ix >= 0 && ix < W &&
                                    grid[iy][ix] == BLANK) {
                                stop = false;              // найдены непомеченные клетки
                                grid[iy][ix] = d + 1;      // распространяем волну
                            }
                        }
                    }
            d++;
        } while (!stop && grid[by][bx] == BLANK);

        return grid[by][bx] != BLANK;
    }
}
