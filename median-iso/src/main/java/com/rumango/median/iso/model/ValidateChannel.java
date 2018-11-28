package com.rumango.median.iso.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@XmlRootElement(name = "resultsData")
public class ValidateChannel {
	private String accountNumber;
	private long amount;
	private String channelID;
	private String status;
	private String trnasactiID;

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTrnasactiID() {
		return trnasactiID;
	}

	public void setTrnasactiID(String trnasactiID) {
		this.trnasactiID = trnasactiID;
	}

	@Override
	public String toString() {
		return "ValidateChannel [accountNumber=" + accountNumber + ", amount=" + amount + ", channelID=" + channelID
				+ ", status=" + status + ", trnasactiID=" + trnasactiID + "]";
	}
}
