package com.android.stefan.testproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EnterPasswordDialog extends DialogFragment {

    private OnButtonClickListener listener;

    public static EnterPasswordDialog newInstance() {
        return new EnterPasswordDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_enter_password, container, false);
        setListeners(view);
        return view;
    }

    private void setListeners(View view) {
        final EditText editPassword = view.findViewById(R.id.edit_enter_password);
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        view.findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onConnectClick(editPassword.getText().toString());
                }
                dismiss();
            }
        });

    }

    public void setListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnButtonClickListener {
        void onConnectClick(String password);
    }
}
