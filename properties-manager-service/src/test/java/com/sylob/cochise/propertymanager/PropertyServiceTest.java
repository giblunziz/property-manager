package com.sylob.cochise.propertymanager;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;


public class PropertyServiceTest {
	private static final String KEY_1 = "KEY_1";
	private static final String PROJECT_NAME = "project.name";
	private static final String PROJECT_PATH = "project.path";
	private static final String PROJECT_VERSION = "project.version"; 
	
	@Before
	public void reset() {
		PropertyService.clearAll();
	}

	@Test
	public void test01DynamicMultiPropertyInverted() {
		String expected = "/home/PropertyManager/1.0.0";
		PropertyService.setProperty(PROJECT_NAME, "PropertyManager");
		PropertyService.setProperty(PROJECT_PATH, "/home/${project.name}/${project.version}");
		PropertyService.setProperty(PROJECT_VERSION, "1.0.0");
		
		String path = PropertyService.getProperty(PROJECT_PATH);
		System.out.println("Project path : " + path);
		assertNotNull("Return value for PATH cannot be NULL", path);
		assertEquals("La valeur attendue n'est pas correcte",expected, path);
	}


	
	@Test
	public void testSetSimpleProperty() {
		PropertyService.setProperty(KEY_1,"Valeur de la clef 1");
		String value = PropertyService.getProperty(KEY_1);
		System.out.println("Value : " + value);
		
		assertNotNull("Return value cannot be NULL", value);
	}
	
	@Test
	public void testDynamicProperty() {
		PropertyService.setProperty(PROJECT_NAME, "PropertyManager");
		PropertyService.setProperty(PROJECT_PATH, "/home/${project.name}/");
		
		String path = PropertyService.getProperty(PROJECT_PATH);
		System.out.println("Project path : " + path);
		assertNotNull("Return value for PATH cannot be NULL", path);
	}

	@Test
	public void testDynamicEnvProperty() {
		PropertyService.setProperty(PROJECT_NAME, "PropertyManager");
		PropertyService.setProperty(PROJECT_PATH, "${HOMEDRIVE}/home/${project.name}/");
		
		String path = PropertyService.getProperty(PROJECT_PATH);
		System.out.println("testDynamicEnvProperty: Project path : " + path);
		assertNotNull("Return value for PATH cannot be NULL", path);
		path = PropertyService.getProperty(PROJECT_PATH);
	}

	@Test

	public void testDynamicSystemProperty() {
		PropertyService.setProperty(PROJECT_NAME, "PropertyManager");
		PropertyService.setProperty(PROJECT_PATH, "/home/${project.name}/${os.arch}");
		
		String path = PropertyService.getProperty(PROJECT_PATH);
		System.out.println("testDynamicSystemProperty: Project path : " + path);
		assertNotNull("Return value for PATH cannot be NULL", path);
		path = PropertyService.getProperty(PROJECT_PATH);
	}

	@Test
	public void testDynamicMultiProperty() {
		PropertyService.setProperty(PROJECT_NAME, "PropertyManager");
		PropertyService.setProperty(PROJECT_VERSION, "1.0.0");
		PropertyService.setProperty(PROJECT_PATH, "/home/${project.name}/${project.version}");
		
		String path = PropertyService.getProperty(PROJECT_PATH);
		System.out.println("Project path : " + path);
		assertNotNull("Return value for PATH cannot be NULL", path);
	}


	@Test
	public void testDynamicPropertyMultiCall() {
		PropertyService.setProperty(PROJECT_NAME, "PropertyManager");
		PropertyService.setProperty(PROJECT_VERSION, "1.0.0");
		PropertyService.setProperty(PROJECT_PATH, "/home/#{project.name}/${project.version}");
		
		String path = PropertyService.getProperty(PROJECT_PATH);
		System.out.println("Project path : " + path);

		PropertyService.setProperty(PROJECT_NAME, "PropertyManager-v2");
		String path2 = PropertyService.getProperty(PROJECT_PATH);
		System.out.println("Project path : " + path2);
		
		assertNotEquals("Property is not really dynamic !!!", path, path2);
	
	}
	
	@Test
	public void testStaticUnresolved() {
		PropertyService.setProperty("k1", "The k2 value is ${k2}");
		PropertyService.getProperty("k1");
	}
	
	@Test
	public void testLoadFromFile() throws IOException {
		PropertyService.loadFromFile(this.getClass().getResourceAsStream("/external-values.properties"));
		String APPDATA = PropertyService.getProperty("APPDATA");
		System.out.println("${APPDATA}="+APPDATA);
		String moduleName = PropertyService.getProperty("module.name");
		String moduleVersion = PropertyService.getProperty("module.version");
		String modulePath = PropertyService.getProperty("module.path");
		String moduleExtraPath = PropertyService.getProperty("module.extrapath");
		System.out.println("Module name       : " + moduleName);
		System.out.println("Module version    : " + moduleVersion);
		System.out.println("Module path       : " + modulePath);
		System.out.println("Module extra path : " + moduleExtraPath);
	}
	
}
