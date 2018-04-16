package com.coopbank.common.xml.utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Flatten utility used to create a name value pair field structure from an XML
 * document.
 * 
 * @version 1.0
 * @author Julian Cranfield - TIBCO Software Inc.
 *
 */
public class FlattenXMLUtil
{

	public static void main(String[] args)
	{
		FlattenXMLUtil obj = new FlattenXMLUtil();

		String xml = "<ns:a1><ns1:a2>fred</ns1:a2><ns1:a3>Jim</ns1:a3><ns1:a4><ns1:a5 sillyName='notjane'>Jane</ns1:a5></ns1:a4></ns:a1>";

		String[] reg = new String[]{"a1/a2","a1/a4/a5"};
		
		FieldList ret = obj.flattenXML(xml, reg);

		for (Field field : ret.getFields())
		{
			System.out.println(field.getName() + " - " + field.getValue());
		}

	}

	/**
	 * Basic Constructor for BW6 to construct
	 */
	public FlattenXMLUtil()
	{
		//Serializer with JRE SUN XML (avoid error on deployed apps)
		//System.setProperty("javax.xml.stream.XMLInputFactory", "com.sun.xml.internal.stream.XMLInputFactoryImpl");
		//System.setProperty("javax.xml.stream.XMLOutputFactory", "com.sun.xml.internal.stream.XMLOutputFactoryImpl");
		//System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
		//System.setProperty("javax.xml.transform.TransformerFactory","com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
	}

	/**
	 * Take an XML document as a String and then create a set of name value
	 * paired from this with the name being the full XML path to the value.
	 * Attributes are handled in the same way as elements and the attribute name
	 * is appended to the path.
	 * 
	 * @param xmlString
	 * @return
	 */
	public FieldList flattenXML(String xmlString)
	{
		return flattenXML(xmlString, null);
	}

	/**
	 * Take an XML document as a String and then create a set of name value
	 * paired from this with the name being the full XML path to the value.
	 * Attributes are handled in the same way as elements and the attribute name
	 * is appended to the path. The fields returned are only those also in the
	 * requiredFields argument. This argument can be null, in which case all fields
	 * are returned.
	 * 
	 * @param xmlString
	 * @param requiredFields
	 * @return
	 */
	public FieldList flattenXML(String xmlString, String[] requiredFields)
	{
		List<Field> fields = new ArrayList<Field>();

		Document xml = convertStringToDocument(removeXmlStringNamespaceAndPreamble(xmlString));
		if (xml != null)
		{
			Node node = xml.getFirstChild();
			if (requiredFields != null)
			{
				ArrayList<String> req = new ArrayList<String>();
				for (String name : requiredFields)
				{
					req.add(name);
				}
				fields = extractNode(node, node.getNodeName(), fields, req);
			}
			else
			{
				fields = extractNode(node, node.getNodeName(), fields, null);
			}
		}

		return new FieldList(fields);
	}

	/**
	 * Using the XML Node, extract the child nodes to create a List of name
	 * value pairs. This is called recursively for each node.
	 * 
	 * @param node
	 * @param path
	 * @param fields
	 * @return
	 */
	private List<Field> extractNode(Node node, String path, List<Field> fields, List<String> requiredFields)
	{
		// Check if there are child nodes, either element or attribute, call
		// recursively
		// for each one. The path is built up as we walk the XML document.
		if (node.hasChildNodes())
		{
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++)
			{
				Node child = children.item(i);
				extractNode(child, path + (child.getNodeType() != 3 ? "/" + child.getNodeName() : ""), fields, requiredFields);
			}
			if (node.hasAttributes())
			{
				NamedNodeMap atts = node.getAttributes();
				for (int i = 0; i < atts.getLength(); i++)
				{
					Node child = atts.item(i);
					extractNode(child, path + (child.getNodeType() != 3 ? "/" + child.getNodeName() : ""), fields, requiredFields);
				}
			}
		}
		else
		{
			if (requiredFields == null || requiredFields.contains(path))
			{
				// At a element value or an attribute, so create the field
				Field field = new Field(path, node.getTextContent());
				fields.add(field);
			}
		}

		return fields;
	}

	/**
	 * Remove all the namespace sections from the XML String to enable the
	 * Flatten to just return the local named value.
	 * 
	 * @param xmlString
	 * @return
	 */
	public static String removeXmlStringNamespaceAndPreamble(String xmlString)
	{
		return xmlString.replaceAll("(<\\?[^<]*\\?>)?", "").replaceAll("xmlns.*?(\"|\').*?(\"|\')", "").replaceAll("(<)(\\w+:)(.*?>)", "$1$3")
				.replaceAll("(</)(\\w+:)(.*?>)", "$1$3");
	}

	/**
	 * Create an XML Document from the passed in String
	 * 
	 * @param xmlStr
	 * @return
	 */
	private Document convertStringToDocument(String xmlStr)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try
		{
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
			return doc;
		}
		catch (Exception e)
		{
		}
		return null;
	}
}
