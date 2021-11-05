package ads.ads_grupo1_2021;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.sqwrl.SQWRLResult;
import org.swrlapi.sqwrl.exceptions.SQWRLException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class handles the communication to github.
 * It was made with the help of https://www.twilio.com/blog/improve-workflow-github-api-java
 * and https://docs.github.com/en/rest/reference/repos
 * <p>
 * Not counting the methods that get SHA, this is likely just REST functions rather than specific for github
 * @author Susana Polido
 * @version 0.3
 */
public class GitHubRestAPI {
	private final String authorization;
	private final String baseUrl;
	public final ObjectMapper objectMapper;
	
	
	
	
	
	/**
	 * Creates a comunication bridge between a github repository and the application
	 * by accepting an authentication key and a github repository url
	 * @param authorization the authentication key to interact with the github
	 * @param baseUrl where the repository is located
	 * @since 0.1
	 */
	public GitHubRestAPI(String authorization, String baseUrl) {
		this.authorization = authorization;
		this.baseUrl = baseUrl;
		this.objectMapper =  new ObjectMapper();
	}
	
	
	
	
	
	/**
	 * Makes a get request to the github repository
	 * @param path the location of what we want to get after the baseUrl
	 * @return the github response body as a string
	 * @throws IOException
	 * @throws InterruptedException
	 * @since 0.1
	 */
	public String get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + path))
                .setHeader("Authorization", authorization)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        return response.body();
    }
	
	
	
	
		
	/**
	 * Makes a delete request to the github repository
	 * @param path the location of what we want to delete after the baseUrl
	 * @return the github response body as a string
	 * @throws IOException
	 * @throws InterruptedException
	 * @since 0.1
	 */
	public String delete(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + path))
                .setHeader("Authorization", authorization)
                .DELETE()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        return response.body();
    }
	
	
	
	
	
	/**
	 * Makes a post request to the github repository
	 * @param path the location of where we want to post after the baseUrl
	 * @param body what we want to post to the github
	 * @return the github response body as a string
	 * @throws IOException
	 * @throws InterruptedException
	 * @since 0.1
	 */
	public String post(String path, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + path))
                .setHeader("Authorization", authorization)
                .POST(BodyPublishers.ofString(body))
                .build();
        System.out.println(request);
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        return response.body();
    }
	
	
	
	
	
	/**
	 * Makes a put request to the github repository
	 * @param path the location of where we want to put after the baseUrl
	 * @param body what we want to put to the github
	 * @return the github response body as a string
	 * @throws IOException
	 * @throws InterruptedException
	 * @since 0.1
	 */
	public String put(String path, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + path))
                .setHeader("Authorization", authorization)
                .PUT(BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        return response.body();
    }
	
	
	
	
	
	/**
	 * This method allows us to get the sha of the main branch of the repository.
	 * <p>
	 * Should be changed so the name of the main branch is an argument
	 * The sha allows us to identify branches/files/etc in github so we can update or delete them.
	 * It calls the get method of this class then turns the response into json and finds sha of the main branch
	 * @return a string with the sha of the main branch of the repository
	 * @throws IOException
	 * @throws InterruptedException
	 * @since 0.3
	 */
	public String getMasterBranchSHA() throws IOException, InterruptedException {
        String body = get("/branches/main");
        String sha = objectMapper.readTree(body)
                .get("commit")
                .get("sha")
                .asText();
        return sha;
    }
	
	// Old version that didn't look at the main but rather at the latest branch
	/*public String getMasterBranchSHA() throws IOException, InterruptedException {
    	String body = get("/git/refs/heads");
    	String sha = objectMapper.readTree(body)
            .get(0)
            .get("object")
            .get("sha")
            .asText();
    	return sha;
	}*/
	
	
	

	// NEED TO SEE IF THIS CAN BE USED INSTEAD OF THE OTHER ONE
	/**
	 * This method allows us to get the sha of a file of the repository.
	 * <p>
	 * The sha allows us to identify branches/files/etc in github so we can update or delete them.
	 * It calls the get method of this class then turns the response into json and finds sha of the main branch
	 * @return a string with the sha of the main branch of the repository
	 * @throws IOException
	 * @throws InterruptedException
	 * @since 0.1
	 */
   
	public String getFileFromMainSHA(String file) throws IOException, InterruptedException {
        String body = get("/contents/"+file);
        System.out.println(body);
        String sha = objectMapper.readTree(body)
                .get("sha")
                .asText();

        return sha;
    }
    
    
    

    
    /**
     * Uses the get method to find all the branches of the repository
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @since 0.2
     */
    private List<String> getBranches() throws IOException, InterruptedException {
    	String body = get("/branches");
    	JsonNode arrNode = new ObjectMapper().readTree(body);
    	List<String> branches = new ArrayList<>();
    	if (arrNode.isArray()) {
    	    for (JsonNode objNode : arrNode) {
    	        branches.add(objNode.toString());
    	    }
    	}
        return branches;
	}
    
    
    
    
    
    /**
     * Calls the getBranches() method so it can get all the branches' names
     * @return a list of Strings with the names of the existing branches
     * @throws IOException
     * @throws InterruptedException
     * @since 0.2
     */
    public List<String> getBranchesNames() throws IOException, InterruptedException{
    	List<String> branches = getBranches();
    	List<String> names = new ArrayList<>();
    	for(String branch : branches) {
    		String name = objectMapper.readTree(branch)
    	                .get("name")
    	                .asText();
    		names.add(name);
    	}
    	return names;
    }
    
    
    
    
    
    /**
     * Gets a branch from the repository
     * <p>
     * Might be necessary to get the SHA of the branch
     * @param branch String name of the branch we want
     * @return the repository's get response
     * @throws IOException
     * @throws InterruptedException
     * @since 0.2
     */
    public String getBranch(String branch) throws IOException, InterruptedException {
    	return get("/branches/"+branch);
    }
    
    
    
   
    
    /**
     * Grabs a file from the specified branch
     * @param file String name of the file we want
     * @param branch String name of the branch where the file we want is
     * @return InputStream with the file
     * @throws IOException
     * @throws InterruptedException
     * @since 0.3
     */
    public InputStream getInputStreamFileFromBranch(String file, String branch) throws IOException, InterruptedException {
    	String body = get("/contents/"+file+"?ref="+branch);
    	String url = objectMapper.readTree(body)
				.get("download_url")
				.asText();
    	InputStream input = new URL(url).openStream();
    	return input;
    }
    public String deleteBranch(String branch) throws IOException, InterruptedException {
		return delete("/git/refs/heads/"+branch);
	}
    
    
   
	
    
    /**
     * Merges the second branch into the 1st branch
     * @param baseBranch String branch the other is going to be merged into
     * @param toMergeBranch String branch we want to merge
     * @return github's reply, good for debugging
     * @throws IOException
     * @throws InterruptedException
     * @since 0.3
     */
    public String mergeBranches(String baseBranch, String toMergeBranch) throws IOException, InterruptedException {
    	Map<String, String> createBranchMap = Map.of(
                "base", baseBranch,
                "head", toMergeBranch);
        String requestBody = objectMapper.writeValueAsString(createBranchMap);
    	return post("/merges", requestBody);
    }
    
    
    
    
    
    /**
     * @param tag_name String name of the new tag
     * @param message String message of the tag
     * @return github's reply, good for debugging
     * @throws IOException
     * @throws InterruptedException
     * @since 0.3
     */
    public String releaseVersion(String tag_name, String message) throws IOException, InterruptedException {
    	Map<String, String> createRelease = Map.of(
                "tag_name", tag_name,
                "target_commitish", getMasterBranchSHA(),
    			"body", message);
    	String body = objectMapper.writeValueAsString(createRelease);
    	return post("/releases",body);
    }
    
    
    
    
    
    /**
     * Useful to maybe figure out what the next version tag should be?
     * @return the last tag added to the repository
     * @throws JsonMappingException
     * @throws JsonProcessingException
     * @throws IOException
     * @throws InterruptedException
     * @since 0.3
     */
    public String getLatestTag() throws JsonMappingException, JsonProcessingException, IOException, InterruptedException {
    	String tag = objectMapper.readTree(getTags())
                .get(0)
                .get("name")
                .asText();
		return tag;
    }
    
    
    
    
    
    /**
     * Returns all the tags in the repository
     * Not really necessary
     * @return github's response at being asked the tags that exist in the repository
     * @throws IOException
     * @throws InterruptedException
     * @since 0.3
     */
    public String getTags() throws IOException, InterruptedException {
    	return get("/tags");
    }
    
    
    
    
    
    /**
	 * Creates a new branch in the repository
	 * <p>
	 * Previously was in the Editor class
	 * @param email to be associated with the new branch
	 * @return the name of the new branch (email@localDateTime with : replaced by _)
	 * @throws IOException
	 * @throws InterruptedException
	 * @version 0.3
	 */
	public String createBranch(String email) throws IOException, InterruptedException {
		String time = LocalDateTime.now().toString().replace(":","_");
		String new_branch_name = email+"@"+time;
        Map<String, String> createBranchMap = Map.of(
                "ref", "refs/heads/"+new_branch_name,
                "sha", getMasterBranchSHA());
        String requestBody = objectMapper.writeValueAsString(createBranchMap);
        post("/git/refs", requestBody);
        return new_branch_name;
    }
	
	
	
	
	
	/**
	 * This is the method that actually makes an upload to the repository
	 * <p>
	 * Previously was in the editor class
	 * @param file String name of the file that the editor wants to update (if we only have 1 KB then this doesn't need to be passed by the site)
	 * @param message String message to be associated to the commit
	 * @param content content String of the content that is going to be added to the file (by rewrite)
	 * @param branch String the branch the update will be placed in
	 * @throws IOException
	 * @throws InterruptedException
	 * @version 0.3
	 */
	public String updateFile(String file, String message, String content, String branch) throws IOException, InterruptedException {
		//String sha = getFileSHAFromBranch(file, "main");
		String sha = getFileFromMainSHA(file);
		String encodedContent = java.util.Base64.getEncoder().encodeToString(content.getBytes());
		Map<String,String> createMap = Map.of(
				"content", encodedContent,
                "message", message,
                "branch", branch,
                "sha", sha);
        String requestBody = objectMapper.writeValueAsString(createMap);
		return put("/contents/"+file, requestBody);
		//Maybe we should have a way to delete the branch if the content doesn't get uploaded?
	}
	
	
	
	
	
	/**
	 * Likely not needed, I made this cause of a mistake that has since been corrected (i hope)
	 * @param file String file we want
	 * @param branch String branch we want the file from
	 * @return String SHA of the file in the branch
	 * @throws IOException
	 * @throws InterruptedException
	 * @since 0.3
	 */
	public String getFileSHAFromBranch(String file, String branch) throws IOException, InterruptedException {
    	String body = get("/contents/"+file+"?ref="+branch);
    	String sha = objectMapper.readTree(body)
				.get("sha")
				.asText();
    	return sha;
    }

	
	

	
	/**
     * Was trying to tag the main branch without doing a release
     * WASN'T SUCCESSFUL
     * @param tag String name of the new tag
     * @param message String message of the tag
     * @return github's reply, good for debugging
     * @throws IOException
     * @throws InterruptedException
     * @since 0.3
     */
    public String tagMainBranch(String tag, String message) throws IOException, InterruptedException {
    	Map<String, String> createRelease = Map.of(
                "tag", tag,
                "message", message,
                "object", getMasterBranchSHA(),
    			"type", "branch");
    	String body = objectMapper.writeValueAsString(createRelease);
    	System.out.println(body);
    	return post("/git/tags",body);
    }
    
    
    
    
    
    // Just for "testing" purposes, should be deleted
	public static void main(String[] args) throws IOException, InterruptedException {
		//GitHubRestAPI boop = new GitHubRestAPI("Bearer  ", "");
		//System.out.println(boop.getBranchesNames());
		//System.out.println(boop.get("/branches/main"));
		//InputStream input = boop.getInputStreamFileFromBranch("ADS.owl", "adsprojet01@gmail.com@2021-11-05T11_33_52.923141100");
		
		//FileUtils.copyURLToFile(new URL(boop.getFileDownloadUrlFromBranch("testing.txt", "adsprojet01@gmail.com@2021-11-04T14_14_57.152409900")), new File("testing.txt"));
		//InputStream input = new URL(boop.getFileDownloadUrlFromBranch("testing.txt", "adsprojet01@gmail.com@2021-11-04T14_14_57.152409900")).openStream();
		//System.out.println(input);
		/*OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		try {
			OWLOntology ontology =  m.loadOntologyFromOntologyDocument(input);
			SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);
			System.out.println(ontology.toString());
			String numberOfObjectives = "2";
			String query = "Algorithm(?alg) ^ "   
			    	+ "minObjectivesAlgorithmIsAbleToDealWith(?alg,?min) ^ swrlb:lessThanOrEqual(?min,"+numberOfObjectives+")"
			    	+ "maxObjectivesAlgorithmIsAbleToDealWith(?alg,?max) ^ swrlb:greaterThanOrEqual(?max,"+numberOfObjectives+")"
			    	+ " -> sqwrl:select(?alg) ^ sqwrl:orderBy(?alg)";  
			SQWRLResult result = queryEngine.runSQWRLQuery("q1", query);
			System.out.println("Query: \n" + query + "\n");
			System.out.println("Result: ");
		    while (result.next()) {
		    	System.out.println(result.getNamedIndividual("alg").getShortName());
		    }
		} catch (OWLOntologyCreationException | SQWRLException | SWRLParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(boop.mergeBranches("main", "adsprojet01@gmail.com@2021-11-05T11_33_52.923141100"));*/
		//System.out.println(boop.releaseVersion("V0.3", "meep meep"));
		//System.out.println(boop.tagMainBranch("v0.02", "trying to tag without release"));
		//System.out.println(boop.getLatestTag());
	}
	

}
