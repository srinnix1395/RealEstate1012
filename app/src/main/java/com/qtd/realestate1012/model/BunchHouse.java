package com.qtd.realestate1012.model;

/**
 * Created by Dell on 8/7/2016.
 */
public class BunchHouse {
    private CompactHouse compactHouse1;
    private CompactHouse compactHouse2;
    private CompactHouse compactHouse3;
    private CompactHouse compactHouse4;
    private CompactHouse compactHouse5;

    public BunchHouse(CompactHouse compactHouse1, CompactHouse compactHouse2, CompactHouse compactHouse3, CompactHouse compactHouse4, CompactHouse compactHouse5) {
        this.compactHouse1 = compactHouse1;
        this.compactHouse2 = compactHouse2;
        this.compactHouse3 = compactHouse3;
        this.compactHouse4 = compactHouse4;
        this.compactHouse5 = compactHouse5;
    }

    public CompactHouse getCompactHouse1() {
        return compactHouse1;
    }

    public CompactHouse getCompactHouse2() {
        return compactHouse2;
    }

    public CompactHouse getCompactHouse3() {
        return compactHouse3;
    }

    public CompactHouse getCompactHouse4() {
        return compactHouse4;
    }

    public CompactHouse getCompactHouse5() {
        return compactHouse5;
    }
}
