/**
 * 
 */
package vandy.mooc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * @author bwoo
 * 
 */
public class DownloadAndFilterImageTaskFragment extends Fragment
{

	/**
	 * Callback interface through which the fragment will report the task's
	 * progress and results back to the Activity.
	 */
	interface TaskCallbacks
	{
		void onDownloadImagePreExecute();

		void onDownloadImageProgressUpdate(int percent);

		void onDownloadImageCancelled();

		void onDownloadImagePostExecute(Uri result);
	}

	private TaskCallbacks mCallbacks;
	private Context mApplicationContext;

	/**
	 * Hold a reference to the parent Activity so we can report the task's
	 * current progress and results. The Android framework will pass us a
	 * reference to the newly created Activity after each configuration change.
	 */
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		System.out.println("DownloadImageTaskFragment onAttach!");
		mCallbacks = (TaskCallbacks) activity;
		mApplicationContext = activity.getApplicationContext();
	}

	
	/**
	 * Set the callback to null so we don't accidentally leak the Activity
	 * instance.
	 */
	@Override
	public void onDetach()
	{
		super.onDetach();
		System.out.println("DownloadImageTaskFragment onDetach!");
		mCallbacks = null;
		mApplicationContext = null;
	}

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);
	}
	
	
	
	public void executeTask(Uri uri)
	{
		System.out.println("executeTask!");
		DownloadImageAsyncTask downloadTask = new DownloadImageAsyncTask();
		downloadTask.execute(uri);
		
	}

	
	/**
	 * A Contained Download Image AsyncTask to work with this Fragment
	 * 
	 * @author bwoo
	 *
	 */
	class DownloadImageAsyncTask extends AsyncTask<Uri, Void, Uri>
	{

		@Override
		protected Uri doInBackground(Uri... params)
		{

			Uri downloadedUri = 
					Utils.downloadImage(mApplicationContext, params[0]);
									
			return downloadedUri;
		}

		
		
		@Override
		protected void onPostExecute(Uri result)
		{
			if (null == result)
				return;
			
			ImageFilterAsyncTask imageFilterTask = new ImageFilterAsyncTask();
			imageFilterTask.execute(result);
		}
		
	}
	

	class ImageFilterAsyncTask extends AsyncTask<Uri, Void, Uri>
	{

		@Override
		protected Uri doInBackground(Uri... params)
		{
			Uri filteredFile = 
					Utils.grayScaleFilter(mApplicationContext, params[0]);
			
			return filteredFile;
		}

		
		@Override
		protected void onPostExecute(Uri result)
		{
			if (mCallbacks != null)
				mCallbacks.onDownloadImagePostExecute(result);
		}
		
	}
	
	
}
