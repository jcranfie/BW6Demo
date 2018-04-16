package com.coopbank.common.xml.utils;

public class Field
{
	private String name = null;
	private String value = null;
	
	public Field(String name, String value)
	{
		setName(name);
		setValue(value);
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	
}
