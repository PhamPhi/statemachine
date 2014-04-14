package com.trikke.statemachine;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.*;

/**
 * “ Always code as if the guy who ends up maintaining your code will be a violent psychopath who knows where you live. ”
 */
class Machine implements Serializable
{
	private static final String BUNDLE_SAVE_KEY = "com.trikke.statemachine.SaveKey";
	private static final String STATE_SAVE_FILEPATH = "state.bin";

	private static final long serialVersionUID = 7254089214028172382L;
	public static final String TAG = Machine.class.getCanonicalName();

	private static final Object LOCK = new Object();
	private static volatile Context staticContext;
	private static Machine activeMachine;

	private boolean isPersistent;
	private Serializable storage;

	public Machine( Context context )
	{
		Log.d( TAG, "Creating new machine." );
		initializeStaticContext( context );
	}

	public Serializable get()
	{
		return storage;
	}

	public void set( Serializable storage )
	{
		this.storage = storage;
	}

	/**
	 * Returns the current Machine, or null if there is none.
	 *
	 * @return the current Machine
	 */
	protected static Machine getActiveMachine()
	{
		synchronized ( Machine.LOCK )
		{
			return Machine.activeMachine;
		}
	}

	/**
	 * Sets the current Machine, usually from a file or bundle, or a newly created fresh one on first startup
	 */
	protected static void setActiveMachine( Machine machine )
	{
		synchronized ( Machine.LOCK )
		{
			if ( machine != Machine.activeMachine )
			{
				Machine.activeMachine = machine;
			}
		}
	}

	private static void initializeStaticContext( Context currentContext )
	{
		if ( (currentContext != null) && (staticContext == null) )
		{
			Context applicationContext = currentContext.getApplicationContext();
			staticContext = (applicationContext == null) ? currentContext : applicationContext;
		}
	}

	protected static void storeState()
	{
		Machine machine = getActiveMachine();

		FileOutputStream fos;
		ObjectOutputStream os;

		try
		{
			fos = staticContext.openFileOutput( STATE_SAVE_FILEPATH, Context.MODE_PRIVATE );
			OutputStream buffer = new BufferedOutputStream( fos );
			os = new ObjectOutputStream( buffer );

			try
			{
				os.writeObject( machine );
			} finally
			{
				os.close();
				buffer.close();
				if ( fos != null ) fos.close();
			}

			Log.d( TAG, "Saved machine successfully to " + STATE_SAVE_FILEPATH );
		} catch ( Exception e )
		{
			Log.e( TAG, "Couldn't save machine to " + STATE_SAVE_FILEPATH, e );
		}

	}

	protected static Machine restoreState( Context context )
	{
		Machine machine = null;
		FileInputStream fis;
		ObjectInputStream is;
		try
		{
			fis = context.openFileInput( STATE_SAVE_FILEPATH );
			InputStream buffer = new BufferedInputStream( fis );
			is = new ObjectInputStream( buffer );

			try
			{
				machine = (Machine) is.readObject();

			} finally
			{
				is.close();
				buffer.close();
				if ( fis != null ) fis.close();
			}

			Log.d( TAG, "Loaded machine successfully from " + STATE_SAVE_FILEPATH );
		} catch ( FileNotFoundException fnfe )
		{
			return null;
		} catch ( Exception e )
		{
			Log.e( TAG, "Couldn't load machine from " + STATE_SAVE_FILEPATH, e );
		}


		initializeStaticContext( context );
		return machine;
	}

	/**
	 * Restores the saved Machine from a Bundle, if any. Returns the restored Machine or
	 * null if it could not be restored. This method is intended to be called from an Activity or Fragment's
	 * onCreate method when a Machine has previously been saved into a Bundle via saveState to preserve a Machine
	 * across Activity lifecycle events.
	 *
	 * @param context the Activity or Service creating the Machine, must not be null
	 * @param bundle  the bundle to restore the Machine from
	 * @return the restored Machine, or null
	 */
	protected static Machine onRestoreInstanceState( Context context, Bundle bundle )
	{
		Log.d( TAG, "Loading state from bundle" );
		if ( bundle == null )
		{
			return null;
		}
		byte[] data = bundle.getByteArray( BUNDLE_SAVE_KEY );
		if ( data != null )
		{
			ByteArrayInputStream is = new ByteArrayInputStream( data );
			try
			{
				Machine machine = (Machine) (new ObjectInputStream( is )).readObject();
				initializeStaticContext( context );
				return machine;
			} catch ( ClassNotFoundException e )
			{
				Log.e( TAG, "Unable to restore state", e );
			} catch ( IOException e )
			{
				Log.e( TAG, "Unable to restore state.", e );
			}
		}
		return null;
	}

	/**
	 * Save the Machine object into the supplied Bundle.
	 * This method is intended to be called from an Activity or Fragment's onSaveInstanceState method in order to preserve the Machine across Activity lifecycle events.
	 *
	 * @param machine the Machine to save
	 * @param bundle  the Bundle to save the Machine to
	 */
	protected static void onSaveInstanceState( Machine machine, Bundle bundle )
	{
		Log.d( TAG, "Saving machine to bundle" );
		if ( bundle != null && machine != null && !bundle.containsKey( BUNDLE_SAVE_KEY ) )
		{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try
			{
				new ObjectOutputStream( outputStream ).writeObject( machine );
			} catch ( IOException e )
			{
				throw new RuntimeException( "Unable to save machine.", e );
			}
			bundle.putByteArray( BUNDLE_SAVE_KEY, outputStream.toByteArray() );
		}
	}

	public boolean isPersistent()
	{
		return isPersistent;
	}

	public void setPersistent( boolean isPersistent )
	{
		this.isPersistent = isPersistent;
		if ( !isPersistent )
		{
			// remove any file if this is switched so no old state is remembered.
			staticContext.deleteFile( STATE_SAVE_FILEPATH );
		}
	}
}
