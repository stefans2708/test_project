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
    private OnNetworkClickListener listener;

    public NetworkAdapter(List<NetworkInfo> networks, String bssid, OnNetworkClickListener listener) {
        this.networks = networks;
        this.bssidOfConnectedNetwork = bssid;
        this.listener = listener;
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
        networkViewHolder.securityInfo.setText(network.isSecured() ? "Secured" : "Open");
        networkViewHolder.itemView.setBackgroundColor(network.getBssid().equals(bssidOfConnectedNetwork) ?
                networkViewHolder.itemView.getContext().getResources().getColor(R.color.colorCurrentNetworkItem) : Color.WHITE);

        String imgName = networkViewHolder.itemView.getContext().getString(R.string.wifi_image_name, network.getLevel());
        networkViewHolder.imgSignalStrength.setImageResource(
                networkViewHolder.itemView.getContext().getResources().getIdentifier(imgName, "drawable", networkViewHolder.itemView.getContext().getPackageName())
        );
    }

    @Override
    public int getItemCount() {
        return networks == null ? 0 : networks.size();
    }

    public void removeAll() {
        networks.clear();
        notifyDataSetChanged();
    }

    class NetworkViewHolder extends RecyclerView.ViewHolder {

        TextView txtNetworkName;
        ImageView imgSignalStrength;
        TextView securityInfo;

        NetworkViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNetworkName = itemView.findViewById(R.id.txt_network_name);
            imgSignalStrength = itemView.findViewById(R.id.img_network_signal);
            securityInfo = itemView.findViewById(R.id.txt_field2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onNetworkClick(networks.get(getAdapterPosition()));
                    }
                }
            });
        }

    }

    public interface OnNetworkClickListener {
        void onNetworkClick(NetworkInfo network);
    }
}
