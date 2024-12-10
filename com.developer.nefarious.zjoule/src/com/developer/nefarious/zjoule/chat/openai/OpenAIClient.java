package com.developer.nefarious.zjoule.chat.openai;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.util.List;
import com.developer.nefarious.zjoule.auth.IAuthClient;
import com.developer.nefarious.zjoule.chat.IAIClient;
import com.developer.nefarious.zjoule.chat.IMessage;
import com.developer.nefarious.zjoule.memory.IMemoryDeployment;
import com.developer.nefarious.zjoule.memory.IMemoryResourceGroup;
import com.developer.nefarious.zjoule.models.Deployment;
import com.developer.nefarious.zjoule.models.Role;

public class OpenAIClient implements IAIClient<OpenAIMessage> {

	private IAuthClient auth;

	private HttpClient httpClient;
	
	private IMemoryResourceGroup memoryResourceGroup;
	
	private IMemoryDeployment memoryDeployment;
	
	private IOpenAIClientHelper helper;

	// @formatter:off
	public OpenAIClient(
			final IAuthClient authClient, 
			final IMemoryResourceGroup memoryResourceGroup,
			final IMemoryDeployment memoryDeployment,
			final IOpenAIClientHelper openAIClientHelper) {
		// @formatter:on
		auth = authClient;
		this.httpClient = HttpClient.newHttpClient();
		this.memoryResourceGroup = memoryResourceGroup;
		this.memoryDeployment = memoryDeployment;
		helper = openAIClientHelper;
	}

	@Override
	public IMessage chatCompletion(final List<OpenAIMessage> messages) throws IOException, InterruptedException {
		URI endpoint = createChatEndpoint();
		
		String token = auth.getAccessToken();
		String resourceGroup = memoryResourceGroup.load();
		
		BodyPublisher requestBody = helper.createRequestBody(messages);
		
		// @formatter:off
		HttpRequest request = HttpRequest.newBuilder()
			.uri(endpoint)
			.header("Authorization", "Bearer " + token)
			.header("AI-Resource-Group", resourceGroup)
			.POST(requestBody)
			.build();
		// @formatter:on
		
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		return helper.convertResponseToObject(response.body());
	}
	
	private URI createChatEndpoint() {
		String serviceUrl = auth.getServiceUrl();
		Deployment deployment = memoryDeployment.load();
		String endpointInStringFormat = serviceUrl + "/inference/deployments/" + 
				deployment.getId() + "/chat/completions?api-version=2023-05-15";
		return URI.create(endpointInStringFormat);
	}

	@Override
	public OpenAIMessage createMessage(final Role role, final String userPrompt) {
		return new OpenAIMessage(role, userPrompt);
	}

	@Override
	public void setMessageHistory(final List<IMessage> messages) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IMessage> getMessageHistory() {
		// TODO Auto-generated method stub
		return null;
	}

}
