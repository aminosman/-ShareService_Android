package com.amin.socialemail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;

public class MyAdapter extends BaseAdapter {

    public List<Card> cards = Collections.emptyList();
   private MainActivity parent;

	private static AQuery aq;

    // the context is needed to inflate views in getView()
    public MyAdapter(MainActivity parent) {
    	this.parent=parent;

    	aq = new AQuery(parent);
        cards = new ArrayList<Card>();


    }

    public void updateCards(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }
    public void updateCards(Card newCard) {
        this.cards.add(newCard);
        notifyDataSetChanged();
    }
    @Override
    public int getViewTypeCount() {
      return ViewType.values().length;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to BananaPhone thanks to Java return type covariance
    @Override
    public Card getItem(int position) {
        return cards.get(position);
    }
    @Override
    public int getItemViewType(int position) {
        return cards.get(position).getViewType();
    }
    // getItemId() is often useless, I think this should be the default
    // implementation in BaseAdapter
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Card currentCard  = getItem(position);
          	
   	if(cards.indexOf(currentCard) == MiddleMan.position-1){
		MiddleMan.feedMe();
	}


        return cards.get(position).getView(convertView, parent, aq, position);
    }
	


}
