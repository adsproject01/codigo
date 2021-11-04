package ads.ads_grupo1_2021;

import java.io.IOException;

public class Curator extends SiteUser{
	private Configurations ini;
	private GitHubRestAPI github;
	private String email;
	
	public Curator(String ini_name, String email) {
		this.ini = new Configurations(ini_name);
		this.email = email;
		if(!ini.isCurator(email)) {
			throw new IllegalArgumentException("Email not authorized");
		}
		github = new GitHubRestAPI(ini.getGitHubToken(), ini.getGitHubBaseUrl());
	}
	
	
	
	
	
	/*public String getBranches() throws IOException, InterruptedException {
		return github.getBranches();
	}*/
	
	
	
	
	
	public void deleteBranch(String branch) throws IOException, InterruptedException {
		github.delete("/git/refs/heads/"+branch);
	}
	
	
	
	
	
	
	public void mergeBranch(String branch) {}
	
	
	
	
	
	public static void main(String[] args) {
		Curator curator = new Curator("config.ini", "adsprojet01@gmail.com");
		/*try {
			//curator.deleteBranch("adsprojet01@gmail.com@2021-11-03T16_34_11.435296100");
			//System.out.println(curator.getBranches());
			System.out.println("done!");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
