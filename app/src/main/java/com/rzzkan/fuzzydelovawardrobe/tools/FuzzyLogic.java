package com.rzzkan.fuzzydelovawardrobe.tools;

import android.app.Application;


import com.rzzkan.fuzzydelovawardrobe.model.FuzzySet;

import java.util.ArrayList;

public class FuzzyLogic extends Application {

    private static ArrayList<FuzzySet> demandSets;
    private static ArrayList<FuzzySet> stockSets;
    private static Integer productionMax;
    private static Integer productionMin;

    public static ArrayList<FuzzySet> getDemandSets() {
        return demandSets;
    }

    public static void setDemandSets(ArrayList<FuzzySet> demandSets) {
        FuzzyLogic.demandSets = demandSets;
    }

    public static ArrayList<FuzzySet> getStockSets() {
        return stockSets;
    }

    public static void setStockSets(ArrayList<FuzzySet> stockSets) {
        FuzzyLogic.stockSets = stockSets;
    }

    public static Integer getProductionMax() {
        return productionMax;
    }

    public static void setProductionMax(Integer productionMax) {
        FuzzyLogic.productionMax = productionMax;
    }

    public static Integer getProductionMin() {
        return productionMin;
    }

    public static void setProductionMin(Integer productionMin) {
        FuzzyLogic.productionMin = productionMin;
    }

    public static void setDemandSets(Integer min, Integer max) {
        FuzzySet fuzzySet;

        demandSets = new ArrayList<>();

        fuzzySet = new FuzzySet(min, 0, 1, "Demand Decrease");
        demandSets.add(fuzzySet);

        fuzzySet = new FuzzySet(max, min, 3, "Demand Decrease");
        demandSets.add(fuzzySet);

        fuzzySet = new FuzzySet(max, min, 2, "Demand Increase");
        demandSets.add(fuzzySet);

        fuzzySet = new FuzzySet(-1, max, 1, "Demand Increase");
        demandSets.add(fuzzySet);
    }

    public static void setStockSets(Integer min, Integer max) {
        FuzzySet fuzzySet;

        stockSets = new ArrayList<>();

        fuzzySet = new FuzzySet(min, 0, 1, "Stock Low");
        stockSets.add(fuzzySet);

        fuzzySet = new FuzzySet(max, min, 3, "Stock Low");
        stockSets.add(fuzzySet);

        fuzzySet = new FuzzySet(max, min, 2, "Stock High");
        stockSets.add(fuzzySet);

        fuzzySet = new FuzzySet(-1, max, 1, "Stock High");
        stockSets.add(fuzzySet);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
