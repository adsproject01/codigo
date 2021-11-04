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
import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class handles the communication to github.
 * It was made with the help of https://www.twilio.com/blog/improve-workflow-github-api-java
 * and https://docs.github.com/en/rest/reference/repos
 * <p>
 * Not counting the methods that get SHA, this is likely just REST functions rather than specific for github
 * @author Susana Polido
 * @version 0.2
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
	 * The sha allows us to identify branches/files/etc in github so we can update or delete them.
	 * It calls the get method of this class then turns the response into json and finds sha of the main branch
	 * @return a string with the sha of the main branch of the repository
	 * @throws IOException
	 * @throws InterruptedException
	 * @since 0.1
	 */
	public String getMasterBranchSHA() throws IOException, InterruptedException {
        String body = get("/git/refs/heads");
        String sha = objectMapper.readTree(body)
                .get(0)
                .get("object")
                .get("sha")
                .asText();
        return sha;
    }
    
	
	
	
	
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
    public String getFileSHA(String file) throws IOException, InterruptedException {
    	// I tried this with only 1 file in the repository and I think also with just 1 branch
    	// Further experiments are likely needed
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
     * Gets the specified file from the specified branch from the repository
     * @param file
     * @param branch
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String getFileFromBranch(String file, String branch) throws IOException, InterruptedException {
    	return get("/contents/"+file+"?ref="+branch);
    }
    
    public String getFileDownloadUrlFromBranch(String file, String branch) throws IOException, InterruptedException {
    	String body = getFileFromBranch(file, branch);
    	String url = objectMapper.readTree(body)
    							.get("download_url")
    							.asText();
    	return url;
    }
    
    
    
    
    
    
    // TODO: public String getBranch(String branch){}
    // TODO: get file x from branch y public String getFileFromBranch(String file, String Branch){}
    // TODO: merge branch x into main public String mergeBranch(String branch)
    // TODO: add version to commit/merge
	
    
    
    
    
    // Just for "testing" purposes, should be deleted
	public static void main(String[] args) throws IOException, InterruptedException {
		GitHubRestAPI boop = new GitHubRestAPI("Bearer  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", "https://api.github.com/repos/adsproject01/base_conhecimento");
		System.out.println(boop.getBranchesNames());
		//FileUtils.copyURLToFile(new URL(boop.getFileDownloadUrlFromBranch("testing.txt", "adsprojet01@gmail.com@2021-11-04T14_14_57.152409900")), new File("testing.txt"));
		InputStream input = new URL(boop.getFileDownloadUrlFromBranch("testing.txt", "adsprojet01@gmail.com@2021-11-04T14_14_57.152409900")).openStream();
		System.out.println(input);
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
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
	}
	

}
