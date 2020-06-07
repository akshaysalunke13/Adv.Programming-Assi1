
public class RentalRecord {

	private String recordID;
	private DateTime rentDate;
	private DateTime expectedReturnDate;
	private DateTime actualReturnDate;
	private float rentFee;
	private float lateFee;
	
	//Constructor to create new rental record.
	public RentalRecord(String recordID, DateTime rentDate, DateTime expectedReturn) {
		super();
		this.recordID = recordID;
		this.rentDate = rentDate;
		this.expectedReturnDate = expectedReturn;
	}


	public String getRecordID() {
		return recordID;
	}


	public DateTime getRentDate() {
		return rentDate;
	}


	public DateTime getExpectedReturnDate() {
		return expectedReturnDate;
	}


	public DateTime getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(DateTime actual_return) {
		this.actualReturnDate = actual_return;
	}

	public float getRentFee() {
		return rentFee;
	}

	public void setRentFee(float rent_fee) {
		this.rentFee = rent_fee;
	}

	public float getLateFee() {
		return lateFee;
	}

	public void setLateFee(float late_fee) {
		this.lateFee = late_fee;
	}

	public String toString() {
		
		String stringToReturn = recordID + ":" + rentDate.getFormattedDate() + ":" + expectedReturnDate.getFormattedDate() + ":";

		//Vehicle not yet returned. Hence add "none" to string.
		if (actualReturnDate != null) {
			//Formatting float decimal to 2 places.
			stringToReturn += actualReturnDate.getFormattedDate() + ":" + String.format("%.2f", rentFee) + ":"
					+ String.format("%.2f", lateFee) + "\n";
		} else {
			stringToReturn += "none:none:none\n";
		}
		return stringToReturn;
	}
	
	public String getDetails() {
		
		String stringToReturn = "Record ID:\t" + recordID + "\nRent Date:\t" + rentDate.toString() + "\nEstimated Return Date:\t" + expectedReturnDate.toString();
		if(actualReturnDate == null) {
			return stringToReturn;
		} else {
			stringToReturn = stringToReturn + "\nActual Return Date:\t" + actualReturnDate.toString() + "\nRental Fee:\t" + String.valueOf(rentFee) + "\nLate Fee:\t" + Float.toString(lateFee);
		}
		return stringToReturn;
	}

}
