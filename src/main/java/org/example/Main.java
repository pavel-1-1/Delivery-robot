package org.example;

import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        int sizePol = 1000;

        Thread[] threads = new Thread[sizePol];

        for (int i = 0; i < sizePol; i++) {
            Runnable task = () -> {
                String route = generateRoute("RLRFR", 100);
                char r = 'R';
                int key = Math.toIntExact(route.chars().filter(n -> n == r).count());
                addMap(key);
            };
            threads[i] = new Thread(task);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.printf("Самое частое количество повторений: %s (встретилось %s раз)\n", Collections.max(sizeToFreq
                .entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(), Collections.max(sizeToFreq.values()));
        System.out.println("Другие размеры:");
        sizeToFreq.forEach((key, value) -> System.out.printf(" - %s (%s раз)\n", key, value));
    }

    protected static synchronized void addMap(Integer key) {
        int i = 1;
        if (!sizeToFreq.containsKey(key)) {
            sizeToFreq.put(key, i);
        } else {
            int j = sizeToFreq.get(key);
            j++;
            sizeToFreq.put(key, j);
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
