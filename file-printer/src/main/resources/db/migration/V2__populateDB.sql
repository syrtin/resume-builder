insert into templates (name, type, description, content)
values
    ('template01_ru', 'JRXML', 'Template for creating resumes', '<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.21.2.final using JasperReports Library version 6.21.2-8434a0bd7c3bbc37cbf916f2968d35e4b165821a  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="ResumeTemplate" pageWidth="595" pageHeight="842" columnWidth="555" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="af91aa54-8ac7-43ab-a1bc-28d906a1d942">
	<style name="DejaVuSans" isDefault="true" fontName="DejaVu Sans"/>
	<parameter name="fullName" class="java.lang.String"/>
	<parameter name="email" class="java.lang.String"/>
	<parameter name="skills" class="java.lang.String"/>
	<parameter name="objective" class="java.lang.String"/>
	<parameter name="interests" class="java.lang.String"/>
	<parameter name="educationList" class="java.lang.String"/>
	<parameter name="workExperienceList" class="java.lang.String"/>
	<parameter name="photo" class="java.awt.Image"/>
	<background>
		<band height="800">
			<rectangle>
				<reportElement x="0" y="0" width="555" height="800" backcolor="#D3D3D3" uuid="8aa57f9f-8863-4e08-7777-0e6340b75cfd"/>
			</rectangle>
		</band>
	</background>
	<title>
		<band height="50">
			<staticText>
				<reportElement x="0" y="0" width="555" height="50" uuid="8aa57f9f-8863-4e08-8b66-0e6340b75cfd"/>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="24" isBold="true"/>
				</textElement>
				<text><![CDATA[Резюме]]></text>
			</staticText>
		</band>
	</title>
	<detail>
		<band height="210">
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<image scaleImage="RetainShape">
				<reportElement x="20" y="0" width="150" height="200" uuid="e07ab48b-1990-425f-bf46-d1b0af595c19">
					<property name="com.jaspersoft.studio.unit.width" value="px"/>
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
				</reportElement>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<imageExpression><![CDATA[$P{photo}]]></imageExpression>
			</image>
			<textField>
				<reportElement style="DejaVuSans" x="170" y="0" width="385" height="30" uuid="3f709371-b6fb-4047-9f37-3fc5cd97beca">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="18" isBold="true"/>
					<paragraph leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{fullName}]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement style="DejaVuSans" x="170" y="60" width="384" height="20" uuid="5e23f719-8c65-4055-bbee-819d3690c6c5"/>
				<textElement>
					<font size="14"/>
					<paragraph leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA["email: " + $P{email}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="170" y="40" width="120" height="20" uuid="d544dad6-7cb7-4ae6-9965-50ec33412d87"/>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Контакты]]></text>
			</staticText>
			<textField textAdjust="StretchHeight">
				<reportElement style="DejaVuSans" x="170" y="110" width="385" height="30" uuid="81e94bf2-b51a-415b-9fcb-c601dbf0fb98">
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
				</reportElement>
				<textElement>
					<font size="14"/>
					<paragraph leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{objective}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="170" y="90" width="120" height="20" uuid="1dfef121-4da6-4e8c-ba2d-d9b6aeff78ea">
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
				</reportElement>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Цель]]></text>
			</staticText>
			<textField textAdjust="StretchHeight">
				<reportElement style="DejaVuSans" x="170" y="160" width="385" height="40" uuid="4d0eb006-c414-4a03-a800-82b9cd1f2d31">
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
					<printWhenExpression><![CDATA[$P{interests} != null && $P{interests}.trim().length() > 0]]></printWhenExpression>
				</reportElement>
				<textElement>
					<font size="14"/>
					<paragraph leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{interests}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="170" y="140" width="120" height="20" uuid="f217b7c2-2781-454e-b231-1ca95ff8d0e3">
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
					<property name="com.jaspersoft.studio.unit.y" value="px"/>
					<printWhenExpression><![CDATA[$P{interests} != null && $P{interests}.trim().length() > 0]]></printWhenExpression>
				</reportElement>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Интересы]]></text>
			</staticText>
		</band>
		<band height="50" splitType="Stretch">
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<staticText>
				<reportElement x="0" y="0" width="140" height="20" uuid="d260dcd2-bdea-4811-bbfa-b23baa8bd4dc">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Навыки]]></text>
			</staticText>
			<textField textAdjust="StretchHeight">
				<reportElement style="DejaVuSans" stretchType="RelativeToTallestObject" x="0" y="20" width="555" height="20" uuid="2ddfe6a6-21a1-40d4-8bce-44cd7290fedd">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="14"/>
					<paragraph leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{skills}]]></textFieldExpression>
			</textField>
		</band>
		<band height="50" splitType="Stretch">
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<textField textAdjust="StretchHeight">
				<reportElement style="DejaVuSans" stretchType="ContainerHeight" x="0" y="20" width="555" height="20" uuid="ddecdf61-015b-4a88-8870-bc40d7836c35">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="14"/>
					<paragraph lineSpacing="1_1_2" leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{educationList}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="0" y="0" width="140" height="20" uuid="e8ad8c42-bb53-4e14-ba34-06a7e8288eca">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Образование]]></text>
			</staticText>
		</band>
		<band height="50" splitType="Stretch">
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<staticText>
				<reportElement x="0" y="0" width="140" height="21" uuid="69bd008c-486e-4861-a10a-06a2a116090c">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Опыт]]></text>
			</staticText>
			<textField textAdjust="StretchHeight">
				<reportElement style="DejaVuSans" stretchType="ContainerHeight" x="0" y="20" width="555" height="20" uuid="6b2a2557-bd85-4ab1-8d68-41b4cdce3aba">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="14"/>
					<paragraph lineSpacing="1_1_2" lineSpacingSize="1.0" leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{workExperienceList}]]></textFieldExpression>
			</textField>
		</band>
	</detail>
