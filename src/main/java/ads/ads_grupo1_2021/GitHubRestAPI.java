package ads.ads_grupo1_2021;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class handles the communication to github.
 * It was made with the help of https://www.twilio.com/blog/improve-workflow-github-api-java
 * and https://docs.github.com/en/rest/reference/repos
 * <p>
 * Not counting the methods that get SHA, this is likely just REST functions rather than specific for github
 * @author Susana Polido
 * @version 0.1
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
		System.out.println("autorization: " + authorization);
		System.out.println("baseurl: " + baseUrl);
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
        System.out.println(body);
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
        String body = this.get("/contents/"+file);
        String sha = objectMapper.readTree(body)
                .get("sha")
                .asText();

        return sha;
    }
    
    
    
    
    
    // TODO: public String getBranch(String branch){}
	
    
    
    
    
    // Just for "testing" purposes, should be deleted
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

}
