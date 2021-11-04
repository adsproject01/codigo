package ads.ads_grupo1_2021;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ini4j.Config;
import org.ini4j.Ini;

/**
 * This class holds methods that extract the information necessary for communicating with
 * the many technologies picked to do the project so if any credentials/locations/etc change
 * it's not necessary to recompile/change the code
 * @author Susana Polido
 * @version 1.2
 */
public class Configurations {
	private Ini file;
	
	/**
	 * Creates Configurations object from the location/name of the ini file
	 * and sets it to allow multiple values for the same key
	 * @param file_name
	 * @since 1.2
	 */
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
	
	
	
	

	/**
	 * Looks up the passed value on the github section of the ini file
	 * @param key of the value we want to retrieve from the ini file
	 * @return the value the key returns
	 * @since 1.0
	 */
	private String getFromIni(String key) {
        return file.get("github", key);
        // we might want to make one that allows to specify the section too if we end up
        // adding more configurations to the file (like for the google site)
	}
	
	
	
	
	
	/**
	 * Gets the owner of the github repository that's stored in the ini file
	 * @return the owner of the repository
	 * @since 1.0
	 */
	public String getRepoOwner() {
		return getFromIni("owner");
	}
	
	
	
	
	/**
	 * Gets the name of the github repository that's stored in the ini file 
	 * @return the name of the repository on github
	 * @since 1.0
	 */
	public String getRepository() {
		return getFromIni("repository");
	}
	
	
	
	
	/**
	 * Gets the base url of the github repository by getting the owner and repository's names from the ini file
	 * @return the base url of the github repository
	 * @since 1.0
	 */
	public String getGitHubBaseUrl() {
		return "https://api.github.com/repos/" + getRepoOwner()+"/"+getRepository();
		// depending on what's necessary to communicate with the google site, we can edit this so it's more general
		//ex: that 1st string bit can also be added to the configurations, this way it can also be changed without
		// messing with code if github ever decides to change it
	}
	
	
	
	
	
	/**
	 * Gets the name of the github repository's main branch that's stored in the ini file 
	 * @return the name of the repository on github
	 * @since 1.0
	 */
	public String getRepoMainBranch() {
		return getFromIni("main_branch");
	}
	
	
	
	
	
	/**
	 * Gets the authentication key of a curator that is stored in the ini file so the curator can access the repository  
	 * @return the authentication key of the curator
	 * @since 1.2
	 */
	private List<String> getGitHubCurators() {
		Ini.Section section = file.get("github");
		return section.getAll("curator");
	}
	
	
	
	
	
	/**
	 * Checks if an email matches with a curator of the KB
	 * @param email String email of the curator
	 * @return if the sent email matches with the email of a curator
	 * @since 1.2
	 */
	public boolean isCurator(String email) {
		return getGitHubCurators().contains(email);
	}
	
	
	
	/**
	 * Gets the authentication key stored in the ini file to access the repository  
	 * @return the authentication key for the repository
	 * @since 1.2
	 */
	public String getGitHubToken() {
		return "Bearer " + getFromIni("key");
	}
	
	
	
	
	
	// Just for "testing" purposes, should be deleted
	public static void main(String[] args) {
		Configurations ini = new Configurations("config.ini");
		System.out.println(ini.getRepoOwner());
		System.out.println(ini.getRepository());
		System.out.println(ini.getRepoMainBranch());
		System.out.println(ini.isCurator("hello"));
		System.out.println(ini.isCurator("adsprojet01@gmail.com"));
		System.out.println(ini.getGitHubToken());
	}

}
