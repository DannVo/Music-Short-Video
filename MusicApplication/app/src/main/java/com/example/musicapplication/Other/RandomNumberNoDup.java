package com.example.musicapplication.Other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class RandomNumberNoDup implements Iterator<Integer> {

    private List<Integer> numbers = new ArrayList<>();

    public RandomNumberNoDup(int n) {
        for (int i = 1; i <= n; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
    }

    @Override
    public boolean hasNext() {
        return !numbers.isEmpty();
    }

    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return numbers.remove(0);
    }
}
