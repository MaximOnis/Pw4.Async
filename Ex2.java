import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;


public class Ex2 {
    public static int[] generate(){
        int[] arr = new int[20];
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            arr[i] = (rand.nextInt(5));
        }
        return arr;
    }
    public static int getMin(int[] arr){
        int[] newArr = new int[19];
        for (int i = 0; i < arr.length - 1; i++) {
            newArr[i] = arr[i] + arr[i+1];
        }
        System.out.println("New Array: " + Arrays.toString(newArr));
        return Arrays.stream(newArr).min().orElseThrow();
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

        CompletableFuture<Integer> totalFuture = future.thenApplyAsync(array -> {
            long startTime = System.currentTimeMillis();
            System.out.println("Calculating min...");
            int res = getMin(array);
            long endTime = System.currentTimeMillis();
            System.out.println("Calculating Work Time: " + (endTime - startTime) + " ms");
            System.out.println("--------------------");
            return res;
        });

        long displayStartTime = System.currentTimeMillis();
        CompletableFuture<Void> display = CompletableFuture.allOf(
                future.thenAcceptAsync(array -> {System.out.println("Start Array: " + Arrays.toString(array));}),
                totalFuture.thenAcceptAsync(res -> {System.out.println("Min number: " + res);})
        );

        display.get();
        long displayEndTime = System.currentTimeMillis();
        System.out.println("Display Work Time: " + (displayEndTime - displayStartTime) + " ms");
        System.out.println("Work Time: " + (displayEndTime- globalStartTime) + " ms");

    }
}
