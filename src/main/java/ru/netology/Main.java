package ru.netology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class Main {
    private static final int MAX_INCOME = 1_000;
    private static final int PURCHASES_NUMBER = 10;
    private static final int SHOPS_COUNT = 3;
    private static final int TIMEOUT_TO_WAIT_SUM_IN_SEC = 1;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws InterruptedException {
        final Random random = new Random();
        final List<int[]> shopsIncome = new ArrayList<>();
        for (int i = 0; i < SHOPS_COUNT; i++) {
            shopsIncome.add(generateIncomeArray(random, (i + 1)));
        }

        final LongAdder incomeAdder = new LongAdder();
        final ExecutorService executorService = Executors.newFixedThreadPool(SHOPS_COUNT);
        for (int i = 0; i < SHOPS_COUNT; i++) {
            final int[] shopIncomeArr = shopsIncome.get(i);
            executorService.submit(() -> {
                Arrays.stream(shopIncomeArr).forEach(incomeAdder::add);
            });
        }

        executorService.awaitTermination(TIMEOUT_TO_WAIT_SUM_IN_SEC, TimeUnit.SECONDS);
        System.out.println("Суммарная выручка: " + incomeAdder.sum());
        executorService.shutdown();
    }

    private static int[] generateIncomeArray(Random random, int shopNumber) {
        final int[] resultIncomeArray = new int[PURCHASES_NUMBER];
        for (int i = 0; i < PURCHASES_NUMBER; i++) {
            resultIncomeArray[i] = random.nextInt(MAX_INCOME) + 1;
        }
        System.out.println("Покупки магазина " + shopNumber);
        System.out.println(Arrays.toString(resultIncomeArray));
        return resultIncomeArray;
    }
}
