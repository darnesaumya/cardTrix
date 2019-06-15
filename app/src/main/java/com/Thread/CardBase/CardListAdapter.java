package com.Thread.CardBase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.Thread.CardBase.R;

import java.util.List;

public class CardListAdapter extends ArrayAdapter<CardData> {
    List<CardData> cardDataList;
    Context context;
    int resource;

    public CardListAdapter(Context context, int resource, List<CardData> cardDataList) {
        super(context, resource, cardDataList);
        this.context = context;
        this.resource = resource;
        this.cardDataList = cardDataList;
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource,null,false);
        TextView textView = view.findViewById(R.id.tf);
        CardData cardData = cardDataList.get(position);
        textView.setText(cardData.getTitle());
        return view;
    }
}
