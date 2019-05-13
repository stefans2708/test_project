package com.android.stefan.testproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WifiActivity extends AppCompatActivity implements NetworkAdapter.OnNetworkClickListener {

    private WifiManager wifiManager;

    private RecyclerView recyclerViewNetworks;
    private Switch switchWifi;
    private Button btnScan;
    private Button btnDisconnect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        initViews();

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
    }

    private void initViews() {
        recyclerViewNetworks = findViewById(R.id.recycler_wifi_scan_result);
        recyclerViewNetworks.setHasFixedSize(true);
        recyclerViewNetworks.setLayoutManager(new LinearLayoutManager(this));

        btnScan = findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiManager.startScan();
            }
        });

        btnDisconnect = findViewById(R.id.btn_disconnect);
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiManager.disconnect();
            }
        });

        switchWifi = findViewById(R.id.switch_wifi);
        switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                btnScan.setEnabled(isChecked);
                if (wifiManager.isWifiEnabled() ^ isChecked) {
                    wifiManager.setWifiEnabled(isChecked);
                }
                if (!isChecked && recyclerViewNetworks.getAdapter() != null) {
                    ((NetworkAdapter) recyclerViewNetworks.getAdapter()).removeAll();
                }
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
                    recyclerViewNetworks.setAdapter(new NetworkAdapter(getNetworkInfo(scanResults), bssid, WifiActivity.this));
                }
            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLED: {
                        switchWifi.setChecked(true);
                        btnScan.setEnabled(true);
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLED: {
                        btnScan.setEnabled(false);
                        switchWifi.setChecked(false);
                        break;
                    }
                }
            }

        }
    };


    private List<NetworkInfo> getNetworkInfo(List<ScanResult> scanResults) {
        List<NetworkInfo> result = new ArrayList<>();
        for (ScanResult scanResult : scanResults) {
            result.add(new NetworkInfo(scanResult.SSID,
                    scanResult.BSSID,
                    WifiManager.calculateSignalLevel(scanResult.level, 5),
                    isNetworkSecured(scanResult.capabilities)));
        }

        Collections.sort(result, new Comparator<NetworkInfo>() {
            @Override
            public int compare(NetworkInfo networkInfo1, NetworkInfo networkInfo2) {
                return Integer.compare(networkInfo1.getLevel(), networkInfo2.getLevel());
            }
        });
        Collections.reverse(result);

        return result;
    }

    private boolean isNetworkSecured(String capabilities) {
        return capabilities.contains("WEP") || capabilities.contains("PSK") || capabilities.contains("EAP");
    }

    @Override
    public void onNetworkClick(final NetworkInfo network) {
        if (network.isSecured()) {
            EnterPasswordDialog dialog = EnterPasswordDialog.newInstance();
            dialog.setListener(new EnterPasswordDialog.OnButtonClickListener() {
                @Override
                public void onConnectClick(String password) {
                    network.setPassword(password);
                    connectToWifiNetwork(network);
                }
            });
            dialog.show(getSupportFragmentManager(), EnterPasswordDialog.class.getSimpleName());
        } else {
            connectToWifiNetwork(network);
        }
    }

    private void connectToWifiNetwork(NetworkInfo network) {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        WifiConfiguration config = new WifiConfiguration();
        config.SSID = String.format("\"%s\"", network.getSsid());
        if (network.isSecured()) {
            config.preSharedKey = String.format("\"%s\"", network.getPassword());
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }

        int netId = wifiManager.addNetwork(config);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

}
