package com.rickybooks.rickybooks.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
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
import com.rickybooks.rickybooks.Retrofit.RetrofitClient;
import com.rickybooks.rickybooks.Retrofit.TextbookService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class SellFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 71;
    private ImageView chosenImage = null;
    private Bitmap chosenImageBitmap = null;
    private String chosenImageExtension = null;
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
    private TextbookService textbookService;
    private View view;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(chosenImageBitmap != null) {
            Glide.with(view).load(chosenImageBitmap).into(chosenImage);
            setChooseImageButtonText("DELETE");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = new RetrofitClient().getClient();
        textbookService = retrofit.create(TextbookService.class);
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

    public void submitButtonPressed(View view) {
        hideKeyboard(view);
        getData(view);
        textbookPostReq(view);
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

    public void textbookPostReq(final View view) {
        Call<String> call = textbookService.postTextbookForm(tokenString, userId, textbookTitle,
                textbookAuthor, textbookEdition, textbookCondition, textbookType,
                textbookCoursecode, textbookPrice);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                textbookPostRes(view, response);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Ricky", "textbookPostReq error: " + t.getMessage());
            }
        });
    }

    public void textbookPostRes(View view, Response<String> response) {
        if(response.isSuccessful()) {
            createAlert("Success!", "Your textbook has been successfully posted!");
            EditText titleField = view.findViewById(R.id.textbook_title);
            titleField.getText().clear();
            EditText authorField = view.findViewById(R.id.textbook_author);
            authorField.getText().clear();
            Spinner editionSpinner = view.findViewById(R.id.textbook_edition_spinner);
            Spinner conditionSpinner = view.findViewById(R.id.textbook_condition_spinner);
            Spinner typeSpinner = view.findViewById(R.id.textbook_type_spinner);
            editionSpinner.setSelection(0);
            conditionSpinner.setSelection(0);
            typeSpinner.setSelection(0);
            EditText coursecodeField = view.findViewById(R.id.textbook_coursecode);
            coursecodeField.getText().clear();
            EditText priceField = view.findViewById(R.id.textbook_price);
            priceField.getText().clear();

            String textbookId = response.body();
            if(imageFile != null) {
                getSignedPostUrlReq(textbookId);
            }
        }
        else {
            try {
                String errorResponse = response.errorBody().string();

                JSONObject resObj = new JSONObject(errorResponse);
                JSONObject resData = resObj.getJSONObject("data");
                StringBuilder errorMessage = new StringBuilder();
                for(int i=0; i<resData.length(); i++) {
                    String name = resData.names().getString(i);
                    String value = resData.get(name).toString();
                    value = value.replace("[\"can't be blank\"]", "Missing: ");
                    name = name.replace(name.substring(0, 9), "Textbook ");
                    name = name.substring(0, 9)
                            + name.substring(9, 10).toUpperCase()
                            + name.substring(10);
                    if(i == resData.length()-1) {
                        errorMessage.append(value).append(name);
                    }
                    else {
                        errorMessage.append(value).append(name).append("\n");
                    }
                }
                createAlert("Hmm.. you forgot something!", String.valueOf(errorMessage));
            } catch(IOException e) {
                e.printStackTrace();
            } catch(JSONException e) {
                e.printStackTrace();
            } catch(NullPointerException e) {
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        }
    }

    public void getSignedPostUrlReq(String textbookId) {
        Call<String> call = textbookService.getSignedPostUrl(tokenString, textbookId,
                chosenImageExtension);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                getSignedPostUrlRes(response);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Ricky", "getSignedPostUrlReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void getSignedPostUrlRes(Response<String> response) {
        if(response.isSuccessful()) {
            String signedUrl = response.body();
            uploadImageReq(signedUrl);
        }
        else {
            try {
                String errorMessage = response.errorBody().string();
                Log.e("Ricky", "getSignedPostUrlReq unsuccessful: " + errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                    "reach the server at the moment.\n\nPlease try again later.");
        }
    }

    public void uploadImageReq(String signedUrl) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        Call<Void> call = textbookService.putImageAws(signedUrl, reqFile);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                uploadImageRes(response);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Ricky", "uploadImage failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void uploadImageRes(Response<Void> response) {
        if(response.isSuccessful()) {
            imageFile = null;
            chosenImageBitmap = null;
            chosenImage.setImageBitmap(null);
            setChooseImageButtonText("CHOOSE");
            chosenImageExtension = null;
        }
        else {
            try {
                String errorMessage = response.errorBody().string();
                Log.e("Ricky", "uploadImageReq unsuccessful: " + errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                    "reach the server at the moment.\n\nPlease try again later.");
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
            chosenImage.setImageBitmap(null);
            chosenImageBitmap = null;
            setChooseImageButtonText("CHOOSE");
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
                chosenImageExtension = activity.getContentResolver().getType(chosenImageUri);
                chosenImageExtension = chosenImageExtension.substring(6);
                chosenImage.setImageBitmap(chosenImageBitmap);
                Glide.with(view).load(chosenImageBitmap).into(chosenImage);

                imageFile = new File(getContext().getCacheDir(), "ChosenImage");

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                chosenImageBitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream);
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

    public void createAlert(final String title, final String message) {
        final Activity activity = getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle(title);
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                alertDialog.show();
            }
        });
    }
}
