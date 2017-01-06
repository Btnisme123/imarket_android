package com.example.framgia.imarketandroid.ui.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.framgia.imarketandroid.R;
import com.example.framgia.imarketandroid.data.model.Session;
import com.example.framgia.imarketandroid.data.model.UserModel;
import com.example.framgia.imarketandroid.ui.activity.LoginActivity;
import com.example.framgia.imarketandroid.util.Constants;
import com.example.framgia.imarketandroid.util.Flog;
import com.example.framgia.imarketandroid.util.HttpRequest;
import com.example.framgia.imarketandroid.util.SharedPreferencesUtil;

/**
 * Created by toannguyen201194 on 22/07/2016.
 */
public class SignUpFragment extends android.support.v4.app.Fragment {
    private final int MIN_PASS = 6;
    private final int MIN_PHONE_09 = 10;
    private final int MIN_PHONE_01 = 11;
    private final String HEAD_NUMBER_01 = "01";
    private final String HEAD_NUMBER_09 = "09";
    private View mView;
    private TextInputLayout mInputFullName, mInputPassword, mInputConfirmPass, mInputmail;
    private EditText mEditFullName, mEditMail, mEditPassword, mEditPasswordConfirm;
    private Button mButtonRegister;
    private ProgressDialog mProgressDialog;
    private TextInputLayout mInputPhoneNumber;
    private EditText mEditPhoneNumber;

    public SignUpFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_signup, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView();
    }

    private void findView() {
        mInputFullName = (TextInputLayout) mView.findViewById(R.id.input_text_fullname);
        mInputPassword = (TextInputLayout) mView.findViewById(R.id.input_text_password);
        mInputConfirmPass = (TextInputLayout) mView.findViewById(R.id.input_text_confirmpassword);
        mInputmail = (TextInputLayout) mView.findViewById(R.id.input_text_email);
        mEditFullName = (EditText) mView.findViewById(R.id.edit_text_fullname);
        mEditMail = (EditText) mView.findViewById(R.id.edit_text_email);
        mEditPassword = (EditText) mView.findViewById(R.id.edit_text_password);
        mEditPasswordConfirm = (EditText) mView.findViewById(R.id.edit_text_confirmpassword);
        mButtonRegister = (Button) mView.findViewById(R.id.button_register);
        mEditFullName.addTextChangedListener(new MyTextWatcher(mEditFullName));
        mEditMail.addTextChangedListener(new MyTextWatcher(mEditMail));
        mEditPasswordConfirm.addTextChangedListener(new MyTextWatcher(mEditPasswordConfirm));
        mEditPassword.addTextChangedListener(new MyTextWatcher(mEditPassword));
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        mInputPhoneNumber = (TextInputLayout) mView.findViewById(R.id.input_text_phone_number);
        mEditPhoneNumber = (EditText) mView.findViewById(R.id.edit_text_phone_number);
    }

    private void submitForm() {
        if (!validate(mEditFullName, mInputFullName, R.string.err_msg_name)) {
            return;
        }
        if (!validate(mEditMail, mInputmail, R.string.err_msg_email)) {
            return;
        }
        if (!validate(mEditPassword, mInputPassword, R.string.err_msg_password)) {
            return;
        }
        if (!validate(mEditPhoneNumber, mInputPhoneNumber, R.string.enter_phonenumber)) {
            return;
        }
        if (!checkPasswordConfirm()) {
            return;
        } else {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.progressdialog));
            mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
            Session user = new Session(
                mEditFullName.getText().toString(),
                mEditMail.getText().toString(),
                mEditPassword.getText().toString(),
                mEditPasswordConfirm.getText().toString(),
                mEditPhoneNumber.getText().toString()
            );
            final UserModel signupModel = new UserModel(user);
            HttpRequest.getInstance(getActivity().getBaseContext()).register(signupModel);
            HttpRequest.getInstance(getActivity().getBaseContext())
                .setOnLoadDataListener(new HttpRequest.OnLoadDataListener() {
                    @Override
                    public void onLoadDataSuccess(Object object) {
                        UserModel userModel = (UserModel) object;
                        mProgressDialog.dismiss();
                        if (userModel != null && userModel.getSession() != null) {
                            LoginActivity.mViewPagerLogin.setCurrentItem(0);
                        } else {
                            Flog.toast(getContext(), userModel.getmErrors().toString());
                        }
                    }

                    @Override
                    public void onLoadDataFailure(String message) {
                        mProgressDialog.dismiss();
                    }
                });
        }
    }

    private boolean validate(EditText editText, TextInputLayout inputLayout, int error) {
        if (editText.getText().toString().trim().isEmpty()) {
            inputLayout.setError(getString(error));
            requestFocus(editText);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean checkPasswordConfirm() {
        String password = mEditPassword.getText().toString().trim();
        if (!mEditPasswordConfirm.getText().toString().trim().equals(password)) {
            mInputConfirmPass.setError(getString(R.string.err_msg_password_confirm));
            requestFocus(mEditPasswordConfirm);
            return false;
        } else if (mEditPasswordConfirm.getText().toString().trim().isEmpty()) {
            mInputConfirmPass.setError(getString(R.string.err_msg_password_confirm));
            return false;
        } else if (password.length() < MIN_PASS) {
            mInputPassword.setError(getString(R.string.mat_khau_toi_thieu));
            requestFocus(mInputPassword);
            return false;
        } else {
            mInputConfirmPass.setErrorEnabled(false);
        }
        String phonenumber = mEditPhoneNumber.getText().toString().trim();
        String dauCach = " ";
        boolean checkDauCach = phonenumber.contains(dauCach);
        if (checkDauCach) {
            mInputPhoneNumber.setError(getString(R.string.sai_sdt));
            Flog.toast(getContext(), getString(R.string.sai_sdt));
            requestFocus(mInputPhoneNumber);
            return false;
        }
        String soDau = mEditPhoneNumber.getText().toString().substring(0,2);
        if (soDau.equals(HEAD_NUMBER_09) && phonenumber.length() != MIN_PHONE_09) {
            mInputPhoneNumber.setError(getString(R.string.sai_sdt));
            Flog.toast(getContext(), getString(R.string.sai_sdt));
            requestFocus(mInputPhoneNumber);
            return false;
        } else if (soDau.equals(HEAD_NUMBER_01) && phonenumber.length() != MIN_PHONE_01) {
            mInputPhoneNumber.setError(getString(R.string.sai_sdt));
            Flog.toast(getContext(), getString(R.string.sai_sdt));
            requestFocus(mInputPhoneNumber);
            return false;
        } else if (!soDau.equals(HEAD_NUMBER_01) && !soDau.equals(HEAD_NUMBER_09)) {
            mInputPhoneNumber.setError(getString(R.string.sai_sdt));
            Flog.toast(getContext(), getString(R.string.sai_sdt));
            requestFocus(mInputPhoneNumber);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_text_fullname:
                    validate(mEditFullName, mInputFullName, R.string.err_msg_name);
                    break;
                case R.id.input_text_email:
                    validate(mEditMail, mInputmail, R.string.err_msg_email);
                    break;
                case R.id.input_text_password:
                    validate(mEditPassword, mInputPassword, R.string.err_msg_password);
                    break;
                case R.id.input_text_confirmpassword:
                    checkPasswordConfirm();
                    break;
            }
        }
    }
}
