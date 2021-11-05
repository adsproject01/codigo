package ads.ads_grupo1_2021;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * This class holds methods that are exclusive to editors
 * <p>
 * Likely will have a lot of changes, this is more to explore what fields we might need on the html forms
 * @author Susana Polido
 * @version 0.2
 */
public class Editor extends SiteUser{
	private Configurations ini;
	private GitHubRestAPI github; // Bridge to the github repository
	
	/**
	 * 
	 * @param ini_name
	 * @version 0.1
	 */
	public Editor(String ini_name) {
		this.ini = new Configurations(ini_name);
		github = new GitHubRestAPI(ini.getGitHubToken(), ini.getGitHubBaseUrl());
	}	
	
	
	
	
	
	/**
	 * The site can call another function that received all of this,
	 * Sends the content to an OWL API function so it can add the changes to the file
	 * Once that is done, this function is called
	 * @param file String name of the file that the editor wants to update (if we only have 1 KB then this doesn't need to be passed by the site)
	 * @param content String of the content that is going to be added to the file (by rewrite)
	 * @param message String message to be associated to the commit
	 * @param email String email that's going to be used to name the branch and for the editor to know who to send feedback about the commit
	 * @throws IOException
	 * @throws InterruptedException
	 * @version 0.1
	 */
	public String makeContribuition(String file, String content, String message, String email) throws IOException, InterruptedException {
		return github.updateFile(file, content, message, github.createBranch(email));
	}
	
	
	
	
	
	
	
	
	
	// For testing only
	public static void main(String[] args) {
		/*try {
			Editor editor = new Editor("config.ini");
			System.out.println(editor.github.getMasterBranchSHA());
			InputStream fileStream = new FileInputStream("ADS2.owl");
			String content = new String(Objects.requireNonNull(fileStream).readAllBytes(), StandardCharsets.UTF_8);
			System.out.println(editor.makeContribuition("ADS.owl","let's see if this works", content, "adsprojet01@gmail.com"));
			System.out.println("done?");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
}
