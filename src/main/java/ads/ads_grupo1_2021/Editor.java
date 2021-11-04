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
 * @version 0.1
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
	 * Creates a new branch in the repository
	 * @param email to be associated with the new branch
	 * @return the name of the new branch (email@localDateTime with : replaced by _)
	 * @throws IOException
	 * @throws InterruptedException
	 * @version 0.1
	 */
	private String createBranch(String email) throws IOException, InterruptedException {
		String time = LocalDateTime.now().toString().replace(":","_");
		String new_branch_name = email+"@"+time;
        Map<String, String> createBranchMap = Map.of(
                "ref", "refs/heads/"+new_branch_name,
                "sha", github.getMasterBranchSHA());
        String requestBody = github.objectMapper.writeValueAsString(createBranchMap);
        github.post("/git/refs", requestBody);
        return new_branch_name;
    }
	
	
	
	
	
	
	/**
	 * The site can call another function that received all of this,
	 * Sends the content to an OWL API function so it can add the changes to the file
	 * Once that is done, this function is called
	 * There's not much of a reason to have this and the updateFile separately, i think
	 * @param file String name of the file that the editor wants to update (if we only have 1 KB then this doesn't need to be passed by the site)
	 * @param content String of the content that is going to be added to the file (by rewrite)
	 * @param message String message to be associated to the commit
	 * @param email String email that's going to be used to name the branch and for the editor to know who to send feedback about the commit
	 * @throws IOException
	 * @throws InterruptedException
	 * @version 0.1
	 */
	public void makeContribuition(String file, String content, String message, String email) throws IOException, InterruptedException {
		updateFile(file, content, message, createBranch(email));
	}
	
	
	
	
	
	
	/**
	 * This is the method that actually makes an upload to the repository
	 * @param file String name of the file that the editor wants to update (if we only have 1 KB then this doesn't need to be passed by the site)
	 * @param message String message to be associated to the commit
	 * @param content content String of the content that is going to be added to the file (by rewrite)
	 * @param branch String the branch the update will be placed in
	 * @throws IOException
	 * @throws InterruptedException
	 * @version 0.1
	 */
	private void updateFile(String file, String message, String content, String branch) throws IOException, InterruptedException {
		String sha = github.getFileSHA(file);
		String encodedContent = java.util.Base64.getEncoder().encodeToString(content.getBytes());
		Map<String,String> createMap = Map.of(
				"content", encodedContent,
                "message", message,
                "branch", branch,
                "sha", sha);
        try {
        	String requestBody = github.objectMapper.writeValueAsString(createMap);
			github.put("/contents/"+file, requestBody);
			//maybe send an email to at least 1 curator?
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			// Should delete the branch if the update doesn't go right
			// Also warn the editor that it went wrong
		}
	}
	
	
	
	
	
	// For testing only
	public static void main(String[] args) {
		try {
			Editor editor = new Editor("config.ini");
			InputStream fileStream = new FileInputStream("ADS.owl");
			String content = new String(Objects.requireNonNull(fileStream).readAllBytes(), StandardCharsets.UTF_8);
			editor.makeContribuition("testing.txt","let's see if this works", content, "adsprojet01@gmail.com");
			System.out.println("hello?");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
