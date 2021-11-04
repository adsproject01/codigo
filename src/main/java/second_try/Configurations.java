package second_try;

import java.io.File;
import java.io.IOException;

import org.ini4j.Config;
import org.ini4j.Ini;

public class Configurations {
private Ini file;
	
	public Configurations(String file_name) {
		try {
			file = new Ini( new File(file_name));
			Config config = new Config();
			config.setMultiOption(true);
			file.setConfig(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	private String getFromIni(String key) {
        return file.get("jgit", key);
	}
	
	
	
	
	
	public String getRepository() {
		return getFromIni("repo");
	}
	
	
	
	
	
	public String getLocal() {
		return getFromIni("local");
	}
}
