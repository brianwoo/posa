/**
 * 
 */
package vandy.mooc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

/**
 * @author bwoo
 * 
 */
public abstract class AsyncTaskFragment extends Fragment
{

	/**
	 * Callback interface through which the fragment will report the task's
	 * progress and results back to the Activity.
	 */
	interface TaskCallbacks
	{
		void onPreExecute();

		void onProgressUpdate(int percent);

		void onCancelled();

		void onPostExecute(String result);
	}

	private TaskCallbacks mCallbacks;

	/**
	 * Hold a reference to the parent Activity so we can report the task's
	 * current progress and results. The Android framework will pass us a
	 * reference to the newly created Activity after each configuration change.
	 */
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		mCallbacks = (TaskCallbacks) activity;
	}

	
	/**
	 * Set the callback to null so we don't accidentally leak the Activity
	 * instance.
	 */
	@Override
	public void onDetach()
	{
		super.onDetach();
		mCallbacks = null;
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);
		executeTask();
	}

	
	protected TaskCallbacks getActivityCallback()
	{
		return mCallbacks;
	}
	
	
	protected Context getApplicationContext()
	{
		if (mCallbacks != null)
		{
			return ((Activity) mCallbacks).getApplicationContext();
		}
		else
			return null;
	}
	
	
	protected abstract void executeTask();
	
}
