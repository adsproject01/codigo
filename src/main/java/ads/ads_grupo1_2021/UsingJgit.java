package ads.ads_grupo1_2021;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.ServiceUnavailableException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class UsingJgit {
	public static void main(String[] args){
		/*try {
			File workingDirectory = File.createTempFile("nuxeo-git-test", "");
			workingDirectory.delete();
			workingDirectory.mkdirs();
			
			// Create a Repository object
			Repository repo = FileRepositoryBuilder.create(new File(workingDirectory, ".git"));
			repo.create();
			Git git = new Git(repo);
			
			// Create a new file and add it to the index
			File newFile = new File(workingDirectory, "myNewFile");
			newFile.createNewFile();
			git.add().addFilepattern("myNewFile").call();

			// Now, we do the commit with a message
			RevCommit rev = git.commit().setAuthor("gildas", "gildas@example.com").setMessage("My first commit").call();
		} catch (IOException | GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*try {
			Git git = Git.cloneRepository()
			.setURI("https://github.com/adsproject01/base_conhecimento.git")
			.setDirectory(new File("/ack/to/targetdirectory")) 
			.call();
			System.out.println(git.lsRemote());
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		File file = new File("/ack/to/targetdirectory");
		System.out.println(file.getAbsolutePath());
	}
}
