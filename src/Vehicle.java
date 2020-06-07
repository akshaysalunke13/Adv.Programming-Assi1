import java.util.ArrayList;

public abstract class Vehicle {

	//Unique vehicle ID
	private String vehicleID;
	private int mfdYear;
	private String make;
	private String model;
	private int noOfSeats;
	// Possible value - Available, Rented, Under Maintenance
	private String status;
	//Holds current incomplete rental record.
	private RentalRecord currentRecord;
	private DateTime lastMaintenanceDate;
	//Rental history of last 10 records.
	private ArrayList<RentalRecord> rentalHistory = new ArrayList<RentalRecord>(10);

	public Vehicle(String id, int mfdYear, String make, String model, int seats, String status) {
		this.vehicleID = id;
		this.mfdYear = mfdYear;
		this.make = make;
		this.model = model;
		this.noOfSeats = seats;
		this.status = status;
	}

	public DateTime getLastMaintenanceDate() {
		return lastMaintenanceDate;
	}

	public void setLastMaintenanceDate(DateTime date) {
		this.lastMaintenanceDate = date;
	}

	public RentalRecord getCurrentRecord() {
		return currentRecord;
	}

	public void setCurrentRecord(RentalRecord current) {
		this.currentRecord = current;
	}

	public String getVehicleID() {
		return vehicleID;
	}

	public int getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(int seat_capacity) {
		this.noOfSeats = seat_capacity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<RentalRecord> getRentalHistory() {
		return rentalHistory;
	}

	public boolean performMaintenance() {
		
		if (this.getStatus() == "Available") {
			this.setStatus("Under Maintenance");
			return true;
		}
		return false;
	}

	// Complete maintenance for Van
	public boolean completeMaintenance(DateTime date2) {

		if ((this instanceof Van) && (this.status.contentEquals("Under Maintenance"))) {
			this.setLastMaintenanceDate(date2);
			this.setStatus("Available");
			return true;
		} else {
			return false;
		}
	}

	// Complete maintenance for Car i.e without maintenance date
	public boolean completeMaintenance() {
		if ((this instanceof Car) && (this.status.contentEquals("Under Maintenance"))) {
			this.setLastMaintenanceDate(new DateTime());
			this.setStatus("Available");
			return true;
		} else {
			return false;
		}
	}

	//Update rent history with actual return date, calculated rental and late fees.
	public void updateHistory(DateTime returnDate, float rentFee, float lateFee) {
		
		this.getCurrentRecord().setRentFee(rentFee);
		this.getCurrentRecord().setActualReturnDate(returnDate);
		this.getCurrentRecord().setLateFee(lateFee);

		if (rentalHistory.size() >= 10) {
			rentalHistory.remove(0);

		}
		rentalHistory.add(currentRecord);
		this.setCurrentRecord(null);
	}

	public String toString() {
		String stringToReturn = this.vehicleID + ":" + this.mfdYear + ":" + this.make + ":" + this.model + ":" + this.noOfSeats
				+ ":" + this.status;
		if (this instanceof Van) {
			stringToReturn = stringToReturn + ":" + this.lastMaintenanceDate;
		}

		return stringToReturn;
	}

	public String getDetails() {
		String detailsToReturn = "";
		detailsToReturn = "Vehicle ID:\t\t" + this.vehicleID + "\n";
		detailsToReturn += "Year:\t\t\t" + this.mfdYear + "\n";
		detailsToReturn += "Make:\t\t\t" + this.make + "\n";
		detailsToReturn += "Model:\t\t\t" + this.model + "\n";
		detailsToReturn += "Number of seats:\t" + this.noOfSeats + "\n";
		detailsToReturn += "Status:\t\t\t" + this.status + "\n";

		if (this instanceof Van) {
			detailsToReturn += "Last maintenance date:\t" + this.lastMaintenanceDate.getFormattedDate() + "\n";
		}

		detailsToReturn += "RENTAL RECORD:\t";

		// Vehicle never rented
		if (currentRecord == null && rentalHistory.size() == 0) {
			detailsToReturn += "EMPTY\n";
		}

		// Vehicle currently rented.
		if (currentRecord != null) {
			detailsToReturn += "\n";
			detailsToReturn += "Record ID:\t" + this.currentRecord.getRecordID() + "\n";
			detailsToReturn += "Rent Date:\t" + this.currentRecord.getRentDate().getFormattedDate() + "\n";
			detailsToReturn += "Estimated return date:\t" + this.currentRecord.getExpectedReturnDate().getFormattedDate() + "\n";
		}

		//Vehicle not currently rented but has rent history.
		if (rentalHistory.size() != 0) {
			detailsToReturn += "\n--------------------------------------\n";
			for (int i = rentalHistory.size() - 1; i >= 0; i--) {
				detailsToReturn += "Record ID:\t\t" + this.rentalHistory.get(i).getRecordID() + "\n";
				detailsToReturn += "Rent Date:\t\t" + this.rentalHistory.get(i).getRentDate().getFormattedDate() + "\n";
				detailsToReturn += "Estimated return date:\t" + this.rentalHistory.get(i).getExpectedReturnDate().getFormattedDate()
						+ "\n";
				detailsToReturn += "Actual return date:\t" + this.rentalHistory.get(i).getActualReturnDate().getFormattedDate()
						+ "\n";
				detailsToReturn += "Rental fee:\t\t" + String.format("%.2f", this.rentalHistory.get(i).getRentFee()) + "\n";
				detailsToReturn += "Late fee:\t\t" + String.format("%.2f", this.rentalHistory.get(i).getLateFee()) + "\n";
			}
		}

		return detailsToReturn;
	}

	public abstract boolean rent(String customerId, DateTime rentDate, int numOfRentDay);

	public abstract boolean returnVehicle(DateTime returnDate);

}
