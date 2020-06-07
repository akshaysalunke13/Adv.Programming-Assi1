
public class Van extends Vehicle {
	
	//Constant values for rental rate
	private final float RENTAL = 235;
	private final float LATE_RENTAL = 299;

	//Constructor to initialize Van object
	public Van(String id, int mfd_year, String make, String model, int seat_capacity, String status, DateTime last) {
		super(id, mfd_year, make, model, seat_capacity, status);
		this.setLastMaintenanceDate(last);

	}

	//Method to rent Van.
	public boolean rent(String customerId, DateTime rentDate, int numOfRentDay) {
		
		//Check if rental ends before next maintenance date.
		if ((numOfRentDay >= 1 && DateTime.diffDays(new DateTime(rentDate, numOfRentDay), new DateTime(this.getLastMaintenanceDate(), 12)) < 0) && this.getStatus().contentEquals("Available")) {
			this.setCurrentRecord(new RentalRecord(this.getVehicleID() + "_" + customerId, rentDate, new DateTime(rentDate, numOfRentDay)));
			this.setStatus("Rented");
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public boolean returnVehicle(DateTime returnDate) {
		float rental, late;
		
		//Proceed with returning only if Van already rented.
		if(this.getStatus().contentEquals("Rented")) {
			
			//Check if return date is after the actual rent date.
			if (DateTime.diffDays(returnDate, this.getCurrentRecord().getRentDate()) > 0) {
			
				// Customer returned Van late
			if(DateTime.diffDays(returnDate, getCurrentRecord().getExpectedReturnDate()) > 0) {
				
				rental = (float) ((DateTime.diffDays(getCurrentRecord().getExpectedReturnDate(), getCurrentRecord().getRentDate()) * RENTAL));
				late = (float) (DateTime.diffDays(returnDate, getCurrentRecord().getExpectedReturnDate()) * LATE_RENTAL * 1.25) ;
				
				this.updateHistory(returnDate, rental, late);
				this.setStatus("Available");
				return true;
				
			} else {
				
				//Customer returned Van on time. No late fees
				rental = (DateTime.diffDays(returnDate, getCurrentRecord().getRentDate()) * RENTAL);
				late = 0;
				
				this.updateHistory(returnDate, rental, late);
				this.setStatus("Available");
				return true;
			}
		} else {
			return false;
		}
		} else
		return false;
	}
	
}
