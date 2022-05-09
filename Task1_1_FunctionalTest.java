package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task1_1_FunctionalTest {
	private Parser parser;
	
	/**
	 * BUGS FOUND:
	 * 1
	 * 2
	 * 3
	 * 4
	 * 5
	 * 6
	 * 7
	 * 8
	 * 9
	 * 10
	 * 11
	 * 12
	 * 13
	 * 14
	 * 15
	 * 16
	 * 18
	 * 20
	 */
	
	

	
	@Before
	public void set_up() {parser = new Parser();}

	
	
	
	@Test //(expected = RuntimeException.class)
	public void no_shortcut_test() {
		
		//Bug 1
		
	parser.addOption(new Option("Output", Type.STRING));
	
	parser.parse("--Output=output.txt");
	assertEquals(parser.getString("Output"),"output.txt");
		


	}

	



	@Test
	public void boolean_set_to_false_using_0_test() {
		//bug 2
		parser.addOption(new Option("output", Type.BOOLEAN),"o");
		parser.parse("--output=0");
		assertEquals(parser.getBoolean("output"), false);
		

	}

	

	
		

	

	@Test	
	public void boolean_string_integer_test() {
		//Bug 3
		parser.addOption(new Option("output", Type.BOOLEAN), "o");
		parser.parse("--output=1000000");
		assertEquals(parser.getInteger("o"), 1000000);
		
		
	}




	@Test //(expected = RuntimeException.class)
	public void name_does_not_contain_invalid_character() {
		//Bug 4 Medium 2pts
		parser.addOption(new Option("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890__", Type.STRING), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_");
		

	}
	

	
	
	
	@Test //(expected = ArithmeticException.class)
	public void value_smaller_than_minus_one_test() {
		//Bug 5
		
		parser.addOption(new Option("output", Type.INTEGER), "o");
		
		parser.parse("--output=-1");
		
		
		
		assertEquals(parser.getInteger("o"), "-1");		
	
	}
	
	@Test
	public void second_test(){
		//Bug 6
		
		parser.addOption(new Option("output", Type.INTEGER), "o");
		parser.addOption(new Option("output1", Type.INTEGER), "o1");
		Option output = new Option("output", Type.INTEGER);
		Option output1 = new Option("output1", Type.INTEGER);
		
		parser.parse("output=1");
		parser.parse("output1=3");
		assertFalse(output.equals(output1));
		
		
		
	}
	
	@Test
	public void same_name_different_type_test() {
		
		//Bug 8 Medium 2pts
		//Bug 7 Hard 3pts
		
		
		parser.addOption(new Option("output", Type.STRING), "o");
		parser.parse("--output 1.txt");
		assertEquals(parser.getString("o"),"1.txt");
	
		parser.addOption(new Option("output", Type.INTEGER), "o");
		parser.parse("--output 5");
		assertEquals(parser.getInteger("o"), 5);
	
		
		parser.addOption(new Option("output", Type.BOOLEAN), "o");
		parser.parse("--output 5");
		assertNotEquals(parser.getInteger("o"), 5);
	
		parser.parse("--output true");
		assertTrue(parser.getBoolean("o"));
	
		
		parser.addOption(new Option("output",  Type.CHARACTER), "o");
		
	
		parser.parse("--output o");
		assertEquals(parser.getCharacter("o"),'o');
		
	
	}




	@Test
	public void length_zero_test() {
		//Bug 9		
		assertEquals(parser.parse(" "), "-2");
		
	}




	@Test
	public void char_case_test(){
		
		//Bug 10 Easy 1pt
		parser.addOption(new Option("output", Type.CHARACTER),"o");
		parser.addOption(new Option("Output", Type.CHARACTER), "o");
		
		parser.parse("--output=a");
		assertEquals(parser.getCharacter("o"), 'a');
		
		parser.parse("--Output=a");
		assertNotEquals(parser.getCharacter("o"), 'A');
		
	}




	@Test //(expected = RuntimeException.class)
	public void name_contains_invalid_character() {
		
		//Bug 11 Hard 3pts
		
	parser.addOption(new Option("out%put", Type.STRING), "o");
	
	}




	@Test 
	public void replace_test() {
		//Bug 12
		parser.addOption(new Option("output",Type.STRING), "o");
		parser.parse("--output=1.txt");		
		parser.replace ("-o","1.txt","2.txt");
		assertEquals(parser.getString("o"), "2.txt");
	}




	@Test
	public void parsingWithMultipleOptionTest() {
		//Bug 13 Medium
		
		parser.addOption(new Option("string", Type.STRING), "n");
		parser.parse("--string='ab' --string='cd'");
		
		assertEquals(parser.getString("n"),  "string");
		
	}




	@Test
	public void newline_test() {
		//Bug 14
		parser.addOption(new Option("output", Type.STRING), "o");
		String a = "abc";
		String b = "def";
		String ab = a + "\\n" + b;
		parser.parse("--output=" + ab);
		
		
		assertEquals(parser.getString("o"), ab);
		
	}




	@Test
	public void max_integer_test(){
		
		//Bug 15
		
		
		int max = 2147483647;
		String str = Integer.toBinaryString(max);
		parser.addOption(new Option("output", Type.STRING), "o");
		parser.parse("--output=" + str);
		assertEquals(parser.getInteger("o"), str);
	}




	@Test
	public void null_test() {
		//Bug 16		
		assertEquals(parser.getString(null), null);
		
	}




	@Test //(expected = RuntimeException.class)
	public void long_string_test() {
		//bug 17
		
		parser.addOption(new Option("thisisalongtstringtestforsoftwaretestingcoursework", Type.STRING));
		
		
		
		
		
	}




	@Test //(expected = RuntimeException.class)
	public void empty_replace_test() {
		//Bug18					
		parser.replace ("   ","   ","   ");		
			
	
	}




	@Test //(expected = RuntimeException.class)
	public void bug_19_test() {		
		//bug 19
		parser.addOption(new Option("output", Type.INTEGER), "o");
		parser.addOption(new Option("output1", Type.INTEGER), "ou");
		parser.parse("--output=\"gh\" --output1=\"ou\"" );
		
	}




	@Test //(expected = RuntimeException.class)
	public void differentOptionsButParsingTwoWithSpace() {		
		//Bug 20 Hard 3pts
		
		parser.addOption(new Option("output", Type.STRING), "o");
		parser.addOption(new Option("output2", Type.STRING ), "o2");
		parser.parse("--output 'old Text' --output2 'old Text1'");
		
	} 
	 
	
	
	
	
	
	
	
	
	
		

		
		
	
	
	
}