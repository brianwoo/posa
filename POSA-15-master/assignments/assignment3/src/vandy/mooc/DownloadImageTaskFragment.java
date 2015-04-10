/**
 * 
 */
package vandy.mooc;

import android.net.Uri;
import android.os.AsyncTask;

/**
 * @author bwoo
 *
 */
public class DownloadImageTaskFragment extends AsyncTaskFragment
{

	private Uri mUri;
	
	public DownloadImageTaskFragment(Uri uri)
	{
		this.mUri = uri;
	}
	
	
	
	/* (non-Javadoc)
	 * @see vandy.mooc.AsyncTaskFragment#executeTask()
	 */
	@Override
	protected void executeTask()
	{
		DownloadImageTask task = new DownloadImageTask();
		task.execute(mUri);
	}
	
	

	/**
	 * DownloadImageTask - to download the image as a background
	 * task
	 * 
	 * @author bwoo
	 *
	 */
	class DownloadImageTask extends AsyncTask<Uri, Void, String>
	{

		@Override
		protected String doInBackground(Uri... params)
		{
			
			Uri uriOnDisk = DownloadUtils.downloadImage(getApplicationContext(), params[0]);
			return uriOnDisk.toString();
		}

		
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			TaskCallbacks callBacks = getActivityCallback();
			
			if (callBacks != null)
				callBacks.onPostExecute(result);
		}
		
	}
	

}
