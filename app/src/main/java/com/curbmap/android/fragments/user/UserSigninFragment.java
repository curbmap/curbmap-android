package com.curbmap.android.fragments.user;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.curbmap.android.CurbmapRestService;
import com.curbmap.android.R;
import com.curbmap.android.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserSigninFragment extends Fragment {
    View myView;
    String TAG = "UserSigninFragment";

    //todo:handle logic for showing signinorup or user screen here

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_user_signin, container, false);

        ImageView menu_icon = (ImageView) myView.findViewById(R.id.menu_icon);

        menu_icon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DrawerLayout drawer = (DrawerLayout)
                                getActivity()
                                        .getWindow()
                                        .getDecorView()
                                        .findViewById(R.id.drawer_layout);
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
        );

        Button signInButton = myView.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View buttonView) {
                        //sign in
                        View view = (View) buttonView.getParent();
                        EditText usernameEditText = view.findViewById(R.id.usernameField);
                        String username = usernameEditText.getText().toString();
                        EditText passwordEditText = view.findViewById(R.id.passwordField);
                        String password = passwordEditText.getText().toString();
                        signIn(username, password);
                    }
                });

        return myView;
    }

    /**
     * posts the signin info onto server
     * @param username
     * @param password
     */
    public void signIn(String username, String password) {
        final String BASE_URL = getString(R.string.BASE_URL_API);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        CurbmapRestService service = retrofit.create(CurbmapRestService.class);
        Call<User> results = service.doLoginPOST(
                username,
                password);


        results.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, getString(R.string.success_signin));
                    Toast.makeText(getContext(), "signed in!", Toast.LENGTH_LONG).show();

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , new UserProfileFragment())
                            .commit();

                } else {
                    Log.d(TAG, getString(R.string.issue_signin));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, getString(R.string.fail_signin));
                t.printStackTrace();
            }
        });
    }
}
