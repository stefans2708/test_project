package com.android.stefan.testproject;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.NetworkViewHolder> {

    private List<NetworkInfo> networks;
    private String bssidOfConnectedNetwork;

    public NetworkAdapter(List<NetworkInfo> networks, String bssid) {
        this.networks = networks;
        this.bssidOfConnectedNetwork = bssid;
    }

    @NonNull
    @Override
    public NetworkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NetworkViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_network, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkViewHolder networkViewHolder, int i) {
        NetworkInfo network = networks.get(i);
        networkViewHolder.txtNetworkName.setText(network.getSsid());
        networkViewHolder.f2.setText(network.getBssid());
        networkViewHolder.itemView.setBackgroundColor(network.getBssid().equals(bssidOfConnectedNetwork) ?
                networkViewHolder.itemView.getContext().getResources().getColor(R.color.colorCurrentNetworkItem) : Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return networks == null ? 0 : networks.size();
    }

    class NetworkViewHolder extends RecyclerView.ViewHolder {

        TextView txtNetworkName;
        ImageView imfSignalStrength;
        TextView f2;

        NetworkViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNetworkName = itemView.findViewById(R.id.txt_network_name);
            imfSignalStrength = itemView.findViewById(R.id.img_network_signal);
            f2 = itemView.findViewById(R.id.txt_field2);
        }
    }
}
