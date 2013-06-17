package com.moodmedia.storeportal.zimbra.connection;

import com.moodmedia.storeportal.zimbra.url.AUrl;
import com.moodmedia.storeportal.zimbra.url.ChainEmailUrl;
import com.moodmedia.storeportal.zimbra.url.EmailUrl;
import com.moodmedia.storeportal.zimbra.url.InboxUrl;

/**
 * @author Ancuta Gheorghe
 *
 */
public class CustomConnection {

	private static final int INBOX = 1;
	private static final int EMAIL = 2;
	private static final int CHAINEMAIL = 3;
	private AUrl url;
	private CustomRequest customRequest;

	public CustomConnection(CustomRequest customRequest) {
		setCustomRequest(customRequest);
		setUrl(customRequest.getType());
	}
	
	public AUrl getUrl() {
		return url;
	}
	
	public void setUrl(int requestType) {
		switch (requestType){
        case INBOX:
            url = new InboxUrl(getCustomRequest());
            break;
        case EMAIL:
            url = new EmailUrl(getCustomRequest());
            break;
        case CHAINEMAIL:
            url = new ChainEmailUrl(getCustomRequest());
            break;
        default:
            throw new IllegalArgumentException("Incorrect URL Code");
		}
	} 
	
	public CustomRequest getCustomRequest() {
		return customRequest;
	}

	public void setCustomRequest(CustomRequest customRequest) {
		this.customRequest = customRequest;
	}
	
}
