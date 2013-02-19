<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4"
					page-width="297mm" page-height="210mm" margin-top="1cm"
					margin-bottom="1cm" margin-left="1cm" margin-right="1cm">
					<fo:region-body margin="3cm" />
					<fo:region-before extent="2cm" />
					<fo:region-after extent="2cm" />
					<fo:region-start extent="2cm" />
					<fo:region-end extent="2cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="A4">
				<fo:flow flow-name="xsl-region-body">
					<fo:table>
						<fo:table-column column-width="8cm" />
						<fo:table-column column-width="5cm" />
						<fo:table-column column-width="5cm" />
						<!-- <fo:table-column column-width="8cm" /> -->
						<fo:table-header>
							<fo:table-row>
								<fo:table-cell border-style="solid" border-width="0.5pt">
									<fo:block font-weight="bold">Time</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.5pt">
									<fo:block font-weight="bold">Event</fo:block>
								</fo:table-cell>
								<fo:table-cell border-style="solid" border-width="0.5pt">
									<fo:block font-weight="bold">Zone</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<xsl:for-each select="array-list/log-entry">
								<fo:table-row>
									<fo:table-cell border-style="solid" border-width="0.5pt">
										<fo:block>
											<xsl:value-of select="time" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border-style="solid" border-width="0.5pt">
										<fo:block text-align="center">
											<xsl:value-of select="event" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell border-style="solid" border-width="0.5pt">
										<fo:block text-align="center">
											<xsl:value-of select="zone" />
										</fo:block>
									</fo:table-cell>
									<!-- Iteration on sub elemnts -->
									<!--  
									<fo:table-cell border-style="solid" border-width="0.5pt">
										<xsl:for-each select="incubation-photos">
											<fo:block>
												<xsl:value-of select="."></xsl:value-of>
												<xsl:value-of select="format-date(.,'[FNn], [D1o] [MNn,*-3], [Y]')">
                                 				</xsl:value-of>
											</fo:block>
										</xsl:for-each>
									</fo:table-cell> 
									-->
								</fo:table-row>
							</xsl:for-each>
						</fo:table-body>
					</fo:table>
				</fo:flow>
				<!-- Page content goes here -->
			</fo:page-sequence>
			<!-- <body> <h2>My CD Collection</h2> <table border="1"> <tr bgcolor="#9acd32"> 
				<th>Title</th> <th>Artist</th> </tr> <xsl:for-each select="catalog/cd"> <tr> 
				<td> <xsl:value-of select="title" /> </td> <td> <xsl:value-of select="artist" 
				/> </td> </tr> </xsl:for-each> </table> </body> -->
		</fo:root>

	</xsl:template>
</xsl:stylesheet>