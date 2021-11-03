package ads.ads_grupo1_2021;

import java.io.IOException;

public class Curator extends SiteUser{
	private Configurations ini;
	private GitHubRestAPI github;
	private String email;
	
	public Curator(String ini_name, String email) {
		this.ini = new Configurations(ini_name);
		this.email = email;
		github = new GitHubRestAPI(ini.getGitHubCurator(email), ini.getGitHubBaseUrl());
		//TODO exception/warning if the curator email isn't found
	}
	
	
	
	
	
	public void getBranches() {}
	
	
	
	
	
	
	public void deleteBranch(String branch) throws IOException, InterruptedException {
		github.delete("/git/refs/heads/"+branch);
	}
	
	
	
	
	
	
	public void mergeBranch(String branch) {}
	
	
	
	
	
	public static void main(String[] args) {
		Curator curator = new Curator("config.ini", "adsprojet01@gmail.com");
		try {
			curator.deleteBranch("adsprojet01@gmail.com@2021-11-03T16_34_11.435296100");
			System.out.println("done!");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
