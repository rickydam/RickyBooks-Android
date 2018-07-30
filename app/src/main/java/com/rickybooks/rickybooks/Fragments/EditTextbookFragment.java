package com.rickybooks.rickybooks.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import com.rickybooks.rickybooks.Other.Alert;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.DeleteImageCall;
import com.rickybooks.rickybooks.Retrofit.DeleteImageEntryCall;
import com.rickybooks.rickybooks.Retrofit.EditTextbookCall;
import com.rickybooks.rickybooks.Retrofit.GetSignedDeleteUrlCall;
import com.rickybooks.rickybooks.Retrofit.GetSignedPutUrlCall;
import com.rickybooks.rickybooks.Retrofit.PutImageAwsCall;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class EditTextbookFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 71;
    private View view;
    private ImageView chosenImage;
    private Button chooseImageButton;
    private Bitmap chosenImageBitmap;
    private String chosenImageFileExtension;
    private File imageFile;
    private boolean chooseImageButtonPressed;
    private boolean textbookHadImage;

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(chosenImageBitmap != null) {
            Glide.with(view).load(chosenImageBitmap).into(chosenImage);
            setChooseImageButtonText("DELETE IMAGE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_textbook, container, false);
        initChooseImageButton();
        initEditionSpinner();
        initConditionSpinner();
        initTypeSpinner();
        initSaveButton();
        loadData();
        return view;
    }

    public void loadData() {
        Bundle bundle = getArguments();

        textbookHadImage = false;
        chosenImage = view.findViewById(R.id.edit_textbook_imageview);
        String imageUrl = bundle.getString("ImageUrl");
        if(!imageUrl.equals("")) {
            Glide.with(view).load(imageUrl).into(chosenImage);
            setChooseImageButtonText("DELETE IMAGE");
            textbookHadImage = true;
        }

        EditText titleField = view.findViewById(R.id.edit_textbook_title);
        titleField.setText(bundle.getString("Title"));

        EditText authorField = view.findViewById(R.id.edit_textbook_author);
        authorField.setText(bundle.getString("Author"));

        String edition = bundle.getString("Edition");
        String[] editionList = getResources().getStringArray(R.array.edition);
        int editionIndex = Arrays.asList(editionList).indexOf(edition);
        Spinner editionSpinner = view.findViewById(R.id.edit_textbook_edition_spinner);
        editionSpinner.setSelection(editionIndex);

        String condition = bundle.getString("Condition");
        String[] conditionList = getResources().getStringArray(R.array.condition);
        int conditionIndex = Arrays.asList(conditionList).indexOf(condition);
        Spinner conditionSpinner = view.findViewById(R.id.edit_textbook_condition_spinner);
        conditionSpinner.setSelection(conditionIndex);

        String type = bundle.getString("Type");
        String[] typeList = getResources().getStringArray(R.array.type);
        int typeIndex = Arrays.asList(typeList).indexOf(type);
        Spinner typeSpinner = view.findViewById(R.id.edit_textbook_type_spinner);
        typeSpinner.setSelection(typeIndex);

        EditText coursecodeField = view.findViewById(R.id.edit_textbook_coursecode);
        coursecodeField.setText(bundle.getString("Coursecode"));

        EditText priceField = view.findViewById(R.id.edit_textbook_price);
        String price = bundle.getString("Price");
        price = price.replace("$ ", "");
        priceField.setText(price);
    }

    public void initChooseImageButton() {
        chooseImageButton = view.findViewById(R.id.edit_textbook_choose_button);
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageButtonPressed();
            }
        });
    }

    public void chooseImageButtonPressed() {
        chooseImageButtonPressed = true;
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

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            setChooseImageButtonText("DELETE IMAGE");
            Uri chosenImageUri = data.getData();
            try {
                MainActivity activity = (MainActivity) getActivity();

                chosenImageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),
                        chosenImageUri);
                Glide.with(view).load(chosenImageBitmap).into(chosenImage);

                chosenImageFileExtension = activity.getContentResolver().getType(chosenImageUri);
                chosenImageFileExtension = chosenImageFileExtension.substring(6);

                imageFile = new File(getContext().getCacheDir(), "ChosenImage");

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                chosenImageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

                byte[] chosenImageBitmapData = byteArrayOutputStream.toByteArray();

                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                fileOutputStream.write(chosenImageBitmapData);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearImageData() {
        chosenImageFileExtension = null;
        imageFile = null;
        chosenImage.setImageResource(0);
        setChooseImageButtonText("CHOOSE IMAGE");
    }

    public void setChooseImageButtonText(String buttonText) {
        if(buttonText.equals("CHOOSE IMAGE")) {
            chooseImageButton.setBackgroundResource(R.drawable.button_blue);
            chooseImageButton.setText(R.string.choose_image_button_text);
        }
        if(buttonText.equals("DELETE IMAGE")) {
            chooseImageButton.setBackgroundResource(R.drawable.button_red);
            chooseImageButton.setText(R.string.delete_image_button_text);
        }
    }

    public void initSaveButton() {
        Button saveButton = view.findViewById(R.id.edit_textbook_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveButtonPressed();
                    }
                }).start();
            }
        });
    }

    public void saveButtonPressed() {
        Bundle bundle = getArguments();
        String textbookId = bundle.getString("Id");

        EditText titleField = view.findViewById(R.id.edit_textbook_title);
        String textbookTitle = titleField.getText().toString();

        EditText authorField = view.findViewById(R.id.edit_textbook_author);
        String textbookAuthor = authorField.getText().toString();

        Spinner editionSpinner = view.findViewById(R.id.edit_textbook_edition_spinner);
        String textbookEdition = editionSpinner.getSelectedItem().toString();
        if(textbookEdition.equals("")) {
            textbookEdition = "";
        }

        Spinner conditionSpinner = view.findViewById(R.id.edit_textbook_condition_spinner);
        String textbookCondition = conditionSpinner.getSelectedItem().toString();
        if(textbookCondition.equals("")) {
            textbookCondition = "";
        }

        Spinner typeSpinner = view.findViewById(R.id.edit_textbook_type_spinner);
        String textbookType = typeSpinner.getSelectedItem().toString();
        if(textbookType.equals("")) {
            textbookType = "";
        }

        EditText coursecodeField = view.findViewById(R.id.edit_textbook_coursecode);
        String textbookCoursecode = coursecodeField.getText().toString();

        EditText priceField = view.findViewById(R.id.edit_textbook_price);
        String textbookPrice = priceField.getText().toString();

        MainActivity activity = (MainActivity) getActivity();
        EditTextbookCall editTextbookCall = new EditTextbookCall(activity);
        editTextbookCall.req(textbookId, textbookTitle, textbookAuthor, textbookEdition,
                textbookCondition, textbookType, textbookCoursecode, textbookPrice);

        if(chooseImageButtonPressed) {
            // Choose Image button was pressed, there may or may not have been a change
            if(chosenImage.getDrawable() != null) {
                // An image is loaded
                DeleteImageEntryCall deleteImageEntryCall = new DeleteImageEntryCall(activity);
                if(textbookHadImage) {
                    // Textbook had an image already, remove the old one
                    deleteImageEntryCall.req(textbookId);
                }
                // Store/replace the image on AWS
                GetSignedPutUrlCall getSignedPutUrlCall = new GetSignedPutUrlCall(activity);
                getSignedPutUrlCall.req(textbookId, chosenImageFileExtension);

                String signedPutUrl = getSignedPutUrlCall.getData();
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);

                PutImageAwsCall putImageAwsCall = new PutImageAwsCall(activity);
                putImageAwsCall.req(signedPutUrl, reqFile);

                if(textbookHadImage) {
                    if(deleteImageEntryCall.isSuccessful() && getSignedPutUrlCall.isSuccessful()
                            && putImageAwsCall.isSuccessful() && editTextbookCall.isSuccessful()) {
                        alertSuccessPopAndRefresh();
                    }
                }
                else {
                    if(getSignedPutUrlCall.isSuccessful() && putImageAwsCall.isSuccessful()
                            && editTextbookCall.isSuccessful()) {
                        alertSuccessPopAndRefresh();
                    }
                }
            }
            else {
                // No image loaded
                if(textbookHadImage) {
                    // The user decided to remove the image
                    GetSignedDeleteUrlCall getSignedDeleteUrlCall = new GetSignedDeleteUrlCall(activity);
                    getSignedDeleteUrlCall.req(textbookId);

                    String signedDeleteUrl = getSignedDeleteUrlCall.getData();
                    DeleteImageCall deleteImageCall = new DeleteImageCall(activity);
                    deleteImageCall.req(signedDeleteUrl);

                    DeleteImageEntryCall deleteImageEntryCall = new DeleteImageEntryCall(activity);
                    deleteImageEntryCall.req(textbookId);

                    if(getSignedDeleteUrlCall.isSuccessful() && deleteImageCall.isSuccessful()
                            && deleteImageEntryCall.isSuccessful() && editTextbookCall.isSuccessful()) {
                        alertSuccessPopAndRefresh();
                    }
                }
            }
        }
        else {
            if(editTextbookCall.isSuccessful()) {
                alertSuccessPopAndRefresh();
            }
        }
    }

    public void alertSuccessPopAndRefresh() {
        MainActivity activity = (MainActivity) getActivity();
        Alert alert = new Alert(activity);
        alert.create("Success!", "Textbook successfully updated!");
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.popBackStack();
        Fragment profileFragment = fm.findFragmentByTag("ProfileFragment");
        if(profileFragment != null) {
            ((ProfileFragment) profileFragment).getUserTextbooks();
        }
    }

    public void initEditionSpinner() {
        Spinner editionSpinner = view.findViewById(R.id.edit_textbook_edition_spinner);

        editionSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                hideKeyboard();
                return true;
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
    }

    public void initConditionSpinner() {
        Spinner conditionSpinner = view.findViewById(R.id.edit_textbook_condition_spinner);

        conditionSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                hideKeyboard();
                return true;
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
    }

    public void initTypeSpinner() {
        Spinner typeSpinner = view.findViewById(R.id.edit_textbook_type_spinner);

        typeSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                hideKeyboard();
                return true;
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
    }

    public void hideKeyboard() {
        Context context = getActivity().getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
