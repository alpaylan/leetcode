
/// Pow (x, n)

// Implement pow(x, n), which calculates x raised to the power n (i.e., xn).

// Example 1:

// Input: x = 2.00000, n = 10
// Output: 1024.00000
// Example 2:

// Input: x = 2.10000, n = 3
// Output: 9.26100
// Example 3:

// Input: x = 2.00000, n = -2
// Output: 0.25000
// Explanation: 2-2 = 1/22 = 1/4 = 0.25

// Constraints:

// -100.0 < x < 100.0
// -2^31 <= n <= 2^31-1
// n is an integer.
// Either x is not zero or n > 0.
// -10^4 <= xn <= 10^4

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class pow {
    public interface PowInterface {
        double pow(double x, int n);
    }

    public static boolean doubleEquals(double a, double b) {
        if (a == Double.POSITIVE_INFINITY && b == Double.POSITIVE_INFINITY) {
            return true;
        }
        return Math.abs(a - b) < 1e-4;
    }

    public static boolean powCorrectV1(PowInterface pow) {
        try {
            // generate a random double between -1.5 and 1.5
            double x = Math.random() * 3 - 1.5;

            // generate a random integer between -2^20 and 2^20
            int n = (int) (Math.random() * 2 * Math.pow(2, 20) - Math.pow(2, 20));

            // calculate the result using Math.pow
            double result = Math.pow(x, n);

            // calculate the result using the pow function
            double powResult = pow.pow(x, n);

            // System.out.println("x: " + x + " n: " + n + " result: " + result + " powResult: " + powResult);
            // compare the results
            return doubleEquals(result, powResult);
        } catch (StackOverflowError e) {
            return false;
        }
    }

    public static boolean powCorrectV2(PowInterface pow) {
        try {
            // generate a random double between -1.5 and 1.5
            double x = Math.random() * 3 - 1.5;

            // generate a random integer between -2^20 and 2^20
            int n = (int) (Math.random() * 2 * Math.pow(2, 20) - Math.pow(2, 20));

            // calculate the result using the pow function
            double powResult = pow.pow(x, n);

            // calculate the result for n + 1 using the pow function
            double powResultPlusOne = n < 0 ? pow.pow(x, n - 1) : pow.pow(x, n + 1);
            // System.out.println("x: " + x + " n: " + n + " powResult: " + powResult + " powResultPlusOne: " + powResultPlusOne);
            // compare the results
            return doubleEquals(powResult * x, powResultPlusOne);
        } catch (StackOverflowError e) {
            return false;
        }
    }

    public static class Result {
        double x;
        int n;
        boolean success;
        double time;
        String error;

        public Result(double x, int n, boolean success, double time, String error) {
            this.x = x;
            this.n = n;
            this.success = success;
            this.time = time;
            this.error = error;
        }
    }

    public static void powCorrectV3(PowInterface pow, String vname) {
        // Result Model
        // x: number, n: number, success: boolean, error: string 
        List<Result> results = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            // generate a random double between -1.5 and 1.5
            double x = Math.random() * 3 - 1.5;

            // generate a random integer between -2^20 and 2^20
            int n = (int) (Math.random() * 2 * Math.pow(2, 20) - Math.pow(2, 20));

            try {
                // calculate the result using the pow function
                double t1 = System.nanoTime();
                double powResult = pow.pow(x, n);
                double t2 = System.nanoTime();
                // calculate the result for n + 1 using the pow function
                double powResultPlusOne = n < 0 ? pow.pow(x, n - 1) : pow.pow(x, n + 1);
                // System.out.println("x: " + x + " n: " + n + " powResult: " + powResult + " powResultPlusOne: " + powResultPlusOne);
                // compare the results
                boolean result = doubleEquals(powResult * x, powResultPlusOne);

                if (result) {
                    results.add(new Result(x, n, result, t2 - t1, ""));
                } else {
                    results.add(new Result(x, n, result, t2 - t1, "powResult * x(" + powResult * x + ") != powResultPlusOne(" + powResultPlusOne + ")"));
                }

            } catch (StackOverflowError e) {
                results.add(new Result(0, 0, false, 0, "StackOverflowError"));
            }
        }

        // open results file
        try {
            FileWriter myWriter = new FileWriter("data/" + vname + "_results.json");
            myWriter.write("[\n");
            for (Result result : results.subList(0, results.size() - 1)) {
                myWriter.write("{\"x\": " + result.x + ", \"n\": " + result.n + ", \"success\": " + result.success + ",\"time\":" + result.time + ", \"error\": \"" + result.error + "\"},\n");
            }

            Result lastResult = results.get(results.size() - 1);
            myWriter.write("{\"x\": " + lastResult.x + ", \"n\": " + lastResult.n + ", \"success\": " + lastResult.success + ",\"time\":" + lastResult.time + ", \"error\": \"" + lastResult.error + "\"}\n");

            myWriter.write("]");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // False solution
    public static PowInterface powV1 = new PowInterface() {
        public double pow(double x, int n) {
            double result = 1;
            for (int i = 0; i < n; i++) {
                result *= x;
            }
            return result;
        }
    };

    // Correct but slow solution(iterative)
    public static PowInterface powV2 = new PowInterface() {
        public double pow(double x, int n) {
            if (n < 0) {
                x = 1 / x;
                n = -n;
            }
            double result = 1;
            for (int i = 0; i < n; i++) {
                result *= x;
            }
            return result;
        }
    };

    // Correct but slow solution(recursive)
    public static PowInterface powV3 = new PowInterface() {
        public double pow(double x, int n) {
            if (n == 0) {
                return 1;
            }

            if (n < 0) {
                x = 1 / x;
                n = -n;
            }

            return x * pow(x, n - 1);
        }
    };

    // Correct solution
    public static PowInterface powV4 = new PowInterface() {
        public double pow(double x, int n) {
            if (n == 0) {
                return 1;
            }

            if (n == -2147483648) {
                return (1 / x) * pow(x, -2147483647);
            }

            if (n < 0) {
                x = 1 / x;
                n = -n;
            }
            return n % 2 == 0 ? pow(x * x, n / 2) : x * pow(x * x, n / 2);
        }
    };

    public static void main(String[] args) {
        powCorrectV3(powV1, "v1");
        powCorrectV3(powV2, "v2");
        powCorrectV3(powV3, "v3");
        powCorrectV3(powV4, "v4");
    }

}