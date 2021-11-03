package ads.ads_grupo1_2021;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;

/**
 * This class holds methods that extract the information necessary for communicating with
 * the many technologies picked to do the project so if any credentials/locations/etc change
 * it's not necessary to recompile/change the code
 * @author Susana Polido
 * @version 1.1
 */
public class Configurations {
	private Ini file;
	
	/**
	 * Creates Configurations object from the location/name of the ini file
	 * @param file_name
	 * @since 1.0
	 */
	public Configurations(String file_name) {
		try {
			file = new Ini( new File(file_name));
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
	 * @param curator the (user)name of the curator
	 * @return the authentication key of the curator
	 * @since 1.1
	 */
	public String getGitHubCurator(String curator) {
		return "Bearer " + file.get("curators", curator);
	}
	
	
	
	
	
	/**
	 * Gets the authentication key stored in the ini file that non curators can use to access the repository  
	 * @return the authentication key of a non curator
	 * @since 1.0
	 */
	public String getGitHubNonCurator() {
		return "Bearer " + getFromIni("key");
	}
	
	
	
	
	
	// Just for "testing" purposes, should be deleted
	public static void main(String[] args) {
		Configurations ini = new Configurations("config.ini");
		System.out.println(ini.getRepoOwner());
		System.out.println(ini.getRepository());
		System.out.println(ini.getRepoMainBranch());
		System.out.println(ini.getGitHubCurator("curator_teste"));
		System.out.println(ini.getGitHubCurator("adsprojet01@gmail.com"));
		System.out.println(ini.getGitHubNonCurator());
	}

}
