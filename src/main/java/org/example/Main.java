package org.example;


import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        int sizePol = 1000;

        Runnable task = () -> {
            for (int i = 0, j = 0; i < sizePol; i++, j++) {
                String route = generateRoute("RLRFR", 100);
                char r = 'R';
                int key = Math.toIntExact(route.chars().filter(n -> n == r).count());
                synchronized (sizeToFreq) {
                    addMap(key);
                    System.out.println("err: " + j);
                    sizeToFreq.notify();
                }
            }
        };
        Thread thread1 = new Thread(task);
        thread1.start();

        Thread thread = new Thread(() -> {
            int i = 0;
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    System.out.printf("Текущий лидер: %s (встретилось %s раз) %s\n", Collections.max(sizeToFreq
                            .entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(), Collections.max(sizeToFreq.values()), i++);
                }
            }
        });
        thread.start();

        thread1.join();

        thread.interrupt();

        System.out.printf("Самое частое количество повторений: %s (встретилось %s раз)\n", Collections.max(sizeToFreq
                .entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(), Collections.max(sizeToFreq.values()));
        System.out.println("Другие размеры:");
        sizeToFreq.forEach((key, value) -> System.out.printf(" - %s (%s раз)\n", key, value));
    }

    protected static void addMap(Integer key) {
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
