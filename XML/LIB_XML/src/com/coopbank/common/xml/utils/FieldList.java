package com.coopbank.common.xml.utils;

import java.io.Serializable;
import java.util.List;

/**
 * The list of fields returned to the called so it can be
 * converted to XML structure in the BW6 layer.
 * 
 * @author Julian Cranfield - TIBCO Software Inc.
 *
 */
public class FieldList implements Serializable
{

	private static final long serialVersionUID = 5640178884520757927L;
	
	private List<Field> fields = null;

	public List<Field> getFields()
	{
		return fields;
	}

	public void setFields(List<Field> fields)
	{
		this.fields = fields;
	}
	
	public FieldList(List<Field> fields)
	{
		setFields(fields);
	}

	public FieldList()
	{

	}
}
