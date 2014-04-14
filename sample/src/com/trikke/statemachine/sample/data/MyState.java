package com.trikke.statemachine.sample.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by the awesome :
 * User: trikke
 * Date: 09/01/14
 * Time: 11:40
 */
public class MyState implements Serializable
{
	private static final long serialVersionUID = 8543653764513308137L;

	private Date lastModified;
	private int number;

	public int getNumber()
	{
		return number;
	}

	public void setNumber( int number )
	{
		this.number = number;
		lastModified = new Date();
	}

	public Date getLastModified()
	{
		return lastModified;
	}
}
