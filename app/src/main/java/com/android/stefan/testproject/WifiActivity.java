package com.android.stefan.testproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class WifiActivity extends AppCompatActivity {

    private WifiManager wifiManager;

    private RecyclerView recyclerViewNetworks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        initViews();

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
    }

    private void initViews() {
        recyclerViewNetworks = findViewById(R.id.recycler_wifi_scan_result);
        recyclerViewNetworks.setHasFixedSize(true);
        recyclerViewNetworks.setLayoutManager(new LinearLayoutManager(this));

        Button btnScan = findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiManager.startScan();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiScanReceiver);
    }

    private BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(WifiActivity.this, "onReceive", Toast.LENGTH_SHORT).show();
            if (intent.getAction() == null) return;

            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                if (intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)) {
                    List<ScanResult> scanResults = wifiManager.getScanResults();
                    Toast.makeText(WifiActivity.this, "result size: " + scanResults.size(), Toast.LENGTH_SHORT).show();

                    String bssid = wifiManager.getConnectionInfo().getBSSID();
                    recyclerViewNetworks.setAdapter(new NetworkAdapter(scanResults, bssid));
                }
            }
        }
    };

}
