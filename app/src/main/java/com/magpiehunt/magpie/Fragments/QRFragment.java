package com.magpiehunt.magpie.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;
import com.magpiehunt.magpie.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.app.Activity.RESULT_OK;


/**
 * This fragment is used for scanning a QR code to verify that the user is at the correct location
 * if the previous fragment was the Landmark fragment then the result of the scan (upon qr code being found) is returned to that fragment
 * if this fragment was selected from the nav bar it currently just prints it to the screen. obviously this will need a bit of wiring work.
 */
//TODO handle denied permissions better
//TODO result of qr scan could be handled better
public class QRFragment extends Fragment implements ZXingScannerView.ResultHandler{
    private boolean status = false, isChildOfLandmark = false;
    private Button scanButton;
    private ZXingScannerView scanner;
    private OnFragmentInteractionListener mListener;

    public QRFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static QRFragment newInstance() {
        QRFragment fragment = new QRFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    public static QRFragment newInstance(boolean childOfLandmark){//alternate constructor use when opened from LandmarkFrag
        QRFragment thisFrag = new QRFragment();
        thisFrag.childOfLandmark();
        return thisFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
        toolbar.setTitle("QR Scanner");
        //scanButton = view.findViewById(R.id.scan_button);
        scanner = view.findViewById(R.id.qr_scanner);
        if(checkQRPermission()) {
            scanner.setResultHandler(this);
            scanner.startCamera();
        }
        else{
            Toast.makeText(getContext(), "Camera permissions required to use QR functionality.", Toast.LENGTH_SHORT).show();
            //go back to previous window?
        }
        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
        scanner.stopCamera();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(scanner != null){
            scanner.startCamera();
        }
    }

    public void childOfLandmark(){
        isChildOfLandmark = true;
    }

    public boolean checkQRPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PackageManager.PERMISSION_GRANTED);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //handle result from qr scanner see top of file for a description of what this is currently doing.
    @Override
    public void handleResult(Result result) {
        if(isChildOfLandmark){
            Intent i = new Intent(getContext(), QRFragment.class);
            i.putExtra("qrresult", result.getText());
            getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, i);
            getFragmentManager().popBackStack();
        }
        else{
            Toast.makeText(getContext(), "Found: " + result.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
