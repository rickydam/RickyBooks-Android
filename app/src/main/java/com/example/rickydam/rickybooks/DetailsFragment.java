package com.example.rickydam.rickybooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        Button button = view.findViewById(R.id.details_send_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageButtonPressed();
            }
        });

        TextView detailsTitle = view.findViewById(R.id.details_title);
        TextView detailsAuthor = view.findViewById(R.id.details_author);
        TextView detailsEdition = view.findViewById(R.id.details_edition);
        TextView detailsCondition = view.findViewById(R.id.details_condition);
        TextView detailsType = view.findViewById(R.id.details_type);
        TextView detailsCoursecode = view.findViewById(R.id.details_coursecode);
        TextView detailsPrice = view.findViewById(R.id.details_price);
        TextView detailsSellerName = view.findViewById(R.id.details_seller);

        Bundle bundle = getArguments();

        detailsTitle.setText((String) bundle.get("Title"));
        detailsAuthor.setText((String) bundle.get("Author"));
        detailsEdition.setText((String) bundle.get("Edition"));
        detailsCondition.setText((String) bundle.get("Condition"));
        detailsType.setText((String) bundle.get("Type"));
        detailsCoursecode.setText((String) bundle.get("Coursecode"));
        detailsPrice.setText((String) bundle.get("Price"));
        detailsSellerName.setText((String) bundle.get("SellerName"));

        ImageView detailsImage = view.findViewById(R.id.details_image);
        Glide.with(this).load(R.drawable.placeholder_img).into(detailsImage);

        return view;
    }

    public void messageButtonPressed() {
        MainActivity activity = (MainActivity) getActivity();
        String token = activity.getToken();
        if(token != null) {
            createSendDialog();
        }
        else {
            activity.replaceFragment("AccountFragment");
        }
    }

    public void createSendDialog() {
        Bundle bundle = getArguments();
        String sellerName = (String) bundle.get("SellerName");
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                "com.rickydam.RickyBooks", Context.MODE_PRIVATE);
        String currentPersonName = sharedPref.getString("name", null);

        if(!sellerName.equals(currentPersonName)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context,
                    R.style.AppTheme_AlertDialogTheme);

            final EditText messageInput = new EditText(context);
            messageInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            messageInput.setSingleLine(false);
            messageInput.setHint("Type a message...");
            messageInput.setBackgroundResource(R.drawable.border);
            messageInput.setPadding(20, 15, 20, 15);
            messageInput.setHintTextColor(getResources().getColor(R.color.lightGray));
            messageInput.setMinWidth(1000);
            messageInput.setMaxHeight(500);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(60, 10, 60, 0);
            messageInput.setLayoutParams(layoutParams);

            LinearLayout parentLayout = new LinearLayout(context);
            parentLayout.addView(messageInput);

            alertDialog.setTitle("Message " + sellerName);
            alertDialog.setView(parentLayout);
            alertDialog.setCancelable(false);

            alertDialog.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String message = String.valueOf(messageInput.getText());
                    conversationReq(message);
                    dialog.dismiss();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
        else {
            createAlert("This is your textbook!", "Don't send yourself messages...");
        }
    }

    public void conversationReq(final String message) {
        final Context context = getActivity();
        String url = "http://rickybooks.herokuapp.com/conversations";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String conversation_id = obj.getString("conversation_id");
                    messageReq(message, conversation_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = ((MainActivity) context).getToken();
                headers.put("Authorization", "Token token=" + token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                SharedPreferences sharedPref = getActivity().getSharedPreferences(
                        "com.rickydam.RickyBooks", Context.MODE_PRIVATE);
                String userId = sharedPref.getString("user_id", null);
                params.put("sender_id", userId);

                Bundle bundle = getArguments();
                String sellerId = (String) bundle.get("SellerId");
                params.put("recipient_id", sellerId);

                String textbookId = (String) bundle.get("Id");
                params.put("textbook_id", textbookId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    public void messageReq(final String message, String conversation_id) {
        final Context context = getActivity();
        String url = "http://rickybooks.herokuapp.com/conversations/" + conversation_id + "/messages";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                createAlert("Success!", "Message has been sent successfully.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                createAlert("Seems like you forgot something...", "That's right! " +
                        "The message!");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = ((MainActivity) context).getToken();
                headers.put("Authorization", "Token token=" + token);

                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("body", message);

                SharedPreferences sharedPref = context.getSharedPreferences("com.rickydam.RickyBooks",
                        Context.MODE_PRIVATE);
                String user_id = sharedPref.getString("user_id", null);
                params.put("user_id", user_id);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }

    public void createAlert(String title, String message) {
        Activity activity = getActivity();
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
}
