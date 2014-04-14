package com.trikke.statemachine.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.trikke.statemachine.StateMachine;
import com.trikke.statemachine.sample.R;
import com.trikke.statemachine.sample.data.MyState;

public class MainActivity extends BaseActivity
{
	TextView mText;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.main );

		mText = (TextView) findViewById( R.id.statetext );

		Button gotoNext = (Button) findViewById( R.id.gotosecond );
		gotoNext.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				startActivity( new Intent( MainActivity.this, SecondActivity.class ) );
			}
		} );

		Button gotoAdjust = (Button) findViewById( R.id.gotoadjuststate );
		gotoAdjust.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				startActivity( new Intent( MainActivity.this, AdjustActivity.class ) );
			}
		} );

		Button wipeState = (Button) findViewById( R.id.wipestate );
		wipeState.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				MyState state = new MyState();
				state.setNumber( 10 );
				StateMachine.create( MainActivity.this, state, true );
				update();
			}
		} );

		initializeLocalState();
	}

	public void onResume()
	{
		super.onResume();
		update();
	}

	private void initializeLocalState()
	{
		// This will set the initial state, if a state with data already exists, this is ignored.
		MyState state = new MyState();
		state.setNumber( 10 );
		StateMachine.initialize( state, true );
	}

	private void update()
	{
		// You get get your object like this
		MyState state = StateMachine.get();

		// Or use it like this
		mText.setText( "The State was last modified on " + StateMachine.get( MyState.class ).getLastModified() + "\nData value : " + StateMachine.get( MyState.class ).getNumber() );
	}
}
