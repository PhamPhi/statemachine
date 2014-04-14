State Machine
========================

# What

This little library helps you keep a persistent state across your app's lifecycle and even beyond. You can store any `serializable` object int it. It is named **State Machine** because I liked the sound of it, rolling off the tongue, it is not related to the [design pattern][2], nor an implementation of it.

# Download

You can find releases here on [GitHub][1].

# How to implement

The core is the `StateMachine` class. It holds methods to get and initialise your data, and also helper methods to include in your `Activity` 's lifecycle to manage the state.

### Create a data object

Your data object or "State" should implement `Serializable` and any objects you store within should also be `Serializable`. Other than that, you are free to store what you want.

### Implement the lifecycle helpers

The `StateMachine` class holds a few helper methods that should be run on every corresponding method in every activity in your app. The easiest way to approach this is to create a base Activity from which all your other activities inherit. The following example includes all the methods that **must** be included for the system to work properly.

```java
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

```

### Getting started with your state

Once the above has been done, you have to create your state and initialise it as soon as possible. Most likely this will be in the `onCreate` method of the first `Activity` your app opens.

```java
@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		// This will set the initial state, if a state already exists, this is ignored.
		MyState state = new MyState();
		state.setSomeInitialData( 10 );
		StateMachine.initialize( state, true );

		// if a state existed before, the following would return the old value (eg: 186), otherwise this will return the 10 from the new state above.
		int amount = StateMachine.get( MyState.class ).getInitializedData();
	}
```

If you wish to override your state anyhow, you can do the following

```java
		// if you wish to override your state anyhow you can do the following
		MyState state = new MyState();
		state.setSomeInitialData( 10 );
		StateMachine.create( this, state, true );
		// this will return the 10 from the new state above.
		int amount = StateMachine.get( MyState.class ).getInitializedData();
```

Accessing your state can be done like this

```java
		// You get get your object like this
		MyState state = StateMachine.get();
		state.doStuff();
		// Or, do it like this
		StateMachine.get( MyState.class ).doStuff();
```

I've added a small sample application (Android 4+) that shows all of the above.

### A note on persistency

Persistency in `StateMachine` is defined as the ability to retain your state across multiple Application life cycles. This means if your app is killed completely ( by yourself, user or task killer ), your state would still be available on the next cold boot of your app. You can toggle it with `StateMachine.setPersistency()`. Or you pass it with `StateMachine.initialize()`. 

If this is turned off, your state will become unavailable if is no longer in memory and there is no **saved instancestate Bundle** available upon recreating your app. See [Recreating an Activity][3] for more info on the topic. 

*Settings this boolean to true would make the Machine save your state in `onDestroy` to a local file.*

[1]:https://github.com/Trikke/StateMachine/releases
[2]:http://en.wikipedia.org/wiki/State_pattern
[3]:http://developer.android.com/training/basics/activity-lifecycle/recreating.html