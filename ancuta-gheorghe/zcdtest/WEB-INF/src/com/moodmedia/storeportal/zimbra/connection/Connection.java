package com.moodmedia.storeportal.zimbra.connection;

import com.moodmedia.storeportal.zimbra.url.AUrl;
import com.moodmedia.storeportal.zimbra.url.ChainEmailUrl;
import com.moodmedia.storeportal.zimbra.url.EmailUrl;
import com.moodmedia.storeportal.zimbra.url.InboxUrl;

/**
 * @author ancuta
 *
 */
public class Connection {

	private static final int INBOX = 1;
	private static final int EMAIL = 2;
	private static final int CHAINEMAIL = 3;
	private AUrl url;
	private Request request;

	public Connection(Request connectionData) {
		setUrl(connectionData.getUrlType());
		setConnectionData(connectionData);
	}
	
	public AUrl getUrl() {
		return url;
	}
	
	public void setUrl(int urlType) {
		switch (urlType){
        case INBOX:
            url = new InboxUrl(getConnectionData());
            break;
        case EMAIL:
            url = new EmailUrl(getConnectionData());
            break;
        case CHAINEMAIL:
            url = new ChainEmailUrl(getConnectionData());
            break;
        default:
            throw new IllegalArgumentException("Incorrect URL Code");
		}
	} 
	
	public Request getConnectionData() {
		return request;
	}

	public void setConnectionData(Request connectionData) {
		this.request = connectionData;
	}
	
}
