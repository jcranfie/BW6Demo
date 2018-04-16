<?xml version="1.0" encoding = "UTF-8"?>
<xsl:stylesheet version="1.0"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:prof="http://www.tibco.com/xmlns/repo/types/2002" exclude-result-prefixes="prof">
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">
 <configuration>
  <properties>
    <xsl:for-each select="prof:repository/prof:globalVariables/prof:globalVariable">
	 <xsl:choose>
        <xsl:when test="not(
contains(prof:name,'//BW.DEPLOYMENTUNIT.TYPE') or contains(prof:name,'//BW.DEPLOYMENTUNIT.NAME') or contains(prof:name,'//BW.APPNODE.NAME') or contains(prof:name,'//BW.HOST.NAME') or contains(prof:name,'//BW.DOMAIN.NAME') or contains(prof:name,'//BW.APPSPACE.NAME') or contains(prof:name,'//BW.MODULE.VERSION') or contains(prof:name,'//BW.DEPLOYMENTUNIT.VERSION') or contains(prof:name,'//BW.MODULE.NAME'))">
      <xsl:element name="property">
        <xsl:attribute name="name"><xsl:value-of select="prof:name"/></xsl:attribute>
		 <xsl:element name="environment">
		  <xsl:attribute name="name"><xsl:text>DEFAULT</xsl:text></xsl:attribute>
		  <xsl:attribute name="value"><xsl:value-of select="prof:value"/></xsl:attribute>
		  </xsl:element>
         <xsl:element name="environment">
		  <xsl:attribute name="name"><xsl:text>DEV</xsl:text></xsl:attribute>
		  <xsl:attribute name="value"><xsl:value-of select="prof:value"/></xsl:attribute>
		  </xsl:element>
		  <xsl:element name="environment">
		  <xsl:attribute name="name"><xsl:text>SIT</xsl:text></xsl:attribute>
		  <xsl:attribute name="value"><xsl:value-of select="prof:value"/></xsl:attribute>
		   </xsl:element>
		  <xsl:element name="environment">
		  <xsl:attribute name="name"><xsl:text>PROD</xsl:text></xsl:attribute>
		  <xsl:attribute name="value"><xsl:value-of select="prof:value"/></xsl:attribute>
		  </xsl:element>
       </xsl:element>
	  
        </xsl:when>
		 </xsl:choose>
    </xsl:for-each>
</properties>
</configuration>
</xsl:template>
</xsl:stylesheet>