</jasperReport>
'),
    ('template01_en', 'JRXML', 'Template for creating resumes', '<?xml version="1.0" encoding="UTF-8"?>
<!-- Created with Jaspersoft Studio version 6.21.2.final using JasperReports Library version 6.21.2-8434a0bd7c3bbc37cbf916f2968d35e4b165821a  -->
<jasperReport xmlns="http://jasperreports.sourceforge.net/jasperreports" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd" name="ResumeTemplate" pageWidth="595" pageHeight="842" columnWidth="555" leftMargin="20" rightMargin="20" topMargin="20" bottomMargin="20" uuid="af91aa54-8ac7-43ab-a1bc-28d906a1d942">
	<style name="DejaVuSans" isDefault="true" fontName="DejaVu Sans"/>
	<parameter name="fullName" class="java.lang.String"/>
	<parameter name="email" class="java.lang.String"/>
	<parameter name="skills" class="java.lang.String"/>
	<parameter name="objective" class="java.lang.String"/>
	<parameter name="interests" class="java.lang.String"/>
	<parameter name="educationList" class="java.lang.String"/>
	<parameter name="workExperienceList" class="java.lang.String"/>
	<parameter name="photo" class="java.awt.Image"/>
	<background>
		<band height="800">
			<rectangle>
				<reportElement x="0" y="0" width="555" height="800" backcolor="#D3D3D3" uuid="8aa57f9f-8863-4e08-7777-0e6340b75cfd"/>
			</rectangle>
		</band>
	</background>
	<title>
		<band height="50">
			<staticText>
				<reportElement x="0" y="0" width="555" height="50" uuid="8aa57f9f-8863-4e08-8b66-0e6340b75cfd"/>
				<textElement textAlignment="Center" verticalAlignment="Middle">
					<font size="24" isBold="true"/>
				</textElement>
				<text><![CDATA[Resume]]></text>
			</staticText>
		</band>
	</title>
	<detail>
		<band height="210">
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<image scaleImage="RetainShape">
				<reportElement x="20" y="0" width="150" height="200" uuid="e07ab48b-1990-425f-bf46-d1b0af595c19">
					<property name="com.jaspersoft.studio.unit.width" value="px"/>
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
				</reportElement>
				<box>
					<topPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<leftPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<bottomPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
					<rightPen lineWidth="1.0" lineStyle="Solid" lineColor="#000000"/>
				</box>
				<imageExpression><![CDATA[$P{photo}]]></imageExpression>
			</image>
			<textField>
				<reportElement style="DejaVuSans" x="170" y="0" width="385" height="30" uuid="3f709371-b6fb-4047-9f37-3fc5cd97beca">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="18" isBold="true"/>
					<paragraph leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{fullName}]]></textFieldExpression>
			</textField>
			<textField isBlankWhenNull="true">
				<reportElement style="DejaVuSans" x="170" y="60" width="384" height="20" uuid="5e23f719-8c65-4055-bbee-819d3690c6c5"/>
				<textElement>
					<font size="14"/>
					<paragraph leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA["email: " + $P{email}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="170" y="40" width="120" height="20" uuid="d544dad6-7cb7-4ae6-9965-50ec33412d87"/>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Contacts]]></text>
			</staticText>
			<textField textAdjust="StretchHeight">
				<reportElement style="DejaVuSans" x="170" y="110" width="385" height="30" uuid="81e94bf2-b51a-415b-9fcb-c601dbf0fb98">
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
				</reportElement>
				<textElement>
					<font size="14"/>
					<paragraph leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{objective}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="170" y="90" width="120" height="20" uuid="1dfef121-4da6-4e8c-ba2d-d9b6aeff78ea">
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
				</reportElement>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Objective]]></text>
			</staticText>
			<textField textAdjust="StretchHeight">
				<reportElement style="DejaVuSans" x="170" y="160" width="385" height="40" uuid="4d0eb006-c414-4a03-a800-82b9cd1f2d31">
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
					<printWhenExpression><![CDATA[$P{interests} != null && $P{interests}.trim().length() > 0]]></printWhenExpression>
				</reportElement>
				<textElement>
					<font size="14"/>
					<paragraph leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{interests}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="170" y="140" width="120" height="20" uuid="f217b7c2-2781-454e-b231-1ca95ff8d0e3">
					<property name="com.jaspersoft.studio.unit.height" value="px"/>
					<property name="com.jaspersoft.studio.unit.y" value="px"/>
					<printWhenExpression><![CDATA[$P{interests} != null && $P{interests}.trim().length() > 0]]></printWhenExpression>
				</reportElement>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Interests]]></text>
			</staticText>
		</band>
		<band height="50" splitType="Stretch">
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<staticText>
				<reportElement x="0" y="0" width="140" height="20" uuid="d260dcd2-bdea-4811-bbfa-b23baa8bd4dc">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Skills]]></text>
			</staticText>
			<textField textAdjust="StretchHeight">
				<reportElement style="DejaVuSans" stretchType="RelativeToTallestObject" x="0" y="20" width="555" height="20" uuid="2ddfe6a6-21a1-40d4-8bce-44cd7290fedd">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="14"/>
					<paragraph leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{skills}]]></textFieldExpression>
			</textField>
		</band>
		<band height="50" splitType="Stretch">
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<textField textAdjust="StretchHeight">
				<reportElement style="DejaVuSans" stretchType="ContainerHeight" x="0" y="20" width="555" height="20" uuid="ddecdf61-015b-4a88-8870-bc40d7836c35">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="14"/>
					<paragraph lineSpacing="1_1_2" leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{educationList}]]></textFieldExpression>
			</textField>
			<staticText>
				<reportElement x="0" y="0" width="140" height="20" uuid="e8ad8c42-bb53-4e14-ba34-06a7e8288eca">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Education]]></text>
			</staticText>
		</band>
		<band height="50" splitType="Stretch">
			<property name="com.jaspersoft.studio.unit.height" value="px"/>
			<staticText>
				<reportElement x="0" y="0" width="140" height="21" uuid="69bd008c-486e-4861-a10a-06a2a116090c">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="16" isBold="true"/>
					<paragraph leftIndent="25"/>
				</textElement>
				<text><![CDATA[Experience]]></text>
			</staticText>
			<textField textAdjust="StretchHeight">
				<reportElement style="DejaVuSans" stretchType="ContainerHeight" x="0" y="20" width="555" height="20" uuid="6b2a2557-bd85-4ab1-8d68-41b4cdce3aba">
					<property name="com.jaspersoft.studio.unit.leftIndent" value="px"/>
				</reportElement>
				<textElement>
					<font size="14"/>
					<paragraph lineSpacing="1_1_2" lineSpacingSize="1.0" leftIndent="20"/>
				</textElement>
				<textFieldExpression><![CDATA[$P{workExperienceList}]]></textFieldExpression>
			</textField>
		</band>
	</detail>
</jasperReport>
');
