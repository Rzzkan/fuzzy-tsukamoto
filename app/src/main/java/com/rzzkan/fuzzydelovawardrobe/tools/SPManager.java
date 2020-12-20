package com.rzzkan.fuzzydelovawardrobe.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class SPManager {
    public static final String SP_APP = "spApp";
    public static final String SP_DEMMAND_MIN = "spDemmandMin";
    public static final String SP_DEMMAND_MAX = "spDemmandMax";
    public static final String SP_STOCK_MIN = "spStockMin";
    public static final String SP_STOCK_MAX = "spStockMax";
    public static final String SP_PRODUCTION_MIN = "spProductionMin";
    public static final String SP_PRODUCTION_MAX = "spProductionMax";
    public static final String SP_IS_AVAILABLE = "spAvailable";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SPManager(Context context){
        sp = context.getSharedPreferences(SP_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.apply();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.apply();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.apply();
    }

    public void removeSP(){
        spEditor.remove(SP_DEMMAND_MIN);
        spEditor.remove(SP_DEMMAND_MAX);
        spEditor.remove(SP_STOCK_MIN);
        spEditor.remove(SP_STOCK_MAX);
        spEditor.remove(SP_PRODUCTION_MIN);
        spEditor.remove(SP_PRODUCTION_MAX);
        spEditor.apply();
    }

    public int getSpDemmandMin(){
        return sp.getInt(SP_DEMMAND_MIN, 0);
    }
    public int getSpDemmandMax(){
        return sp.getInt(SP_DEMMAND_MAX, 0);
    }
    public int getSpStockMin(){
        return sp.getInt(SP_STOCK_MIN, 0);
    }
    public int getSpStockMax(){
        return sp.getInt(SP_STOCK_MAX, 0);
    }
    public int getSpProductionMin(){
        return sp.getInt(SP_PRODUCTION_MIN, 0);
    }
    public int getSpProductionMax(){
        return sp.getInt(SP_PRODUCTION_MAX, 0);
    }

    public Boolean getSpIsAvailable(){
        return sp.getBoolean(SP_IS_AVAILABLE, false);
    }

}
