package st;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task3_Parser {

	private OptionMap optionMap;
	
	
	
	public Task3_Parser() {
	
		
	}
	
	public void addOption(Option options, String shortcuts) {
		optionMap.store(options, shortcuts);
	}
	
	public void addOption(Option option) {
		optionMap.store(option, "");
	}
	
	public boolean optionExists(String key) {
		return optionMap.optionExists(key);
	}
	
	public boolean shortcutExists(String key) {
		return optionMap.shortcutExists(key);
	}
	
	public boolean optionOrShortcutExists(String key) {
		return optionMap.optionOrShortcutExists(key);
	}
	
	public int getInteger(String optionName) {
		String value = getString(optionName);
		Type type = getType(optionName);
		int result;
		switch (type) {
			case STRING:
			case INTEGER:
				try {
					result = Integer.parseInt(value);
				} catch (Exception e) {
			        try {
			            new BigInteger(value);
			        } catch (Exception e1) {
			        }
			        result = 0;
			    }
				break;
			case BOOLEAN:
				result = getBoolean(optionName) ? 1 : 0;
				break;
			case CHARACTER:
				result = (int) getCharacter(optionName);
				break;
			default:
				result = 0;
		}
		return result;
	}
	
	public boolean getBoolean(String optionName) {
		String value = getString(optionName);
		return !(value.toLowerCase().equals("false") || value.equals("0") || value.equals(""));
	}
	
	public String getString(String optionName) {
		return optionMap.getValue(optionName);
	}
	
	public char getCharacter(String optionName) {
		String value = getString(optionName);
		return value.equals("") ? '\0' :  value.charAt(0);
	}
	
	public void setShortcut(String optionName, String shortcutName) {
		optionMap.setShortcut(optionName, shortcutName);
	}
	
	public void replace(String variables, String pattern, String value) {
			
		variables = variables.replaceAll("\\s+", " ");
		
		String[] varsArray = variables.split(" ");
		
		for (int i = 0; i < varsArray.length; ++i) {
			String varName = varsArray[i];
			String var = (getString(varName));
			var = var.replace(pattern, value);
			if(varName.startsWith("--")) {
				String varNameNoDash = varName.substring(2);
				if (optionMap.optionExists(varNameNoDash)) {
					optionMap.setValueWithOptionName(varNameNoDash, var);
				}
			} else if (varName.startsWith("-")) {
				String varNameNoDash = varName.substring(1);
				if (optionMap.shortcutExists(varNameNoDash)) {
					optionMap.setValueWithOptionShortcut(varNameNoDash, var);
				} 
			} else {
				if (optionMap.optionExists(varName)) {
					optionMap.setValueWithOptionName(varName, var);
				}
				if (optionMap.shortcutExists(varName)) {
					optionMap.setValueWithOptionShortcut(varName, var);
				} 
			}

		}
	}
	
	private List<CustomPair> findMatches(String text, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    List<CustomPair> pairs = new ArrayList<CustomPair>();
	    while (matcher.find()) {
	    	CustomPair pair = new CustomPair(matcher.start(), matcher.end());
	    	pairs.add(pair);
	    }
	    return pairs;
	}
	
	
	public int parse(String commandLineOptions) {
		if (commandLineOptions == null) {
			return -1;
		}
		int length = commandLineOptions.length();
		if (length == 0) {
			return -2;
		}	
		
		List<CustomPair> singleQuotePairs = findMatches(commandLineOptions, "(?<=\')(.*?)(?=\')");
		List<CustomPair> doubleQuote = findMatches(commandLineOptions, "(?<=\")(.*?)(?=\")");
		List<CustomPair> assignPairs = findMatches(commandLineOptions, "(?<=\\=)(.*?)(?=[\\s]|$)");
		
		
		for (CustomPair pair : singleQuotePairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
	    	
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);
		}
		
		for (CustomPair pair : doubleQuote) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\'", "{S_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
			
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}
		
		for (CustomPair pair : assignPairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll("\'", "{S_QUOTE}").
					  replaceAll("-", "{DASH}");
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}

		commandLineOptions = commandLineOptions.replaceAll("--", "-+").replaceAll("\\s+", " ");


		String[] elements = commandLineOptions.split("-");
		
		
		for (int i = 0; i < elements.length; ++i) {
			String entry = elements[i];
			
			if(entry.isBlank()) {
				continue;
			}

			String[] entrySplit = entry.split("[\\s=]", 2);
			
			boolean isKeyOption = entry.startsWith("+");
			String key = entrySplit[0];
			key = isKeyOption ? key.substring(1) : key;
			String value = "";
			
			if(entrySplit.length > 1 && !entrySplit[1].isBlank()) {
				String valueWithNoise = entrySplit[1].trim();
				value = valueWithNoise.split(" ")[0];
			}
			
			// Explicitly convert boolean.
			if (getType(key) == Type.BOOLEAN && (value.toLowerCase().equals("false") || value.equals("0"))) {
				value = "";
			}
			
			value = value.replace("{S_QUOTE}", "\'").
						  replace("{D_QUOTE}", "\"").
						  replace("{SPACE}", " ").
						  replace("{DASH}", "-").
						  replace("{EQUALS}", "=");
			
			
			boolean isUnescapedValueInQuotes = (value.startsWith("\'") && value.endsWith("\'")) ||
					(value.startsWith("\"") && value.endsWith("\""));
			
			value = value.length() > 1 && isUnescapedValueInQuotes ? value.substring(1, value.length() - 1) : value;
			
			if(isKeyOption) {
				optionMap.setValueWithOptionName(key, value);
			} else {
				optionMap.setValueWithOptionShortcut(key, value);
				
			}			
		}

		return 0;
		
	}

	
	private Type getType(String option) {
		Type type = optionMap.getType(option);
		return type;
	}
	
	@Override
	public String toString() {
		return optionMap.toString();
	}
	
	public void addAll(String options, String shortcuts, String types) throws IllegalArgumentException {
		
		String[] optionsList = options.split(" "); 		
		String[] shortcutsList = shortcuts.split(" ");	
		String[] typesList = types.split(" ");
		
		int i = 0;
		
		List<String> shortcutList = new ArrayList<String>();
		
		while(shortcutsList.length > i) {
			if(shortcutsList[i].contains("-")) {
				String[] allOptions = shortcutsList[i].split("-");
				
				if(!isNumber(allOptions[1]) || isCharacter(allOptions[0]) 
						|| (isNumber(allOptions[0]) && isCharacter(allOptions[1]))
						|| (isNumber(allOptions[1]) && isCharacter(allOptions[1]))
						|| Character.isUpperCase(allOptions[0].charAt(0)) && Character.isLowerCase(allOptions[1].charAt(0))
						|| Character.isUpperCase(allOptions[1].charAt(0)) && Character.isLowerCase(allOptions[0].charAt(0))
						){
						break;								
				}else if(isNumber(allOptions[0])) {
					int a = Integer.parseInt(allOptions[0]); 
					if(Integer.parseInt(allOptions[0]) <= Integer.parseInt(allOptions[1])) {
						while(Integer.parseInt(allOptions[0]) <= Integer.parseInt(allOptions[1])) {
							shortcutList.add(allOptions[0].substring(0, allOptions[0].length()-1) + Integer.parseInt(allOptions[0]));
							a = a+1;
						}
					}else {
						while (Integer.parseInt(allOptions[0]) >= Integer.parseInt(allOptions[1])) {
							shortcutList.add(allOptions[0].substring(0, allOptions[0].length()-1) + Integer.parseInt(allOptions[0]));
							a = a-1;
						}
					}
					
				}else {
					if(Character.toString(allOptions[0].charAt(allOptions[0].length()-1)).compareTo(allOptions[1]) <= 0){
						while(Character.toString(allOptions[0].charAt(allOptions[0].length()-1)).compareTo(allOptions[1]) <= 0) {
							shortcutList.add(allOptions[0].substring(0, allOptions[0].length()-1) + Character.toString(allOptions[0].charAt(allOptions[0].length()-1)));
							String.valueOf(Character.toString(allOptions[0].charAt(allOptions[0].length()-1)).charAt(0)+1);
									
							
							
						}
					}
					else {
						while(Character.toString(allOptions[0].charAt(allOptions[0].length()-1)).compareTo(allOptions[1]) >= 0) {
							shortcutList.add(allOptions[0].substring(0, allOptions[0].length()-1) + Character.toString(allOptions[0].charAt(allOptions[0].length()-1)));
							String.valueOf(Character.toString(allOptions[0].charAt(allOptions[0].length()-1)).charAt(0)-1);
						}
					}
				}
			}else {
				shortcutList.add(shortcutsList[i]);
			}
			i = i+1;
		}
		
		int j = 0;
		int shortcutNumber = 0;
		while(optionsList.length > j) {
			String currentType;
			if(typesList.length <= j) {
				currentType = typesList[typesList.length -1];
			}else {
				currentType = typesList[i].trim();
			}
			if(optionsList[j].contains("-")) {
				String[] allOptions = shortcutsList[i].split("-");
				
				if(!isNumber(allOptions[1]) || isCharacter(allOptions[0]) 
						|| (isNumber(allOptions[0]) && isCharacter(allOptions[1]))
						|| (isNumber(allOptions[1]) && isCharacter(allOptions[1]))
						|| Character.isUpperCase(allOptions[0].charAt(0)) && Character.isLowerCase(allOptions[1].charAt(0))
						|| Character.isUpperCase(allOptions[1].charAt(0)) && Character.isLowerCase(allOptions[0].charAt(0))
						)
				{
						j = j+1;
						continue;
				}
				else if(isNumber(Character.toString(allOptions[0].charAt(allOptions[0].length()-1)))) {
					int a = Integer.parseInt(Character.toString(allOptions[0].charAt(allOptions[0].length()-1) ));
					if(Integer.parseInt(Character.toString(allOptions[0].charAt(allOptions[0].length()-1))) <= Integer.parseInt(allOptions[1])) {
						while(Integer.parseInt(Character.toString(allOptions[0].charAt(allOptions[0].length()-1) ) )<= Integer.parseInt(allOptions[1])) {
							Option currentOption = new Option(allOptions[0].substring(0, allOptions[0].length()-1) + Character.toString(allOptions[0].charAt(allOptions[0].length()-1)), Type.NOTYPE);
							
							if(currentType.equals("String")) {currentOption.setType(Type.STRING);}							
							else if(currentType.equals("Integer")) {currentOption.setType(Type.INTEGER);}							
							else if(currentType.equals("Character")) {currentOption.setType(Type.CHARACTER);}
							else if(currentType.equals("Boolean")) {currentOption.setType(Type.BOOLEAN);}
							else {
								throw new IllegalArgumentException();
							}
							try {
								if(i >= shortcutList.size()) {addOption(currentOption);}
								else {
									addOption(currentOption, shortcutList.get(shortcutNumber));
									shortcutNumber++;
								}
							}
							catch(Exception e) {
								i++;
								continue;
							}
							a = a + 1; 
						}
					}
					else{
						
						while(Integer.parseInt(Character.toString(allOptions[0].charAt(allOptions[0].length()-1))) >= Integer.parseInt(allOptions[1])) {
							Option currentOption = new Option(allOptions[0].substring(0, allOptions[0].length()-1) + Character.toString(allOptions[0].charAt(allOptions[0].length()-1)), Type.NOTYPE);
							if(currentType.equals("String")) {currentOption.setType(Type.STRING);}							
							else if(currentType.equals("Integer")) {currentOption.setType(Type.INTEGER);}							
							else if(currentType.equals("Character")) {currentOption.setType(Type.CHARACTER);}
							else if(currentType.equals("Boolean")) {currentOption.setType(Type.BOOLEAN);}
							
							else {
								throw new IllegalArgumentException();
							}
							try {
								if(i >= shortcutList.size()) {
									addOption(currentOption);
								}
								else {
									addOption(currentOption, shortcutList.get(shortcutNumber));
									shortcutNumber = shortcutNumber+1;
								}
							}
							catch(Exception e) {
								i++;
								continue;
							}
							a = a-1; 
						}
					}
				}
				
				else { 
					
					if(Character.toString(allOptions[0].charAt(allOptions[0].length()-1)).compareTo(allOptions[1]) <= 0) {
						while(Character.toString(allOptions[0].charAt(allOptions[0].length()-1)).compareTo(allOptions[1]) <= 0) {
							Option currentOption = new Option(allOptions[0].substring(0, allOptions[0].length()-1) + Character.toString(allOptions[0].charAt(allOptions[0].length()-1)), Type.NOTYPE);
							if(currentType.equals("String")) {currentOption.setType(Type.STRING);}							
							else if(currentType.equals("Integer")) {currentOption.setType(Type.INTEGER);}							
							else if(currentType.equals("Character")) {currentOption.setType(Type.CHARACTER);}
							else if(currentType.equals("Boolean")) {currentOption.setType(Type.BOOLEAN);}
							else {
								throw new IllegalArgumentException();
							}
							try {
								if(i >= shortcutList.size()) {
									addOption(currentOption);
								}
								else {
									addOption(currentOption, shortcutList.get(shortcutNumber));
									shortcutNumber++;
								}
							}
							catch(Exception e) {
								i++;
								continue;
							}
							String.valueOf( (char) (Character.toString(allOptions[0].charAt(allOptions[0].length()-1)).charAt(0) + 1));;
						}
					}
					
					else {
						while(Character.toString(allOptions[0].charAt(allOptions[0].length()-1)).compareTo(allOptions[1]) >= 0) {
							Option currentOption = new Option(allOptions[0].substring(0, allOptions[0].length()-1) + allOptions[1], Type.NOTYPE);
							if(currentType.equals("String")) {currentOption.setType(Type.STRING);}							
							else if(currentType.equals("Integer")) {currentOption.setType(Type.INTEGER);}							
							else if(currentType.equals("Character")) {currentOption.setType(Type.CHARACTER);}
							else if(currentType.equals("Boolean")) {currentOption.setType(Type.BOOLEAN);}
							else {
								throw new IllegalArgumentException();
							}
							try {
								if(i >= shortcutList.size()) {
									addOption(currentOption);
								}
								else {
									addOption(currentOption, shortcutList.get(shortcutNumber));
									shortcutNumber++;
								}
							}
							catch(Exception e) {
								i++;
								continue;
							}
							String.valueOf((Character.toString(allOptions[0].charAt(allOptions[0].length()-1)).charAt(0) - 1));;
						}
					}
				}
			}
			else {
				
				Option currentOption = new Option(optionsList[i], Type.NOTYPE);
				if(currentType.equals("String")) {currentOption.setType(Type.STRING);}							
				else if(currentType.equals("Integer")) {currentOption.setType(Type.INTEGER);}							
				else if(currentType.equals("Character")) {currentOption.setType(Type.CHARACTER);}
				else if(currentType.equals("Boolean")) {currentOption.setType(Type.BOOLEAN);}
				else {
					throw new IllegalArgumentException();
				}
				try {
					if(i >= shortcutList.size()) {
						addOption(currentOption);
					}
					else {
						addOption(currentOption, shortcutList.get(shortcutNumber));
						shortcutNumber++;
					}
				}
				catch(Exception e) {
					i++;
					continue;
				}
			}
			i++;
		}
		
	}
					

		
	

	private boolean isCharacter(String string) {
		// TODO Auto-generated method stub
		if(string == null)
			return false;
		for (int i = 0; i < string.length(); i++){
            char ch = string.charAt(i);
            if ((ch >= 'A' && ch <= 'Z') && (ch >= 'a' && ch <= 'z')) {
                return true;
            }
        }
       
		return false;
	}

	private boolean isNumber(String string) {
		// TODO Auto-generated method stub
		{
	        if (string == null) 
	        	return false;
	 
	        for (int i = 0; i < string.length(); i++)
	        {
	            char ch = string.charAt(i);
	            if ((ch >= '0' && ch <= '9')) {
	                return true;
	            }
	        }
	        return false;
	    }
		
	}

	public void addAll(String options, String types) throws Exception {
		// TODO Auto-generated method stub
	}

	public void addAll(String options) {
		// TODO Auto-generated method stub
		
		
	}

	
	private class CustomPair {
		
		CustomPair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
	    private int x;
	    private int y;
	    
	    public int getX() {
	    	return this.x;
	    }
	    
	    public int getY() {
	    	return this.y;
	    }
	}

}