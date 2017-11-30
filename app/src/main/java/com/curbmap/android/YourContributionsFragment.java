package com.curbmap.android;

import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class YourContributionsFragment extends Fragment {
    View myView;
    private String TAG = "YourContributions";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.your_contributions, container, false);

        AppDatabase db = Room.databaseBuilder(myView.getContext(),
                AppDatabase.class,
                "restrictions").allowMainThreadQueries().build();
        RestrictionDao restrictionDao = db.getRestrictionDao();
        Log.d(TAG, restrictionDao.getAll().toString());
        List<Restriction> listOfRestrictions = restrictionDao.getAll();

        //todo: sort by latest entries first
        String allCards = "";
        for (Restriction restriction : listOfRestrictions) {
            allCards += restriction.getCard() + "\n\n";
        }

        TextView yourContributions = (TextView) myView.findViewById(R.id.yourContributionsText);
        yourContributions.setText(allCards);

        return myView;
    }
}
