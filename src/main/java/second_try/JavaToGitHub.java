package second_try;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.CannotDeleteCurrentBranchException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NotMergedException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class JavaToGitHub {
	Git git;
	public JavaToGitHub(String file) throws InvalidRemoteException, TransportException, GitAPIException {
		Configurations config = new Configurations(file);
		File dir = new File(config.getLocal());
		if(dir.exists()) {
			git = Git.init().setDirectory(dir).call();
		}
		else {
			git = Git.cloneRepository()
					.setURI(config.getRepository())
					.setDirectory(new File(config.getLocal())) 
					.call();
		}
		
	}
	
	
	
	
	
	public String createBranch(String email){
		String time = LocalDateTime.now().toString().replace(":","_");
		String new_branch_name = email+"@"+time;
		try {
			git.branchCreate().setName(new_branch_name).call();
			git.checkout().setName(new_branch_name).call();
			git.branchList().setListMode(ListMode.ALL).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
			return null;
		}
		return new_branch_name;
	}
	
	public String makeContribuition(String email, File file, String message) throws NoFilepatternException, GitAPIException, URISyntaxException {
		String branch = createBranch(email);
		git.add().addFilepattern(file.getName()).call();
		git.commit().setMessage(message).call();
		RemoteAddCommand remote = git.remoteAdd();
		remote.setName(branch);
		remote.setUri(new URIish("aqui vai o url do github"));
		remote.call();
		PushCommand push = git.push();
		CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("aqui vai o token de alguem q contribui para o repositorio", "");
		push.setCredentialsProvider(credentialsProvider);
		push.call();
		return "ack!!!";
	}
	
	
	
	
	public String deleteBranch(String branch) throws NotMergedException, CannotDeleteCurrentBranchException, GitAPIException {
		git.checkout().setName("main").call();
		git.branchDelete().setBranchNames(branch).call();
		return "done!";
	}
	
	
	
	
	public void listBranches() throws GitAPIException {
		git.checkout().setName("main").call();
		git.branchList().setListMode(ListMode.ALL).call();
		List<Ref> listRefsBranches = git.branchList().setListMode(ListMode.ALL).call();
		for (Ref refBranch : listRefsBranches) {
		System.out.println("Branch : " + refBranch.getName());
		}
	}
	
	
	
	
	
	public void updateLocal() throws CheckoutConflictException, GitAPIException {
		//git.reset().setRef("refs/remotes/origin/main").setMode(ResetType.HARD).call();
		PullCommand pullCmd = git.pull();
		PullResult result = pullCmd.call();
		FetchResult fetchResult = result.getFetchResult();
		MergeResult mergeResult = result.getMergeResult();
		mergeResult.getMergeStatus();
	}
	
	public static void main(String[] args) throws InvalidRemoteException, TransportException, GitAPIException, IOException, URISyntaxException {
		JavaToGitHub j2g = new JavaToGitHub("config.ini");
		j2g.listBranches();
		System.out.println(j2g.git.getRepository().getBranch());
		//j2g.makeContribuition("adsprojet01@gmail.com", new File("ADS.owl"), "this???");
		j2g.updateLocal();
		System.out.println("done?");
	}
}
