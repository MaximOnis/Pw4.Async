import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ex1 {
    public static int[] generate(){
        int[] arr = new int[10];
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            arr[i] = (rand.nextInt(2));
        }
        return arr;
    }

    public static int[] add(int[] arr) {
        int[] newArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i] + 5;
        }
        return newArr;
    }
    public static BigInteger factorial(int num){
        BigInteger res = BigInteger.ONE;
        for (int i = 1; i <= num; i++){
            res = res.multiply(BigInteger.valueOf(i));
        }
        return res;
    }

    public static int sum(int[] a){
        return Arrays.stream(a).sum();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException{

        long globalStartTime = System.currentTimeMillis();
        CompletableFuture<int[]> future = CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            System.out.println("Generating array...");
            int[] res = generate();
            long endTime = System.currentTimeMillis();
            System.out.println("Generating Work Time: " + (endTime - startTime) + " ms");
            System.out.println("--------------------");
            return res;
        });

        CompletableFuture<int[]> modifiedFuture = future.thenApplyAsync(array1 -> {
            long startTime = System.currentTimeMillis();
            System.out.println("Modifying array...");
            int[] res = add(array1);
            long endTime = System.currentTimeMillis();
            System.out.println("Add Work Time: " + (endTime - startTime) + " ms");
            System.out.println("--------------------");
            return res;
        });

        CompletableFuture<BigInteger> totalFuture = future.thenCombineAsync(modifiedFuture, (arr1, arr2) -> {
            long startTime = System.currentTimeMillis();
            int totalSum = sum(arr1) + sum(arr2);
            System.out.println("Calculating result...");
            System.out.println("Total Sum: " + totalSum);
            BigInteger res = factorial(totalSum);
            long endTime = System.currentTimeMillis();
            System.out.println("Factorial Work Time: " + (endTime - startTime) + " ms");
            System.out.println("--------------------");
            return res;
        });

        long displayStartTime = System.currentTimeMillis();
        CompletableFuture<Void> display = CompletableFuture.allOf(
                future.thenAcceptAsync(array -> {System.out.println("Array: " + Arrays.toString(array));}),
                modifiedFuture.thenAcceptAsync(array -> {System.out.println("Modified Array: " + Arrays.toString(array));}),
                totalFuture.thenAcceptAsync(res -> {System.out.println("Factorial: " + res);})
        );

        display.get();
        long displayEndTime = System.currentTimeMillis();
        System.out.println("Display Work Time: " + (displayEndTime - displayStartTime) + " ms");
        System.out.println("Work Time: " + (displayEndTime- globalStartTime) + " ms");

    }
}
