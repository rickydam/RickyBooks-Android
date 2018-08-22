package com.rickybooks.rickybooks.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.GetSignedPutUrlCall;
import com.rickybooks.rickybooks.Retrofit.PostTextbookCall;
import com.rickybooks.rickybooks.Retrofit.PutImageAwsCall;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class SellFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 71;
    private ImageView chosenImage = null;
    private Bitmap chosenImageBitmap = null;
    private String chosenImageFileExtension = null;
    private Button chooseImageButton = null;
    private File imageFile = null;
    private String tokenString = "";
    private String userId = "";
    private String textbookTitle = "";
    private String textbookAuthor = "";
    private String textbookEdition = "";
    private String textbookCondition = "";
    private String textbookType = "";
    private String textbookCoursecode = "";
    private String textbookPrice = "";
    private View view;

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(chosenImageBitmap != null) {
            Glide.with(view).load(chosenImageBitmap).into(chosenImage);
            setChooseImageButtonText("DELETE");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sell, container, false);

        chosenImage = view.findViewById(R.id.chosen_image);

        final Spinner editionSpinner = view.findViewById(R.id.textbook_edition_spinner);
        editionSpinner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        editionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if(selectedText != null) {
                    if(!text.equals("Edition")) {
                        selectedText.setTextColor(Color.BLACK);
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> editionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.edition, R.layout.spinner_item);
        editionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editionSpinner.setAdapter(editionAdapter);

        final Spinner conditionSpinner = view.findViewById(R.id.textbook_condition_spinner);
        conditionSpinner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if(selectedText != null) {
                    if(!text.equals("Condition")) {
                        selectedText.setTextColor(Color.BLACK);
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.condition, R.layout.spinner_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);

        EditText title = view.findViewById(R.id.textbook_title);
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        final Spinner typeSpinner = view.findViewById(R.id.textbook_type_spinner);
        typeSpinner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if(selectedText != null) {
                    if(!text.equals("Type")) {
                        selectedText.setTextColor(Color.BLACK);
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.type, R.layout.spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        EditText author = view.findViewById(R.id.textbook_author);
        author.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        EditText course = view.findViewById(R.id.textbook_coursecode);
        course.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        EditText price = view.findViewById(R.id.textbook_price);
        price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        Button submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonPressed(view);
            }
        });

        chooseImageButton = view.findViewById(R.id.choose_textbook_image_button);
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageButtonPressed();
            }
        });

        return view;
    }

    public void submitButtonPressed(final View view) {
        hideKeyboard(view);
        getData(view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                postTextbook(view);
            }
        }).start();
    }

    public void getData(View view) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                "com.rickybooks.rickybooks", Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        tokenString = "Token token=" + token;
        userId = sharedPref.getString("user_id", null);

        EditText titleField = view.findViewById(R.id.textbook_title);
        textbookTitle = titleField.getText().toString();

        EditText authorField = view.findViewById(R.id.textbook_author);
        textbookAuthor = authorField.getText().toString();

        Spinner editionSpinner = view.findViewById(R.id.textbook_edition_spinner);
        textbookEdition = editionSpinner.getSelectedItem().toString();
        if(textbookEdition.equals("Edition")) {
            textbookEdition = "";
        }

        Spinner conditionSpinner = view.findViewById(R.id.textbook_condition_spinner);
        textbookCondition = conditionSpinner.getSelectedItem().toString();
        if(textbookCondition.equals("Condition")) {
            textbookCondition = "";
        }

        Spinner typeSpinner = view.findViewById(R.id.textbook_type_spinner);
        textbookType = typeSpinner.getSelectedItem().toString();
        if(textbookType.equals("Type")) {
            textbookType = "";
        }

        EditText coursecodeField = view.findViewById(R.id.textbook_coursecode);
        textbookCoursecode = coursecodeField.getText().toString();

        EditText priceField = view.findViewById(R.id.textbook_price);
        textbookPrice = priceField.getText().toString();
    }

    public void postTextbook(View view) {
        MainActivity activity = (MainActivity) getActivity();

        PostTextbookCall postTextbookCall = new PostTextbookCall(activity, view);
        postTextbookCall.req(tokenString, userId, textbookTitle, textbookAuthor, textbookEdition,
                textbookCondition, textbookType, textbookCoursecode, textbookPrice);

        if(imageFile != null) {
            String textbookId = postTextbookCall.getData();
            getSignedPutUrl(textbookId, chosenImageFileExtension);
        }
    }

    public void getSignedPutUrl(String textbookId, String chosenImageFileExtension) {
        MainActivity activity = (MainActivity) getActivity();

        GetSignedPutUrlCall getSignedPutUrlCall = new GetSignedPutUrlCall(activity);
        getSignedPutUrlCall.req(textbookId, chosenImageFileExtension);

        String signedPutUrl = getSignedPutUrlCall.getData();
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        putImageAws(signedPutUrl, reqFile);
    }

    public void putImageAws(String signedPutUrl, RequestBody reqFile) {
        MainActivity activity = (MainActivity) getActivity();

        PutImageAwsCall putImageAwsCall = new PutImageAwsCall(activity);
        putImageAwsCall.req(signedPutUrl, reqFile);

        if(putImageAwsCall.isSuccessful()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clearImageData();
                }
            });
        }
    }

    public void chooseImageButtonPressed() {
        if(chooseImageButton.getText().equals("CHOOSE IMAGE")) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                    PICK_IMAGE_REQUEST);
        }
        if(chooseImageButton.getText().equals("DELETE IMAGE")) {
            clearImageData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            setChooseImageButtonText("DELETE");
            Uri chosenImageUri = data.getData();
            try {
                MainActivity activity = (MainActivity) getActivity();
                chosenImageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),
                        chosenImageUri);
                chosenImageFileExtension = activity.getContentResolver().getType(chosenImageUri);
                chosenImageFileExtension = chosenImageFileExtension.substring(6);
                Glide.with(view).load(chosenImageBitmap).into(chosenImage);

                imageFile = new File(getContext().getCacheDir(), "ChosenImage");

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                chosenImageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                byte[] chosenImageBitmapData = byteArrayOutputStream.toByteArray();

                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                fileOutputStream.write(chosenImageBitmapData);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearImageData() {
        chosenImageBitmap = null;
        chosenImageFileExtension = null;
        imageFile = null;
        chosenImage.setImageResource(0);
        setChooseImageButtonText("CHOOSE");
    }

    public void setChooseImageButtonText(String buttonText) {
        if(buttonText.equals("CHOOSE")) {
            chooseImageButton.setBackgroundResource(R.drawable.button_blue);
            chooseImageButton.setText(R.string.choose_image_button_text);
        }
        if(buttonText.equals("DELETE")) {
            chooseImageButton.setBackgroundResource(R.drawable.button_red);
            chooseImageButton.setText(R.string.delete_image_button_text);
        }
    }

    public void hideKeyboard(View view) {
        Context context = getActivity().getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
