package vendor.objects;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import vendor.interfaces.Utils;
import vendor.modules.Logger;
import vendor.modules.Logger.LogType;

public class Dictionary implements Utils {
	
	private static final String DEFAULT_LANG = "en";
	private static final String DEFAULT_COUNTRY = "US";
	
	private static final String DEFAULT_DIRECTORY = "lang";
	private static final String DEFAULT_FILE_NAME = "strings";
	
	private static final String ROOT_CHAR = "/";
	
	private ResourceBundle resources;
	private Locale locale;
	private String currentResourcesPath;
	
	private String directory;
	private String fileName;
	
	public Dictionary(){
		this.directory = DEFAULT_DIRECTORY;
		this.fileName = DEFAULT_FILE_NAME;
		this.resources = getDefaultLanguageResources();
	}
	
	private String getDirectory(){
		return this.directory;
	}
	
	private void setDirectory(String directory){
		if(!getDirectory().equals(directory))
			this.directory = directory;
	}
	
	private String getFileName(){
		return this.fileName;
	}
	
	private void setFileName(String fileName){
		if(!getFileName().equals(fileName))
			this.fileName = fileName;
	}
	
	private String getResourcePath(){
		String resourcePath = "";
		
		if(getDirectory() != null || getDirectory().length() != 0)
			resourcePath += getDirectory() + ".";
		
		resourcePath += getFileName();
		
		return resourcePath;
	}
	
	public void setLanguage(String lang, String country){
		
		if(isDebugging())
			ResourceBundle.clearCache();
		
		locale = new Locale(lang, country);
		this.resources = getLanguageResources(locale);
		
	}
	
	public String getDirectString(String key, Object... replacements){
		return getString(key, null, replacements);
	}
	
	public String getDirectString(String key){
		return getString(key, null);
	}
	
	public String getString(String key, String possiblePrefix,
			Object... replacements){
		return format(this.getString(key, possiblePrefix), replacements);
	}
	
	public String getString(String key, String possiblePrefix){
		
		if(key == null){
			throw new IllegalArgumentException(
					"The \"key\" parameter cannot be null!");
		}
		
		key = handleKey(key);
		
		handleResourcesUpdate();
		
		String string = null;
		
		try{
			try{
				
				string = tryGetString(resources, key, possiblePrefix);
				
				if(string == null || string.length() == 0)
					throw new NullPointerException();
				
			}
			catch(MissingResourceException e){
				
				string = tryGetString(getDefaultLanguageResources(), key,
						possiblePrefix);
				
				if(isDebugging())
					Logger.log(
							"Key \""
									+ key
									+ "\" is missing in the resource file for the language \""
									+ locale.getLanguage() + "_"
									+ locale.getCountry() + "\".",
							LogType.WARNING);
				
			}
			catch(NullPointerException e){
				
				string = tryGetString(getDefaultLanguageResources(), key,
						possiblePrefix);
				
				if(isDebugging())
					Logger.log(
							"Key \""
									+ key
									+ "\" is empty in the resource file for the language \""
									+ locale.getLanguage() + "_"
									+ locale.getCountry() + "\".",
							LogType.WARNING);
				
			}
		}
		catch(MissingResourceException e){
			
			Logger.log("Key \"" + key
					+ "\" is not in any resource file - what's up with that?",
					LogType.WARNING);
			
		}
		
		return string;
		
	}
	
	private String handleKey(String key){
		
		if(!key.startsWith(ROOT_CHAR) && !key.contains(".")){
			
			setDirectory(DEFAULT_DIRECTORY);
			setFileName(DEFAULT_FILE_NAME);
			
			return key;
			
		}
		else{
			
			StringBuilder builder = new StringBuilder();
			
			if(!key.startsWith(ROOT_CHAR)){
				builder.append(DEFAULT_DIRECTORY).append(".");
			}
			else{
				key = key.substring(1);
			}
			
			String simplifiedKey;
			
			String[] structure = key.split("\\.");
			
			if(structure.length == 1){
				
				setFileName(DEFAULT_FILE_NAME);
				simplifiedKey = key;
				
			}
			else{
				
				// Don't set the directory structure for either the file name and the key.
				for(int i = 0; i < structure.length - 2; i++){
					
					if(i != 0){
						builder.append(".");
					}
					
					builder.append(structure[i]);
					
				}
				
				setFileName(structure[structure.length - 2]);
				
				simplifiedKey = structure[structure.length - 1];
				
			}
			
			setDirectory(builder.toString());
			
			return simplifiedKey;
			
		}
		
	}
	
	private String tryGetString(ResourceBundle resources, String key,
			String possiblePrefix) throws MissingResourceException{
		
		String string;
		
		try{
			string = resources.getString(key);
		}
		catch(MissingResourceException e){
			
			if(possiblePrefix == null || possiblePrefix.length() == 0)
				throw e;
			
			string = resources.getString(possiblePrefix + key);
			
		}
		
		return string;
		
	}
	
	private void handleResourcesUpdate(){
		
		if(!getResourcePath().equals(currentResourcesPath)){
			
			this.resources = getLanguageResources(this.locale);
			
			this.currentResourcesPath = getResourcePath();
			
		}
		
	}
	
	private ResourceBundle getDefaultLanguageResources(){
		return getLanguageResources(DEFAULT_LANG, DEFAULT_COUNTRY);
	}
	
	private ResourceBundle getLanguageResources(String lang, String country){
		this.locale = new Locale(lang, country);
		
		return getLanguageResources(this.locale);
	}
	
	private ResourceBundle getLanguageResources(Locale locale){
		return ResourceBundle.getBundle(this.getResourcePath(), locale);
	}
	
}
