package com.rzzkan.fuzzydelovawardrobe.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rzzkan.fuzzydelovawardrobe.R;
import com.rzzkan.fuzzydelovawardrobe.model.FuzzySet;
import com.rzzkan.fuzzydelovawardrobe.tools.FuzzyLogic;
import com.rzzkan.fuzzydelovawardrobe.tools.SPManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SPManager spManager;
    Button btnEdit, btnDelete, btnCalculate;
    EditText etDemmand, etStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
    }

    private void initialization(){
        spManager = new SPManager(this);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnCalculate = findViewById(R.id.btnCalculate);
        etDemmand = findViewById(R.id.etDemmand);
        etStock = findViewById(R.id.etStock);

        etDemmand.addTextChangedListener(new inputListener(etDemmand));
        etStock.addTextChangedListener(new inputListener(etStock));
        btnEdit.setOnClickListener(new clickListener(btnEdit));
        btnDelete.setOnClickListener(new clickListener(btnDelete));
        btnCalculate.setOnClickListener(new clickListener(btnCalculate));
        btnCalculate.setEnabled(false);
    }

    private class inputListener implements TextWatcher {
        private EditText editText;

        public inputListener(EditText editText) {
            this.editText = editText;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (check(editText, s.toString())){
                btnCalculate.setEnabled(true);
            }else {
                btnCalculate.setEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class clickListener implements View.OnClickListener{
        private Button button;

        public clickListener(Button button) {
            this.button = button;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnEdit:
                    clearDialog();
                    break;
                case R.id.btnCalculate:
                    FuzzyLogic.setDemandSets(spManager.getSpDemmandMin(), spManager.getSpDemmandMax());
                    FuzzyLogic.setStockSets(spManager.getSpStockMin(), spManager.getSpStockMax());

                    FuzzyLogic.setProductionMin(spManager.getSpProductionMin());
                    FuzzyLogic.setProductionMax(spManager.getSpProductionMax());

                    String demand = etDemmand.getText().toString();
                    String stock = etStock.getText().toString();

                    Integer demandInt = Integer.parseInt(demand);
                    Integer stockInt = Integer.parseInt(stock);

                    setMembership(demandInt, stockInt);
                    break;
                default:
                    Toast.makeText(getApplicationContext(),"ID Not Found",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private Boolean check(EditText editText, String s){
        Boolean valid = true;
        if (s.equals("")) {
            editText.setError("Input tidak dapat kosong...");
            valid = false;
        }else {
            Integer inputInt = Integer.valueOf(s);
            if (inputInt < 0) {
                editText.setError("Input tidak dapat minus...");
                valid = false;
            }
        }
        return valid;
    }

//    private void editClick(){
//        startActivity(new Intent(MainActivity.this, ReferenceActivity.class));
//        finish();
//    }
//
//    private void deleteClick(){
//        clearDialog();
//    }
//
//    private void calculateClick(){
//        FuzzyLogic.setDemandSets(spManager.getSpDemmandMin(), spManager.getSpDemmandMax());
//        FuzzyLogic.setStockSets(spManager.getSpStockMin(), spManager.getSpStockMax());
//
//        FuzzyLogic.setProductionMin(spManager.getSpProductionMin());
//        FuzzyLogic.setProductionMax(spManager.getSpProductionMax());
//
//        String demand = etDemmand.getText().toString();
//        String stock = etStock.getText().toString();
//
//        Integer demandInt = Integer.parseInt(demand);
//        Integer stockInt = Integer.parseInt(stock);
//
//        setMembership(demandInt, stockInt);
//
//    }

    private void setMembership(Integer demand, Integer stock) {
        ArrayList<FuzzySet> demandSets = FuzzyLogic.getDemandSets();
        ArrayList<FuzzySet> stockSets = FuzzyLogic.getStockSets();

        calculateMembership(demandSets, demand);
        calculateMembership(stockSets, stock);

        ArrayList<FuzzySet> productSets = caseChecking(demandSets, stockSets);
        Double crisp = calculateCrips(productSets);

        showDialog("Jumlah masker yang harus diproduksi adalah " + crisp.intValue() + " buah");
    }

    private void calculateMembership(ArrayList<FuzzySet> fuzzySets, Integer fuzzyNum) {
        Boolean isSet;

        for (FuzzySet fuzzySet : fuzzySets) {
            if (fuzzySet.getMax() < 0)
                isSet = fuzzyNum > fuzzySet.getMin();
            else
                isSet = fuzzyNum >= fuzzySet.getMin() && fuzzyNum <= fuzzySet.getMax();

            if (isSet) {
                switch (fuzzySet.getType()) {
                    case 1:
                        fuzzySet.setMembership((double) 1);
                        break;
                    case 2:
                        fuzzySet.setMembership((double) (fuzzyNum - fuzzySet.getMin()) / (double) (fuzzySet.getMax() - fuzzySet.getMin()));
                        break;
                    case 3:
                        fuzzySet.setMembership((double) (fuzzySet.getMax() - fuzzyNum) / (double) (fuzzySet.getMax() - fuzzySet.getMin()));
                        break;
                }

//                Log.d("Membership " + fuzzySet.getName(), "Membershipnya " + fuzzySet.getMembership());
            }

            fuzzySet.setTrue(isSet);
        }
    }

    private ArrayList<FuzzySet> caseChecking(ArrayList<FuzzySet> demandSets, ArrayList<FuzzySet> stockSets) {
        ArrayList<FuzzySet> productSets = new ArrayList<>();

        for (FuzzySet demandSet : demandSets) {
            for (FuzzySet stockSet : stockSets) {
                if (demandSet.getTrue() && stockSet.getTrue()) {
                    FuzzySet productSet = null;

                    if (demandSet.getName().equals("Demand Decrease") && stockSet.getName().equals("Stock High"))
                        productSet = new FuzzySet(FuzzyLogic.getProductionMax(), FuzzyLogic.getProductionMin(), 3, "Production Decrease");
                    else if (demandSet.getName().equals("Demand Decrease") && stockSet.getName().equals("Stock Low"))
                        productSet = new FuzzySet(FuzzyLogic.getProductionMax(), FuzzyLogic.getProductionMin(), 3, "Production Decrease");
                    else if (demandSet.getName().equals("Demand Increase") && stockSet.getName().equals("Stock High"))
                        productSet = new FuzzySet(FuzzyLogic.getProductionMax(), FuzzyLogic.getProductionMin(), 2, "Production Increase");
                    else if (demandSet.getName().equals("Demand Increase") && stockSet.getName().equals("Stock Low"))
                        productSet = new FuzzySet(FuzzyLogic.getProductionMax(), FuzzyLogic.getProductionMin(), 2, "Production Increase");

                    if (productSet != null) {
                        Double membership = Math.min(demandSet.getMembership(), stockSet.getMembership());

                        productSet.setTrue(true);
                        productSet.setMembership(membership);

                    }

                    productSets.add(productSet);
                }
            }
        }

        return productSets;
    }

    private double calculateCrips(ArrayList<FuzzySet> productSets) {
        Double up = 0.0;
        Double down = 0.0;

        for (FuzzySet productSet : productSets) {
            if (productSet != null) {
                Double crisp = 0.0;

                switch (productSet.getType()) {
                    case 2:
                        crisp = (productSet.getMembership() * (productSet.getMax() - productSet.getMin())) + productSet.getMin();
                        break;
                    case 3:
                        crisp = productSet.getMax() - (productSet.getMembership() * (productSet.getMax() - productSet.getMin()));
                        break;
                }

                down += productSet.getMembership();
                up += (productSet.getMembership() * crisp);
            }
        }

        return (up / down);
    }

    public void clearDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle    ("Hapus");
        builder.setMessage("Apakah Anda Yakin untuk Menghapus Referensi ?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                spManager.saveSPBoolean(spManager.SP_IS_AVAILABLE, false);
                spManager.removeSP();
                startActivity(new Intent(MainActivity.this, ReferenceActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDialog(String text){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        TextView tvTittle = dialog.findViewById(R.id.tvTittle);
        Button btnRetry = dialog.findViewById(R.id.btnRetry);

        tvTittle.setText(text);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}