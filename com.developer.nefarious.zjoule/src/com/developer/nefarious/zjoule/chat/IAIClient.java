package com.developer.nefarious.zjoule.chat;

import java.io.IOException;
import java.util.List;
import com.developer.nefarious.zjoule.models.Role;

public interface IAIClient {

	IChatMessage chatCompletion(final List<IChatMessage> messages) throws IOException, InterruptedException;
	
	IChatMessage createMessage(final Role role, final String userPrompt);
	
	List<IChatMessage> getMessageHistory();
	
	void setMessageHistory(final List<IChatMessage> messages);
	
}
