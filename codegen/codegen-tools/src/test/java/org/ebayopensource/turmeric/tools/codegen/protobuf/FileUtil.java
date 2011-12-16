package org.ebayopensource.turmeric.tools.codegen.protobuf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtil {
	
	
public static String readProtoFileToString(File fileLocation){
		final Logger logger = Logger.getLogger("");
		
		StringBuffer sb = new StringBuffer();
		FileReader fr  = null;
		BufferedReader br =null;
		if(fileLocation != null){
			
			try {
				
				fr = new FileReader(fileLocation);
				br = new BufferedReader(fr);
				
				String line = null;
				do{
					line = br.readLine();
					if(line != null){
						
						sb.append(line.trim());
					
					}
						
				}while(line != null);
				
				
			} catch (FileNotFoundException e) {
				logger.log(Level.FINE, "", e);
			} catch (IOException e) {
				logger.log(Level.FINE, "", e);
			}finally{
				if(fr != null && br != null){
					
					try {
						fr.close();
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.log(Level.FINE, "", e);
					}
				}
				
			}
			
			
		}else{
			return null;
		}
		return sb.toString();
	}


public static List<String> readFileAsLines(File fileLocation){
	final Logger logger = Logger.getLogger("");
	
	List<String> lines = new ArrayList<String>();
	FileReader fr  = null;
	BufferedReader br =null;
	if(fileLocation != null){
		
		try {
			
			fr = new FileReader(fileLocation);
			br = new BufferedReader(fr);
			
			String line = null;
			do{
				line = br.readLine();
				if(line != null){
					
					lines.add(line.trim());
				
				}
					
			}while(line != null);
			
			
		} catch (FileNotFoundException e) {
		
			logger.log(Level.FINE, "", e);
		} catch (IOException e) {
			
			logger.log(Level.FINE, "", e);
		}finally{
			if(fr != null && br != null){
				
				try {
					fr.close();
					br.close();
				} catch (IOException e) {
					logger.log(Level.FINE, "", e);
				}
			}
			
		}
		
		
	}else{
		return null;
	}
	return lines;
}

}
