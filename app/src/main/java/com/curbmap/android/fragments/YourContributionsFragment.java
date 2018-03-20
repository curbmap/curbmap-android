package com.curbmap.android.fragments;

import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.curbmap.android.models.db.RestrictionAppDatabase;
import com.curbmap.android.MyAdapter;
import com.curbmap.android.R;
import com.curbmap.android.models.db.Restriction;
import com.curbmap.android.models.db.RestrictionDao;

import java.util.List;

public class YourContributionsFragment extends Fragment {


    View myView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String TAG = "YourContributions";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_your_contributions, container, false);


        ImageView menu_icon = (ImageView) myView.findViewById(R.id.menu_icon);
        menu_icon.setOnClickListener(new View.OnClickListener() {
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


        RestrictionAppDatabase db = Room.databaseBuilder(myView.getContext(),
                RestrictionAppDatabase.class,
                "restrictions")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        final RestrictionDao restrictionDao = db.getRestrictionDao();
        List<Restriction> listOfRestrictions = restrictionDao.getAll();

        //todo: sort by latest entries first
        String allCards = "";
        for (Restriction restriction : listOfRestrictions) {
            allCards += restriction.getCard() + "\n\n";
        }
        if (allCards.equals("")) {
            allCards = getString(R.string.info_no_restrictions);
        }


        mRecyclerView = myView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(myView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        String[] myDataset = new String[listOfRestrictions.size()];
        int index = 0;
        for (Restriction restriction : listOfRestrictions) {
            myDataset[index] = restriction.getCard();
            index++;
        }

        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);


        final TextView yourContributions = myView.findViewById(R.id.yourContributionsText);
        //yourContributions.setText(allCards);

        Button clearContributionsButton = myView.findViewById(R.id.clearContributionsButton);
        clearContributionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(myView.getContext())
                        .setTitle(getString(R.string.clear_contributions))
                        .setMessage(getString(R.string.confirm_clear_contributions))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                restrictionDao.deleteAll();
                                yourContributions.setText("");

                                Toast.makeText(myView.getContext(),
                                        getString(R.string.success_clear_contributions),
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });


        Button emailContributionsButton = myView.findViewById(R.id.emailContributionsButton);
        final String finalAllCards = allCards;
        emailContributionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{getString(R.string.developer_email)});
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.email_subject));
                i.putExtra(Intent.EXTRA_TEXT, finalAllCards);
                try {
                    startActivity(Intent.createChooser(i,
                            getString(R.string.prompt_send_email)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(myView.getContext(),
                            getString(R.string.error_no_email_clients),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        return myView;
    }
}
