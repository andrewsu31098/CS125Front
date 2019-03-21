package avsu.uci.edu.cs125front;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ExerciseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        final Button exerciseButton1 = findViewById(R.id.ExerciseButton1);
        final Button exerciseButton2 = findViewById(R.id.ExerciseButton2);
        final Button exerciseButton3 = findViewById(R.id.ExerciseButton3);
        final Button exerciseButton4 = findViewById(R.id.ExerciseButton4);
        final Button exerciseButton5 = findViewById(R.id.ExerciseButton5);

        final TextView assetGuid1 = findViewById(R.id.guid1textView);
        final TextView assetGuid2 = findViewById(R.id.guid2textView);
        final TextView assetGuid3 = findViewById(R.id.guid3textView);
        final TextView assetGuid4 = findViewById(R.id.guid4textView);
        final TextView assetGuid5 = findViewById(R.id.guid5textView);


        RequestQueue queue = Volley.newRequestQueue(this);
        String getURL ="http://pluto.calit2.uci.edu:8084/v1/activities?lat=33.6295&lon=-117.8684";

        /// Retrieving Server Activity Information and applying on buttons/textviews

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, getURL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        try {
                            Log.d("Response", response.getJSONObject(4).getJSONArray("assetTopics")
                                    .getJSONObject(0).getJSONObject("topic").getString("topicName"));

                            String ex1 = response.getJSONObject(0).getJSONArray("assetTopics")
                                    .getJSONObject(0).getJSONObject("topic").getString("topicName");
                            String ex2 = response.getJSONObject(1).getJSONArray("assetTopics")
                                    .getJSONObject(0).getJSONObject("topic").getString("topicName");
                            String ex3 = response.getJSONObject(2).getJSONArray("assetTopics")
                                    .getJSONObject(0).getJSONObject("topic").getString("topicName");
                            String ex4 = response.getJSONObject(3).getJSONArray("assetTopics")
                                    .getJSONObject(0).getJSONObject("topic").getString("topicName");
                            String ex5 = response.getJSONObject(4).getJSONArray("assetTopics")
                                    .getJSONObject(0).getJSONObject("topic").getString("topicName");

                            assetGuid1.setText(response.getJSONObject(0).getString("assetGuid"));
                            assetGuid2.setText(response.getJSONObject(1).getString("assetGuid"));
                            assetGuid3.setText(response.getJSONObject(2).getString("assetGuid"));
                            assetGuid4.setText(response.getJSONObject(3).getString("assetGuid"));
                            assetGuid5.setText(response.getJSONObject(4).getString("assetGuid"));

                            Log.d("assetGuid",assetGuid1.getText().toString());

                            exerciseButton1.setText(ex1);
                            exerciseButton2.setText(ex2);
                            exerciseButton3.setText(ex3);
                            exerciseButton4.setText(ex4);
                            exerciseButton5.setText(ex5);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(getRequest);


        // Making each Exercise Button commit a POST request to update the server
        exerciseButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postActivityData(assetGuid1.getText().toString());
            }
        });
        exerciseButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postActivityData(assetGuid2.getText().toString());
            }
        });
        exerciseButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postActivityData(assetGuid3.getText().toString());
            }
        });
        exerciseButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postActivityData(assetGuid4.getText().toString());
            }
        });
        exerciseButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postActivityData(assetGuid5.getText().toString());
            }
        });

    }

    private void postActivityData(String assetGuid) {

        String activityURL = "http://pluto.calit2.uci.edu:8084/v1/activities/";
        String postUserDataURL = activityURL + assetGuid;

        StringRequest postActivityDataRequest = new StringRequest(Request.Method.POST, postUserDataURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("POST ACTIVITY VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FAILED POST", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ExerciseListActivity.this);
        queue.add(postActivityDataRequest);

        Log.d("POST SENT THIS",assetGuid);

    }


}
