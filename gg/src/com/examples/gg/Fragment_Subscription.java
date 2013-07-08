package com.examples.gg;

import android.support.v4.app.Fragment;

public class Fragment_Subscription extends Fragment_Base{
	@Override
	protected Fragment listSwitcher(){
		return new Videolist_Subscription();

	}

}
