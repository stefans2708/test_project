package com.android.stefan.testproject;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.NetworkViewHolder> {

    private List<ScanResult> scanResults;
    private String bssidOfConnectedNetwork;

    public NetworkAdapter(List<ScanResult> scanResults, String bssid) {
        this.scanResults = scanResults;
        this.bssidOfConnectedNetwork = bssid;
    }

    @NonNull
    @Override
    public NetworkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NetworkViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_network, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkViewHolder networkViewHolder, int i) {
        ScanResult scanResult = scanResults.get(i);
        networkViewHolder.name.setText(scanResult.SSID);
        networkViewHolder.f1.setText(String.valueOf(scanResult.level));
        networkViewHolder.f2.setText(scanResult.BSSID);
        networkViewHolder.itemView.setBackgroundColor(scanResult.BSSID.equals(bssidOfConnectedNetwork) ?
                networkViewHolder.itemView.getContext().getResources().getColor(R.color.colorCurrentNetworkItem) : Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return scanResults == null ? 0 : scanResults.size();
    }

    class NetworkViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView f1;
        TextView f2;

        NetworkViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_network_name);
            f1 = itemView.findViewById(R.id.txt_field1);
            f2 = itemView.findViewById(R.id.txt_field2);
        }
    }
}
