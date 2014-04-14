package com.trikke.statemachine.sample.ui;

import android.app.Activity;
import android.os.Bundle;
import com.trikke.statemachine.StateMachine;

public class BaseActivity extends Activity
{
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		// immediately restore Machine if needed
		StateMachine.onCreate( this, savedInstanceState );
	}

	@Override
	public void onResume()
	{
		super.onResume();
		StateMachine.onResume( this );
	}

	@Override
	public void onSaveInstanceState( Bundle outState )
	{
		super.onSaveInstanceState( outState );
		StateMachine.onSaveInstanceState( outState );
	}

	@Override
	public void onRestoreInstanceState( Bundle savedInstanceState )
	{
		super.onRestoreInstanceState( savedInstanceState );
		StateMachine.onRestoreInstanceState( this, savedInstanceState );
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		StateMachine.onDestroy();
	}
}
