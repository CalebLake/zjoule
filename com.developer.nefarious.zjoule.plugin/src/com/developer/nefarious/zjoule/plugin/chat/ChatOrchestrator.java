package com.developer.nefarious.zjoule.plugin.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.developer.nefarious.zjoule.plugin.chat.utils.EditorContentReader;
import com.developer.nefarious.zjoule.plugin.models.Role;

public class ChatOrchestrator implements IChatOrchestrator  {

	@Override
	public String getAnswer(final String userPrompt) {

		List<IChatMessage> messages = new ArrayList<>();

		// 0. Define which AI Client should be used
		IAIClient aiClient = AIClientFactory.getClient();
		if (aiClient == null) {
			return getIncompatibilityMessage();
		}

		// 1. Get chat history
		List<IChatMessage> messageHistory = aiClient.getMessageHistory();
		messages.addAll(messageHistory);

		// 2. Get content of the active window
		String baseInstructions = Instruction.getMessage();
		String editorContent = EditorContentReader.readActiveEditorContent();

		String systemInstructions = editorContent != null
		    ? baseInstructions + " Consider the following code as context: " + editorContent
		    : baseInstructions;

		IChatMessage systemMessage = aiClient.createMessage(Role.SYSTEM, systemInstructions);
		messages.add(systemMessage);

		// 3. Create message object
		IChatMessage userMessage = aiClient.createMessage(Role.USER, userPrompt);
		messages.add(userMessage);

		// 4. Get answer from LLM
		IChatMessage answer;
		try {
			answer = aiClient.chatCompletion(messages);
		} catch (IOException | InterruptedException e) {
			Thread.currentThread().interrupt();
			return "Error during the AI request execution";
		}

		// 5. Save the response into the chat history
		messages.remove(systemMessage);
		messages.add(answer);
		aiClient.setMessageHistory(messages);

		// 7. Return response in string format
		return answer.getMessage();
	}

	private String getIncompatibilityMessage() {
		return "The model you have selected is incompatible with the current "
			+ "operation. Please verify the model's configuration or choose a "
			+ "compatible alternative.";
	}

}
