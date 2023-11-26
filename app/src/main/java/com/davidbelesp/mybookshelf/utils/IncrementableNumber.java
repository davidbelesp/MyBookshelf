package com.davidbelesp.mybookshelf.utils;

public class IncrementableNumber {

    private Integer number = 0;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void incrementNumber(Integer sum){
        this.number += sum;
    }

}
