package net.interkoneksi.malangtoday.app;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.interkoneksi.malangtoday.R;
import net.interkoneksi.malangtoday.util.KonfigurasiAPI;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ardhanmz on 8/18/17.
 */

public class KirimKomentar extends DialogFragment {
    EditText txnama, txemail, txisi;
    Button kirim;
    String name, email, content;
    View view;
    int post = -1;
    LinearLayout layout1;
    LinearLayout layoutProgress;
    TextInputLayout  txtemail;
    String API_Komentar = "respond/submit_comment/?post_id="+post+"&name"+name+"&content="+content;

    private void kirimKomentar() {
        name = txnama.getText().toString();
        email = txemail.getText().toString();
        content= txisi.getText().toString();
        if (KonfigurasiAPI.isConnected(getActivity())){
            layout1.setVisibility(View.GONE);
            layoutProgress.setVisibility(View.VISIBLE);
            KonfigurasiAPI.save(getActivity(), "comment_name", name);
            KonfigurasiAPI.save(getActivity(), "comment_email", email);

            KonfigurasiAPI.get(API_Komentar, null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    txisi.setText("");
                    Toast.makeText(getActivity(),getResources().getString(R.string.comment_submit_success),
                            Toast.LENGTH_LONG).show();
                    getDialog().dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.comment_status_close_error),
                            Toast.LENGTH_LONG).show();
                    getDialog().dismiss();
                }
            });
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.kirim_komentar, container, false);

        layout1 = (LinearLayout) view.findViewById(R.id.progress_submit);
        layoutProgress = (LinearLayout) view.findViewById(R.id.form);
        txnama = (EditText) view.findViewById(R.id.input_layout_name);
        txemail = (EditText) view.findViewById(R.id.input_email);
        txtemail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        txisi = (EditText) view.findViewById(R.id.input_content);
        kirim = (Button) view.findViewById(R.id.btn_submit);
        layout1.setVisibility(View.VISIBLE);
        layoutProgress.setVisibility(View.GONE);

        post = getArguments().getInt("postId");

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kirimKomentar();
            }
        });
        if (!KonfigurasiAPI.load(getActivity(), "comment_nama").equals("")){
            txnama.setText(KonfigurasiAPI.load(getActivity(), "comment_name"));
        }

        if (!KonfigurasiAPI.load(getActivity(), "comment_email").equals("")){
            txemail.setText(KonfigurasiAPI.load(getActivity(), "comment_email"));
        }

        txemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    if (!KonfigurasiAPI.isValid(txemail.getText().toString())){
                        txtemail.setError("Email Salah Format");
                    }else {
                        txtemail.setErrorEnabled(false);
                    }
            }
        });
        return view;
    }



}
