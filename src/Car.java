
public class Car extends Vehicle {
	
	// Rental rate fixed constants.
	private final float SEAT_4_RENTAL = 78;
	private final float SEAT_7_RENTAL = 113;

	//Public constructor to create new Car object
	public Car(String id, int mfd_year, String make, String model, int seat_capacity, String status) {
		super(id, mfd_year, make, model, seat_capacity, status);
	}
	
	//Method to rent a car. Return true if successful to rent otherwise returns false
	public boolean rent (String custID, DateTime rentDate, int numOfDays) {
		
		//Check if rent duration is less than 14 as specified in assignment.
		if (numOfDays <= 14 && this.getStatus().contentEquals("Available")) {
			
		//Rent duration should be 2 or more days if renting during Monday - Thursday
		if((rentDate.getNameOfDay().contentEquals("Sunday") || rentDate.getNameOfDay().contentEquals("Monday") || rentDate.getNameOfDay().contentEquals("Tuesday") || rentDate.getNameOfDay().contentEquals("Wednesday") || rentDate.getNameOfDay().contentEquals("Thursday")) && numOfDays >= 2) {
			this.setCurrentRecord(new RentalRecord(this.getVehicleID() + "_" + custID, rentDate, new DateTime(rentDate, numOfDays)));
			this.setStatus("Rented");
			return true;
		} else if ((rentDate.getNameOfDay().contentEquals("Friday") || rentDate.getNameOfDay().contentEquals("Saturday")) && numOfDays >= 3) {
			this.setCurrentRecord(new RentalRecord(this.getVehicleID() + "_" + custID, rentDate, new DateTime(rentDate, numOfDays)));
			this.setStatus("Rented");
			return true;
		} else {
			return false;
		}
		} else {
			// Rent days cannot be greater than 14
			return false;
		}
	}

	//Method to return car. Returns true if car return successful otherwise false
	@Override
	public boolean returnVehicle(DateTime returnDate) {

		float rentFee, lateFee;
		if(this.getStatus().contentEquals("Rented")) {
			float perDay = this.getNoOfSeats() == 4 ? SEAT_4_RENTAL : SEAT_7_RENTAL ;
			
			if (DateTime.diffDays(returnDate, this.getCurrentRecord().getRentDate()) > 0) {
				
			if(DateTime.diffDays(returnDate, getCurrentRecord().getExpectedReturnDate()) > 0) {
				
				rentFee = (float) ((DateTime.diffDays(getCurrentRecord().getExpectedReturnDate(), getCurrentRecord().getRentDate()) * perDay));
				lateFee = (float) (DateTime.diffDays(returnDate, getCurrentRecord().getExpectedReturnDate()) * perDay * 1.25) ;
				
				this.updateHistory(returnDate, rentFee, lateFee);
				this.setStatus("Available");
				return true;
			} else 
				{
				rentFee = (DateTime.diffDays(returnDate, getCurrentRecord().getRentDate()) * perDay);
				lateFee = 0;
				
				this.updateHistory(returnDate, rentFee, lateFee);
				this.setStatus("Available");
				return true;
			}
		} else {

			//Return date cannot be before rent date
			return false;
		}
		} else
			// Car not rented to anyone.
		return false;
	}

}