package avsu.uci.edu.cs125front;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://pluto.calit2.uci.edu:8084/v1/activities?lat=33.6295&lon=-117.8684";

        // Request a string response from the provided URL.
//        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
////                        exampleTextView.setText("Response is: "+ response.substring(0,500));
//                        Log.d("It worked!",response.substring(0,100));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Exercise Get Request","That didn't work!");
//            }
//        });

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
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

    }

}
