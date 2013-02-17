package com.swap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SellSummaryFragment extends Fragment {
	
	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
		
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.sell_summary_fragment, container, false);

        
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        fm.beginTransaction();
        Fragment fragTwo = new SellSummaryListFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(SellSummaryListFragment.ARG_SECTION_NUMBER, getArguments().getInt(ARG_SECTION_NUMBER));
        arguments.putString("userid", getArguments().getString("userid"));
        fragTwo.setArguments(arguments);
        ft.add(R.id.frag_container, fragTwo);
        ft.commit();
        
		if (isFeaturedUser()) {
			TextView txt = (TextView) layout.findViewById(R.id.txtMessage);
			txt.setVisibility(View.GONE);

			Button btn = (Button) layout.findViewById(R.id.btnSubscribe);
			btn.setVisibility(View.GONE);
		}

        return layout;
    }

	private boolean isFeaturedUser() {
		// TODO: Connect to server and check if user is featured.
		return false;
	}

	
}
