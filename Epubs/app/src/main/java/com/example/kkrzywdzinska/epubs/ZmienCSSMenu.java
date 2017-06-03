package com.example.kkrzywdzinska.epubs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;


public class ZmienCSSMenu extends DialogFragment {


    protected AlertDialog.Builder builder;
    protected Spinner spinColor;
    protected Spinner spinBack;
    protected Spinner spinFontSize;
    protected Button defaultButton;
    protected Spinner spinLeft;
    protected Spinner spinRight;
    protected int colInt, backInt,sizeInt, marginLInt, marginRInt;
    protected MainActivity a;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();


        a = (MainActivity)getActivity();
        View view = inflater.inflate(R.layout.change_css, null);

        final SharedPreferences preferences = a.getPreferences(Context.MODE_PRIVATE);

        spinColor = (Spinner) view.findViewById(R.id.spinnerColor);
        colInt = preferences.getInt("spinColorValue", 0);
        spinColor.setSelection(colInt);

        spinBack = (Spinner) view.findViewById(R.id.spinnerBack);
        backInt = preferences.getInt("spinBackValue", 0);
        spinBack.setSelection(backInt);

        spinFontSize = (Spinner) view.findViewById(R.id.spinnerFS);
        sizeInt = preferences.getInt("spinFontSizeValue", 0);
        spinFontSize.setSelection(sizeInt);

        spinLeft = (Spinner) view.findViewById(R.id.spinnerLeft);
        marginLInt = preferences.getInt("spinLeftValue", 0);
        spinLeft.setSelection(marginLInt);

        spinRight = (Spinner) view.findViewById(R.id.spinnerRight);
        marginRInt = preferences.getInt("spinRightValue", 0);
        spinRight.setSelection(marginRInt);

        defaultButton = (Button) view.findViewById(R.id.buttonDefault);

        builder.setTitle("Style");
        builder.setView(view);

        spinColor
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        colInt = (int) id;
                        switch ((int) id) {
                            case 0:
                                a.setColor(getString(R.string.black_rgb));
                                break;
                            case 1:
                                a.setColor(getString(R.string.red_rgb));
                                break;
                            case 2:
                                a.setColor(getString(R.string.green_rgb));
                                break;
                            case 3:
                                a.setColor(getString(R.string.white_rgb));
                                break;
                            case 4:
                                a.setColor(getString(R.string.blue_rgb));
                                break;
                            case 5:
                                a.setBackColor(getString(R.string.yellow_rgb));
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

        spinBack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                backInt = (int) id;
                switch ((int) id) {
                    case 0:
                        a.setBackColor(getString(R.string.white_rgb));
                        break;
                    case 1:
                        a.setBackColor(getString(R.string.red_rgb));
                        break;
                    case 2:
                        a.setBackColor(getString(R.string.green_rgb));
                        break;
                    case 3:
                        a.setBackColor(getString(R.string.black_rgb));
                        break;
                    case 4:
                        a.setBackColor(getString(R.string.blue_rgb));
                        break;
                    case 5:
                        a.setBackColor(getString(R.string.yellow_rgb));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        spinFontSize
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        sizeInt = (int) id;
                        switch ((int) id) {
                            case 0:
                                a.setFontSize("100");
                                break;
                            case 1:
                                a.setFontSize("125");
                                break;
                            case 2:
                                a.setFontSize("150");
                                break;
                            case 3:
                                a.setFontSize("175");
                                break;
                            case 4:
                                a.setFontSize("200");
                                break;
                            case 5:
                                a.setFontSize("90");
                                break;
                            case 6:
                                a.setFontSize("75");
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });


        spinLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                marginLInt = (int) id;
                switch ((int) id) {
                    case 0:
                        a.setMarginLeft("0");
                        break;
                    case 1:
                        a.setMarginLeft("5");
                        break;
                    case 2:
                        a.setMarginLeft("10");
                        break;
                    case 3:
                        a.setMarginLeft("15");
                        break;
                    case 4:
                        a.setMarginLeft("20");
                        break;
                    case 5:
                        a.setMarginLeft("25");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spinRight
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        marginRInt = (int) id;
                        switch ((int) id) {
                            case 0:
                                a.setMarginRight("0");
                                break;
                            case 1:
                                a.setMarginRight("5");
                                break;
                            case 2:
                                a.setMarginRight("10");
                                break;
                            case 3:
                                a.setMarginRight("15");
                                break;
                            case 4:
                                a.setMarginRight("20");
                                break;
                            case 5:
                                a.setMarginRight("25");
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

        defaultButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                a.setColor("");
                a.setBackColor("");
                a.setMarginLeft("");
                a.setMarginRight("");
                a.setFontSize("");
                a.setCSS();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("spinColorValue", 0);
                editor.putInt("spinBackValue", 0);
                editor.putInt("spinLeftValue", 0);
                editor.putInt("spinRightValue", 0);
                editor.putInt("spinFontSizeValue", 0);
                editor.commit();

                dismiss();
            }
        });

        builder.setPositiveButton(getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        a.setCSS();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("spinColorValue", colInt);
                        editor.putInt("spinBackValue", backInt);
                        editor.putInt("spinLeftValue", marginLInt);
                        editor.putInt("spinRightValue", marginRInt);
                        editor.putInt("spinFontSizeValue", sizeInt);
                        editor.commit();
                    }
                });
        builder.setNegativeButton(getString(R.string.Cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create();
    }
}