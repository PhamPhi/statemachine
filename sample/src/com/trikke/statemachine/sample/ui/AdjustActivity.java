package com.trikke.statemachine.sample.ui;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.trikke.statemachine.StateMachine;
import com.trikke.statemachine.sample.R;
import com.trikke.statemachine.sample.data.MyState;

public class AdjustActivity extends BaseActivity
{
	TextView mText;
	NumberPicker mPicker;
	CheckBox mSwitch;

	private MyState state;

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.adjust );

		// You can store your state object after it has been intialized by super.onCreate(), as long as it is properly
		// referenced you can update it on the fly and saves will be handled for you
		state = StateMachine.get();

		mText = (TextView) findViewById( R.id.statetext );
		mPicker = (NumberPicker) findViewById( R.id.numberPicker );
		mPicker.setMinValue( 0 );
		mPicker.setMaxValue( 100 );
		mPicker.setWrapSelectorWheel( true );
		mPicker.setValue( state.getNumber() );

		mPicker.setOnValueChangedListener( new NumberPicker.OnValueChangeListener()
		{
			@Override
			public void onValueChange( NumberPicker numberPicker, int oldv, int newv )
			{
				MyState state = StateMachine.get();
				state.setNumber( newv );
				update();
			}
		} );

		mSwitch = (CheckBox) findViewById( R.id.switchpersistent );
		mSwitch.setChecked( StateMachine.isPersistent() );
		mSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged( CompoundButton compoundButton, boolean b )
			{
				StateMachine.setPersistency( b );
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
