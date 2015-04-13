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
import android.util.Log;
import android.widget.Toast;

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
		void onDownloadAndFilterImagePreExecute();

		void onDownloadAndFilterImageProgressUpdate(int percent);

		void onDownloadAndFilterImageCancelled();

		void onDownloadAndFilterImagePostExecute(Uri result);
	}

	private static final String TAG = DownloadAndFilterImageTaskFragment.class.getSimpleName();

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
		Log.d(TAG,"DownloadImageTaskFragment onAttach!");
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
		Log.d(TAG,"DownloadImageTaskFragment onDetach!");
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
	
	
	/**
	 * execute the DownloadImageAsyncTask then the FilterImageAsyncTask
	 * @param uri
	 */
	public void executeTask(Uri uri)
	{
		Log.d(TAG, "executeTask!");
		DownloadImageAsyncTask downloadTask = new DownloadImageAsyncTask();
		downloadTask.execute(uri);
		
	}

	
	/**
	 * A Contained Download Image AsyncTask to work with this Fragment
	 * 
	 * @author bwoo
	 *
	 */
	private class DownloadImageAsyncTask extends AsyncTask<Uri, Void, Uri>
	{

		@Override
		protected Uri doInBackground(Uri... params)
		{

			Uri downloadedUri = 
					Utils.downloadImage(mApplicationContext, params[0]);
									
			return downloadedUri;
		}

		
		/**
		 * After download is complete, this will trigger the ImageFilterAsyncTask
		 */
		@Override
		protected void onPostExecute(Uri result)
		{
			if (null == result)
			{
	        	Toast toast = Toast.makeText(mApplicationContext, 
	        			"Unable to download image", Toast.LENGTH_SHORT);
	        	toast.show();
				return;
			}
			
			ImageFilterAsyncTask imageFilterTask = new ImageFilterAsyncTask();
			imageFilterTask.execute(result);
		}
		
	}
	

	/**
	 * ImageFilterAsyncTask to convert image to greyscale.
	 * 
	 * @author bwoo
	 *
	 */
	private class ImageFilterAsyncTask extends AsyncTask<Uri, Void, Uri>
	{

		@Override
		protected Uri doInBackground(Uri... params)
		{
			Uri filteredFile = 
					Utils.grayScaleFilter(mApplicationContext, params[0]);
			
			return filteredFile;
		}

		/**
		 * Return result to the Activity.
		 * 
		 */
		@Override
		protected void onPostExecute(Uri result)
		{
			if (mCallbacks != null)
				mCallbacks.onDownloadAndFilterImagePostExecute(result);
		}
		
	}
	
	
}
