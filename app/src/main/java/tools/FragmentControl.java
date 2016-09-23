package tools;

import android.app.Activity;
import android.app.FragmentManager; 
import android.app.Fragment; 
import android.app.FragmentTransaction;

public class FragmentControl {
	private  FragmentManager manager ;
	public FragmentControl(Activity context){
		 manager= context.getFragmentManager();
	}
	public void loadFragment(Fragment fragment, int replaceView) { 
		FragmentTransaction transaction = manager.beginTransaction();  
		transaction.replace(replaceView, fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.addToBackStack(null);
		transaction.commit(); 
	}
}
