package io.github.vcvitaly.algo.design.warmup;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FibonacciHuge {

    private static final List<Long> PIZANO_START_SEQ = Arrays.asList(0L, 1L);

    private final List<BigInteger> numbers = new LinkedList<>(
            PIZANO_START_SEQ.stream().map(BigInteger::valueOf).collect(Collectors.toList())
    );

    long getFibonacciHugeNaive(long n, long m) {
        if (n <= 1)
            return n;

        long previous = 0;
        long current  = 1;

        for (long i = 0; i < n - 1; ++i) {
            long tmp_previous = previous;
            previous = current;
            current = tmp_previous + current;
        }

        return current % m;
    }

    long getFibonacciHugeFast(long n, long m) {
        int pizanoPeriodOfM = getPizanoPeriod(m);
        return calcFib(
                (int) (n % pizanoPeriodOfM)
        ).mod(BigInteger.valueOf(m)).longValue();
    }

    int getPizanoPeriod(long m) {
        if (m >= 10 && m % 10 == 0) {
            return getPizanoPeriodForMdivisibleByTen(m);
        }

        BigInteger mBigInt = BigInteger.valueOf(m);
        List<Long> remainders = new LinkedList<>(PIZANO_START_SEQ);

        for (int i = PIZANO_START_SEQ.size(); !isAPeriod(remainders); i++) {
            BigInteger fib = calcFib(i);
            if (fib.compareTo(BigInteger.ZERO) < 0) {
                throw new IllegalStateException(String.format("fib(%d)=%d and is negative", i, fib));
            }

            remainders.add(fib.mod(mBigInt).longValue());
        }

        return remainders.size() - PIZANO_START_SEQ.size();
    }

    int getPizanoPeriodForMdivisibleByTen(long m) {
        if (m < 10 || m % 10 != 0) {
            throw new IllegalArgumentException(String.format("m should be less than 10 and divisible by 10, was :%d", m));
        }

        if (m == 10) {
            return 60;
        }
        if (m == 100) {
            return 300;
        }

        return (int) (m * 1.5); // TODO is that correct for all M divisible by 10?
    }

    int getPisanoPeriodOfPrime(long m) {
        if (m <= 5) {
            throw new IllegalArgumentException(String.format("m should be less than 10 and divisible by 10, was: %d", m));
        }

        long mod = m % 5;

        if (mod == 1 || mod == 4) {
            // TODO
        }

        return 0;
    }

    private boolean isAPeriod(List<Long> list) {
        if (list.size() > PIZANO_START_SEQ.size()) {
            if (list.subList(list.size() - PIZANO_START_SEQ.size(), list.size())
                    .equals(PIZANO_START_SEQ)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPrime(int number) {
        return number > 1
                && IntStream.rangeClosed(2, (int) Math.sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }

    private BigInteger calcFib(int n) {
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }

        if (n < numbers.size()) {
            return numbers.get(n);
        }

        for (int i = numbers.size(); i <= n; i++) {
            numbers.add(numbers.get(i-1).add(numbers.get(i-2)));
        }

        return numbers.get(n);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        long m = scanner.nextLong();
        System.out.println(new FibonacciHuge().getFibonacciHugeFast(n, m));
    }
}
