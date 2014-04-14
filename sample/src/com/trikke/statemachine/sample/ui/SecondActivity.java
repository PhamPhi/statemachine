package com.trikke.statemachine.sample.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.trikke.statemachine.StateMachine;
import com.trikke.statemachine.sample.R;
import com.trikke.statemachine.sample.data.MyState;

public class SecondActivity extends BaseActivity
{
	TextView mText;

	private MyState state;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.second );

		state = StateMachine.get();

		mText = (TextView) findViewById( R.id.statetext );

		Button finishButton = (Button) findViewById( R.id.finish );
		finishButton.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				SecondActivity.this.finish();
			}
		} );
	}

	public void onResume()
	{
		super.onResume();
		update();
	}

	private void update()
	{
		mText.setText( "The State was last modified on " + state.getLastModified() + "\nData value : " + state.getNumber() );
	}
}
