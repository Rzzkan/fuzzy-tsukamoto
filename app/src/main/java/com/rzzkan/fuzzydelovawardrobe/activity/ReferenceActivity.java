package com.rzzkan.fuzzydelovawardrobe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rzzkan.fuzzydelovawardrobe.R;
import com.rzzkan.fuzzydelovawardrobe.tools.SPManager;

public class ReferenceActivity extends AppCompatActivity {
    SPManager spManager;
    Button btnSave;
    EditText etDemmandMin, etDemmandMax, etStockMin, etStockMax, etProductionMin, etProductionMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);
        initialization();
    }

    private void initialization(){
        spManager = new SPManager(this);
        if (spManager.getSpIsAvailable()){
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        btnSave = findViewById(R.id.btnSave);
        etDemmandMin = findViewById(R.id.etDemmandMin);
        etDemmandMax = findViewById(R.id.etDemmandMax);
        etStockMin = findViewById(R.id.etStockMin);
        etStockMax = findViewById(R.id.etStockMax);
        etProductionMin = findViewById(R.id.etProductionMin);
        etProductionMax = findViewById(R.id.etProductionMax);

        etDemmandMin.addTextChangedListener(new inputListener(etDemmandMin));
        etDemmandMax.addTextChangedListener(new inputListener(etDemmandMax));
        etStockMin.addTextChangedListener(new inputListener(etStockMin));
        etStockMax.addTextChangedListener(new inputListener(etStockMax));
        etProductionMin.addTextChangedListener(new inputListener(etProductionMin));
        etProductionMax.addTextChangedListener(new inputListener(etProductionMax));

        btnSave.setOnClickListener(new clickListener(btnSave));
        btnSave.setEnabled(false);
    }

    private class inputListener implements TextWatcher{
        EditText editText;

        public inputListener(EditText editText) {
            this.editText = editText;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (check(editText, s.toString())){
                btnSave.setEnabled(true);
            }else {
                btnSave.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class clickListener implements View.OnClickListener{
        Button button;

        public clickListener(Button button) {
            this.button = button;
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnSave:
                    spManager.saveSPInt(SPManager.SP_DEMMAND_MIN,Integer.valueOf(etDemmandMin.getText().toString()));
                    spManager.saveSPInt(SPManager.SP_DEMMAND_MAX,Integer.valueOf(etDemmandMax.getText().toString()));
                    spManager.saveSPInt(SPManager.SP_STOCK_MIN,Integer.valueOf(etStockMin.getText().toString()));
                    spManager.saveSPInt(SPManager.SP_STOCK_MAX,Integer.valueOf(etStockMax.getText().toString()));
                    spManager.saveSPInt(SPManager.SP_PRODUCTION_MIN,Integer.valueOf(etProductionMin.getText().toString()));
                    spManager.saveSPInt(SPManager.SP_PRODUCTION_MAX,Integer.valueOf(etProductionMax.getText().toString()));
                    spManager.saveSPBoolean(SPManager.SP_IS_AVAILABLE,true);
                    Toast.makeText(getApplicationContext(),"Berhasil Menyimpan Referensi",Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(ReferenceActivity.this, MainActivity.class));
                            finish();
                        }
                    },1000);
                    break;
                default:
                    Toast.makeText(getApplicationContext(),"ID Tidak ditemukan",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        finish();
    }


}