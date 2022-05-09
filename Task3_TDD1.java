package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task3_TDD1 {
	
	private Task3_Parser parser;
	
	@Before
	public void setup() {
		parser = new Task3_Parser();
	}
	
	
	@Test //(expected  = NullPointerException.class)
	public void bulk_initialization_test() throws Exception {
		parser.addAll("option1 option2 option3 option4","o1 o2 o3 o4","String Integer Boolean Character");
		parser.parse("-o1=test -o2=33 --option3=true -o4=a");
		System.out.println(parser.getCharacter("o4"));
				
	}
	
	@Test
	public void no_shortcut_test() throws Exception {
		parser.addAll("option5","Integer String");
		parser.parse("--option5=5");
		System.out.println(parser.getInteger("option5"));
	}
	
	@Test
	public void same_type_test() throws Exception {
		parser.addAll("option1 option2 option3", "o1 o2 o3 o4", "String");
		parser.parse("-o1=hello -o2=hola --output3=bonjour --output4=merhaba");
		System.out.println(parser.getString("output4"));
	}
	
	@Test 
	public void group_initialization_test() throws Exception {
		parser.addAll("option7-9 optiona-c optionA-B","o7-12","Integer String");
		
		parser.parse("--option7=1 --option8=test --option9=test2");
	
		parser.parse("--optiona=Test --optionb=TestOption --optionc=TestAgain");
		
		parser.parse("--optionA=Test --optionB=TestOption");
		
		System.out.println(parser.getString("option8"));
	}
	
	@Test
	public void no_type_test() {
		//parser.addAll("option129-11");
		
	}
	
	@Test
	public void test() {
		//parser.addAll("option125-2");
		
	}
	
	@Test
	public void second_test() throws Exception {
		parser.addAll("option1-2 option3-4","o1-4", "Integer");
		parser.parse("-o1-2=1  -o3-4=3");
		assertEquals(parser.getInteger("output1-2"), "1");
	}
	
	@Test
	public void boolean_test() throws Exception {
		parser.addAll("output4-8", "Boolean");
		parser.parse("--output4-6=true --output7-8=false");
		assertEquals(parser.getBoolean("output4"), "true");
		assertEquals(parser.getBoolean("output7"), "false");
		assertEquals(parser.getBoolean("output8"), 0);
		
		
	}
	
	@Test
	public void invalid_character_test() throws Exception {
		parser.addAll("output1-3 out%put3-4", "o1-o4", "String");
		parser.parse("-o1-2=hi -o3-4=hello");
		System.out.println(parser.getString("o3"));
				
	}
	
	
	@Test
	public void third_test() throws Exception{
		parser.addAll("optionA-D optionE-G", "oA-D oE-G", "String Integer");
		parser.parse("--optionA-C=hi -oD=hello --optionE-F=21 -oG=22");
		System.out.println(parser.getInteger("optionE-F"));
		
	}
	
	@Test 
	public void invalid_test() throws Exception {
		parser.addAll("2o option", "String");
		
	}
	@Test 
	public void ascending_test() throws Exception {
		parser.addAll("option129-11", "String");
		parser.parse("--option129-1210=hi --option1211= hello");
		System.out.println(parser.getString("option1211"));
	}
	
	@Test 
	public void descending_test() throws Exception {
		parser.addAll("option125-2", "String");
		parser.parse("--option125-123=hi --option122= hello");
		System.out.println(parser.getString("option122"));
	}
	@Test
	public void invalid_two_test() throws Exception {
		parser.addAll("option1 optionA-C", "b d e f", "String Boolean");
		parser.parse("--option1=hi --optionA=true --optionB=false --optionC=true");
		System.out.println(parser.getBoolean("optionC"));
		
	}
	public void test_again() throws Exception {
		parser.addAll("option7-11 optiona optionA-B", "o7-12", "Integer String");
	}
}
