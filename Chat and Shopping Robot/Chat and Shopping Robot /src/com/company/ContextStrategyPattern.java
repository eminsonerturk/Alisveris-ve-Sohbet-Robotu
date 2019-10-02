package com.company;

public class ContextStrategyPattern {


    private StrategyDeseniDosyadanOkuPuanlaInterface strategy;

    public ContextStrategyPattern(StrategyDeseniDosyadanOkuPuanlaInterface strategy){
        this.strategy = strategy;
    }

    public float executeStrategy(String arananKelime){
        return strategy.doOperation(arananKelime);
    }

}
