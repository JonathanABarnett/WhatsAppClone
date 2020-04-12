package com.alaythiaproductions.whatsappclone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    private View groupFragmentView;
    private ListView groupListView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> groups = new ArrayList<>();

    private DatabaseReference groupRef;

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupFragmentView =  inflater.inflate(R.layout.fragment_groups, container, false);

        groupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        intializeFields();

        retrieveAndDisplayGroups();

        return groupFragmentView;
    }

    private void intializeFields() {
        groupListView = groupFragmentView.findViewById(R.id.groups_list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, groups);
        groupListView.setAdapter(arrayAdapter);
    }

    private void retrieveAndDisplayGroups() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> tempGroups = new HashSet<>();

                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    tempGroups.add(((DataSnapshot) iterator.next()).getKey());

                }

                groups.clear();
                groups.addAll(tempGroups);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
