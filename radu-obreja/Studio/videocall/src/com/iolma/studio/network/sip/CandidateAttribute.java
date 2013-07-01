package com.iolma.studio.network.sip;

import javax.sdp.SdpException;

import org.ice4j.TransportAddress;
import org.ice4j.ice.Candidate;

import gov.nist.core.Separators;
import gov.nist.javax.sdp.fields.AttributeField;

public class CandidateAttribute extends AttributeField {

	private static final long serialVersionUID = -2875158220451231240L;
	public String name = "candidate";
    @SuppressWarnings("rawtypes")
	private Candidate candidate = null;

    
	public CandidateAttribute(@SuppressWarnings("rawtypes") Candidate candidate) {
        this.candidate = candidate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
    	
    }

    public boolean hasValue() {
        return true;
    };

    public String getValue() {
        StringBuffer buff= new StringBuffer();

        buff.append(candidate.getFoundation());
        buff.append(" ").append(candidate.getParentComponent().getComponentID());
        buff.append(" ").append(candidate.getTransport());
        buff.append(" ").append(candidate.getPriority());
        buff.append(" ").append(candidate.getTransportAddress().getHostAddress());
        buff.append(" ").append(candidate.getTransportAddress().getPort());
        buff.append(" typ ").append(candidate.getType());

        TransportAddress relAddr = candidate.getRelatedAddress();

        if(relAddr != null) {
            buff.append(" raddr ").append(relAddr.getHostAddress());
            buff.append(" rport ").append(relAddr.getPort());
        }

        return buff.toString();
    }

    public void setValue(String value) throws SdpException {

    }

    public char getTypeChar() {
        return 'a';
    }

    public CandidateAttribute clone() {
        return this;
    }

     public String encode() {
         StringBuffer sbuff = new StringBuffer(ATTRIBUTE_FIELD);
         sbuff.append(getName()).append(Separators.COLON);
         sbuff.append(getValue());
         return sbuff.append(Separators.NEWLINE).toString();
     }
     
}
