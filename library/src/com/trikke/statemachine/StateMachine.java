package com.trikke.statemachine;

import android.content.Context;
import android.os.Bundle;

import java.io.Serializable;

/**
 * “ Always code as if the guy who ends up maintaining your code will be a violent psychopath who knows where you live. ”
 */
public class StateMachine
{
	/**
	 * To be called from an Activity or Fragment's onCreate method.
	 *
	 * @param activity           the current activity
	 * @param savedInstanceState the previously saved state
	 */
	public static void onCreate( Context activity, Bundle savedInstanceState )
	{
		Machine machine = Machine.getActiveMachine();
		if ( machine == null )
		{
			// from bundle
			if ( savedInstanceState != null )
			{
				machine = Machine.onRestoreInstanceState( activity, savedInstanceState );
			}
			// if not found in bundle, try file ( if persistency was disabled/not used, there would be no file )
			if ( machine == null )
			{
				machine = Machine.restoreState( activity );
			}
			// if not found, create a new one
			if ( machine == null )
			{
				machine = new Machine( activity );
			}
			Machine.setActiveMachine( machine );
		}
	}

	/**
	 * To be called from an Activity or Fragment's onResume method.
	 *
	 * @param activity the current activity
	 */
	public static void onResume( Context activity )
	{
		Machine machine = Machine.getActiveMachine();
		if ( machine == null )
		{
			machine = Machine.restoreState( activity );

			// if not found, create a new one
			if ( machine == null )
			{
				machine = new Machine( activity );
			}
			Machine.setActiveMachine( machine );
		}
	}

	/**
	 * To be called from an Activity or Fragment's onSaveInstanceState method.
	 *
	 * @param outState the bundle to save state in
	 */
	public static void onSaveInstanceState( Bundle outState )
	{
		Machine.onSaveInstanceState( Machine.getActiveMachine(), outState );
	}

	/**
	 * To be called from an Activity or Fragment's onRestoreInstanceState method.
	 *
	 * @param activity           the current activity
	 * @param savedInstanceState the previously saved state
	 */
	public static void onRestoreInstanceState( Context activity, Bundle savedInstanceState )
	{
		// only restore the machine if it was not restored by onCreate ( depends on the developer's preference )
		Machine machine = Machine.getActiveMachine();
		if ( machine == null )
		{
			machine = Machine.onRestoreInstanceState( activity, savedInstanceState );
			Machine.setActiveMachine( machine );
		}
	}

	/**
	 * To be called from an Activity or Fragment's onDestroy method.
	 */
	public static void onDestroy()
	{
		Machine machine = Machine.getActiveMachine();
		if ( machine != null && machine.isPersistent() )
		{
			Machine.storeState();
		}
	}

	/**
	 * Initialize and bind data to the current Machine
	 *
	 * @param state      the object to be stored
	 * @param persistent whether the object should be persistent across multiple app lifecycles. When set to
	 *                   false, this would mean the object would no longer be available once Android doesn't pass
	 *                   a bundle anymore to recreate any Activity that is started. When true, the object is saved
	 *                   in a file and thus is more persistently available.
	 */
	public static void initialize( Serializable state, boolean persistent )
	{
		Machine machine = Machine.getActiveMachine();
		if ( machine != null && machine.get() == null )
		{
			machine.set( state );
			machine.setPersistent( persistent );
		}
	}

	/**
	 * A Handy compound method that does the same as
	 * create(activity) and initialize(state, persistent)
	 *
	 * @param activity   associated context
	 * @param state      the object to be stored
	 * @param persistent whether the object should be persistent across multiple app lifecycles. When set to
	 *                   false, this would mean the object would no longer be available once Android doesn't pass
	 *                   a bundle anymore to recreate any Activity that is started. When true, the object is saved
	 *                   in a file and thus is more persistently available.
	 */
	public static void create( Context activity, Serializable state, boolean persistent )
	{
		Machine machine = new Machine( activity );
		Machine.setActiveMachine( machine );
		machine.set( state );
		machine.setPersistent( persistent );
	}

	/**
	 * Save your current State object
	 *
	 * @param state Something serializable
	 */
	public static void set( Serializable state )
	{
		Machine machine = Machine.getActiveMachine();
		if ( machine != null )
		{
			machine.set( state );
		} else
		{
			throw new RuntimeException( "You shouldn't try to set a state on a machine that does not exist." );
		}
	}

	/**
	 * Get your current State object
	 */
	public static <T extends Serializable> T get()
	{
		return (T) Machine.getActiveMachine().get();
	}

	/**
	 * Get your current State object
	 *
	 * @param cls the class of the object to cast
	 */
	public static <T extends Serializable> T get( Class<T> cls )
	{
		return (T) Machine.getActiveMachine().get();
	}

	/**
	 * Set if the State object should be persistently held
	 *
	 * @param persistent boolean!
	 */
	public static void setPersistency( boolean persistent )
	{
		Machine machine = Machine.getActiveMachine();

		if ( machine != null )
		{
			machine.setPersistent( persistent );
		}
	}

	/**
	 * check if the State object should be persistently held
	 */
	public static boolean isPersistent()
	{
		Machine machine = Machine.getActiveMachine();

		if ( machine != null )
		{
			return machine.isPersistent();
		}

		return false;
	}
}
