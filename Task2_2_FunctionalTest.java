

package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task2_2_FunctionalTest {
	private Parser parser;
	private OptionMap optionMap;
	private Option option;
	
		
	@Before
	public void set_up() {
		
		parser = new Parser();
	}

	
	
	
	@Test //(expected = RuntimeException.class)
	public void no_shortcut_test() {
		
		//Bug 1
		
	parser.addOption(new Option("output", Type.STRING));
	
	parser.parse("--output=output.txt");
	assertEquals(parser.getString("output"),"output.txt");
		


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
		parser.parse("--output=false");
		assertEquals(parser.getInteger("o"), 0);
		
		
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
		
		
		
		assertEquals(parser.getInteger("o"), -1);		
	
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
		
		assertNotEquals(parser.getString("o"), "0");
		
		
		
		parser.addOption(new Option("output", Type.INTEGER), "o");
		parser.parse("--output 5");
		assertEquals(parser.getInteger("o"), 5);
		assertNotEquals(parser.getInteger("o"), 0);
		assertNotEquals(parser.getBoolean("o"), 0);
	
		
		parser.addOption(new Option("output", Type.BOOLEAN), "o");
		parser.parse("--output 5");
		assertNotEquals(parser.getInteger("o"), 5);
	
		parser.parse("--output true");
		assertTrue(parser.getBoolean("o"));
		assertNotEquals(parser.getBoolean("o"), 0);
	
		
		parser.addOption(new Option("output",  Type.CHARACTER), "o");
		
		parser.parse("--output o");
		assertNotEquals(parser.getInteger("o"), 0);
		assertEquals(parser.getCharacter("o"),'o');
		assertNotEquals(parser.getCharacter("o"), 0);
		
		parser.addOption(new Option("output1", Type.BOOLEAN),"o");
		parser.parse("--output1=0");
		assertFalse(parser.getBoolean("o"));
		
		
		assertNotEquals(parser.getInteger("o"),false);
		
		
		
		
		
	
	}




	@Test
	public void length_zero_test() {
		//Bug 9		
		assertEquals(parser.parse(""), -2);
		
	}




	@Test
	public void char_case_test(){
		
		//Bug 10 Easy 1pt
		parser.addOption(new Option("output", Type.CHARACTER),"o");
		parser.addOption(new Option("Output", Type.CHARACTER), "O");
		
		parser.parse("--output=a");
		assertEquals(parser.getCharacter("o"), 'a');
		
		parser.parse("--Output=a");
		assertNotEquals(parser.getCharacter("O"), 'A');
		
	}




	




	@Test 
	public void replace_test() {
		//Bug 12
		parser.addOption(new Option("opt1",Type.STRING), "o");
		parser.parse("--opt1=1.txt");		
		parser.replace ("-o","1.txt","2.txt");
		assertEquals(parser.getString("o"), "2.txt");
		
		parser.addOption(new Option("opt2",Type.STRING), "o2");
		parser.parse("--opt2=3.txt");		
		parser.replace ("--opt2","3.txt","4.txt");
		assertEquals(parser.getString("o2"), "4.txt");
		
		parser.addOption(new Option("opt3",Type.STRING), "o3");
		parser.parse("o3=5.txt");		
		parser.replace ("o3","5.txt","6.txt");
		assertEquals(parser.getString("o3"), "6.txt");
		
		parser.addOption(new Option("o4",Type.STRING), "o4");
		parser.parse("o4=7.txt");		
		parser.replace ("o4","7.txt","8.txt");
		assertEquals(parser.getString("o4"), "8.txt");
		
	}




	@Test
	public void parsingWithMultipleOptionTest() {
		//Bug 13 Medium
		
		parser.addOption(new Option("string", Type.STRING), "n");
		parser.parse("--string='ab' --string='cd'");
		

		
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
		assertEquals(parser.getInteger("o"), 0);
	}




	@Test
	public void null_test() {
		//Bug 16		
		//assertNull(parser.getString(null));
		parser.parse(null);
		
	}




	@Test //(expected = RuntimeException.class)
	public void long_string_test() {
		//bug 17
		
		parser.addOption(new Option("thisisalongtstringtestforsoftwaretestingcoursework", Type.STRING));
		
		
		
		
		
	}




	@Test //(expected = RuntimeException.class)
	public void empty_replace_test() {
		//Bug18					
		parser.replace (" "," "," ");		
			
	
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
		
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.addOption(new Option("option2", Type.STRING ), "o1");
		parser.parse("--option 'old Text' --option1 'old Text1'");
		
	} 
	
	@Test
	public void set_shortcut_test() {
		parser.addOption(new Option("output", Type.STRING));
		parser.parse("--output=output.txt");
		parser.setShortcut("output", "o");
		
		parser.shortcutExists("o");
		parser.optionExists("output");
		parser.optionOrShortcutExists("o");
		parser.optionOrShortcutExists("output");
		
		
	}
	
	
	@Test
	
	public void assert_not_equals_test() {
		
		parser.addOption(new Option("output", Type.BOOLEAN), "o");
		assertNotEquals(parser.getBoolean("o"), "false");
		assertNotEquals(parser.getBoolean("o"), "true");
		
		parser.addOption(new Option("output", Type.STRING), "o");
		assertNotEquals(parser.getString("o"), "[a-zA-Z0-9_]*");
		
		
		
		parser.addOption(new Option("output", Type.INTEGER), "o");
		assertNotEquals(parser.getInteger("o"), "[0,9]*");
	
		
		parser.addOption(new Option("output", Type.CHARACTER), "o");
		
		assertNotEquals(parser.getCharacter("o"), "[a-zA-Z]*");
		
		
		
		
	}
	
	
	@Test
	
	public void test() {
		parser.addOption(new Option("opt1", Type.STRING));
		parser.addOption(new Option("opt2", Type.STRING));
		parser.parse("--opt1=old --opt2=old2");
		parser.replace("opt1 opt2", "old", "new");
		parser.setShortcut("opt1", "o");
		
		
	}
	
	@Test
	public void get_string_test() {
		parser.addOption(new Option("output", Type.STRING), "o");
		assertNotEquals(parser.getString("o"),"false");
		assertNotEquals(parser.getString("o"),"0");
		assertNotEquals(parser.getString("o")," ");
		assertNotEquals(parser.getString("o"), "\0");
		
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void no_type_test() {
		parser.addOption(new Option("output", Type.NOTYPE));
	}
	
	@Test
	public void testt() {
		parser.addOption(new Option("output", Type.BOOLEAN), null);
		parser.parse("--output=false");
		assertEquals(parser.getBoolean(null),"false");
		
		parser.addOption(new Option("output", Type.BOOLEAN), "o");
		parser.parse("--output=%");
		assertEquals(parser.getBoolean(""), "false");
	}

		
		
		
		
	
	
	
	
	
	
	
	
	
}