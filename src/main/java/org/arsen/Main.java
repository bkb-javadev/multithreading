package org.arsen;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static Random random = new Random();
    public static Lock lock = new ReentrantLock();
    public static List<Account> accounts = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        for (long i = 1; i < 11; i++) {
            accounts.add(new Account(i, new BigDecimal(1000)));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {
            executorService.submit(Main::operation);
        }

        executorService.shutdown();

        executorService.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Итоговый баланс аккаунтов:");

        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    public static void operation() {
        lock.lock();
        Account account = accounts.get(random.nextInt(accounts.size()));
        BigDecimal amount = new BigDecimal(random.nextInt(5000));
        if (random.nextInt(2) == 0) {
            System.out.println("Пополнение счета " + account + " на сумму " + amount);
            account.deposit(amount);
        } else {
            System.out.println("Списание со счета " + account + " на сумму " + amount);
            if (account.getBalance().compareTo(amount) >= 0) {
                account.withdraw(amount);
            } else {
                System.out.println("Ошибка платежа!");
            }
        }
        System.out.println("Итоговый баланс счета " + account.getBalance() + "\n");
        lock.unlock();
    }

    public static void transfer() {
        lock.lock();

        Account account1 = accounts.get(random.nextInt(accounts.size()));
        Account account2 = accounts.get(random.nextInt(accounts.size()));

        while (account1.equals(account2)) {
            account2 = accounts.get(random.nextInt(accounts.size()));
        }

        BigDecimal amount = new BigDecimal(random.nextInt(500));

        System.out.println("Перевод с аккаунта " + account1 +
                "\nНа аккаунт " + account2 +
                "\nНа сумму " + amount);

        if (account1.getBalance().compareTo(amount) >= 0) {
            Account.transfer(account1, account2, amount);
        } else {
            System.out.println("Ошибка перевода!");
        }

        System.out.println("Итого: " +
                "\nБаланс отправителя " + account1.getBalance() +
                "\nБаланс получателя " + account2.getBalance() + "\n");

        lock.unlock();
    }

}