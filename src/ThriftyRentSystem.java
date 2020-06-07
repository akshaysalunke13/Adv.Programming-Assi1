import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class ThriftyRentSystem {

	private static HashMap<String, Vehicle> rentalVehicles = new HashMap<String, Vehicle>(50);
	private static String vehicleID;
	private static Scanner sc = new Scanner(System.in);
	
	//Driver function.
	public void start() {

		int menuChoice = 0;

		do {

			System.out.println(
					"**** ThriftyRent SYSTEM MENU **** \nAdd vehicle:\t\t1\nRent vehicle:\t\t2\nReturn vehicle:\t\t3\nVehicle Maintenance:\t4\nComplete Maintenance:\t5\nDisplay All Vehicles:\t6\nExit Program:\t\t7\nEnter your choice: ");
			try {
				menuChoice = sc.nextInt();
				sc.nextLine();

				switch (menuChoice) {
				case 1: // Add vehicle
					int subChoice;
					System.out.println("Select vehicle to add:\n1.Car\n2.Van");
					subChoice = sc.nextInt();
					sc.nextLine();
					if (subChoice == 1 || subChoice == 2) {
						System.out.println("Enter Vehicle ID:");
						vehicleID = sc.nextLine();
						if(!rentalVehicles.containsKey(vehicleID)) {
						if (checkIdAndChoice(vehicleID, subChoice)) {
							if (addVehicle(vehicleID, subChoice))
								System.out.println("Added Successfully.");
							else
								System.out.println("Error adding vehicle!");
						} else {
							System.out.printf("Invalid ID format. Should start with %s\n", subChoice == 1 ? "C_" : "V_");
						}
					} else {
						System.out.println("ID already exists.");
					}
					} else {
						System.out.println("Wrong choice!");
					}
					break;
				case 2: // Rent Vehicles
					System.out.println("Vehicle ID:");
					vehicleID = sc.nextLine();
					String custId;
					int tempDays;
					if (rentalVehicles.containsKey(vehicleID)) {

						if (rentalVehicles.get(vehicleID).getStatus().contentEquals("Available")) {
							System.out.println("Customer ID:");
							custId = sc.nextLine();
							System.out.println("Rent Date (dd/mm/yyyy)");
							DateTime rent_date = acceptDate();
							if(rent_date != null) {
							System.out.println("How many days?");
							tempDays = sc.nextInt();
							sc.nextLine();
							
							if ((rentalVehicles.get(vehicleID)).rent(custId, rent_date, tempDays))
								System.out.printf("Vehicle %s is now rented by customer %s.\n", vehicleID, custId);
							else
								System.out.printf("Vehicle %s could not be rented.\n", vehicleID);
						}
						} else {
							System.out.printf("Vehicle %s already rented or under maintenance.\n", vehicleID);
						}

					} else {
						System.out.println("No such vehicle found.");
					}
					break;
				case 3: // Return vehicle
					System.out.println("Enter vehicle id to return:");
					vehicleID = sc.nextLine();
					System.out.println("Enter vehicle return date:");
					DateTime returnDate = acceptDate();
					if (returnDate != null) {
						if(rentalVehicles.containsKey(vehicleID)) {
					if (rentalVehicles.get(vehicleID).returnVehicle(returnDate)) {
						System.out.println(rentalVehicles.get(vehicleID).getDetails());
					} else {
						System.out.println("Vehicle couldn't be returned.");
					}
					} else {
						System.out.println("Vehicle not found.");
					}
				}

					break;
				case 4: // Start maintenance
					System.out.println("Vehicle ID:");
					vehicleID = sc.next();
					if(rentalVehicles.containsKey(vehicleID)) {
						if(rentalVehicles.get(vehicleID).performMaintenance()) {
							System.out.printf("Vehicle %s is now under maintenance.\n", vehicleID);
						} else {
							System.out.printf("Vehicle %s is already under maintenance or rented.It cannot be maintained.\n", vehicleID);
						}
							
					} else {
						System.out.println("No such vehicle found.");
					}
					
					break;
				case 5: //Complete maintenance
					System.out.println("Enter vehicle ID:");
					vehicleID = sc.nextLine();
					if (vehicleID.startsWith("V_") && rentalVehicles.containsKey(vehicleID)) {
						System.out.println("Maintenance completion date (dd/mm/yyyy):");
						DateTime return_date2 = acceptDate();
						if (return_date2 != null) {
							if(rentalVehicles.get(vehicleID).completeMaintenance(return_date2)) {
								System.out.printf("Vehicle %s has all maintenance completed and ready for rent.\n", vehicleID);
							} else {
								System.out.println("Vehicle not under maintenance.");
							}
						}
					} else if (vehicleID.startsWith("C_") && rentalVehicles.containsKey(vehicleID)) {
						if(rentalVehicles.get(vehicleID).completeMaintenance()) {
							System.out.printf("Vehicle %s has all maintenance completed and ready for rent.\n", vehicleID);
						} else {
							System.out.println("Vehicle not under maintenance.");
						}
					} else {
						System.out.printf("No vehicle with id %s found.\n", vehicleID);
					}
					
					break;
				case 6:
					for (Map.Entry<String, Vehicle> tempVehicle : rentalVehicles.entrySet()) {
						System.out.println(tempVehicle.getValue().getDetails());
					}
					break;
				case 7:
					System.out.println("Exiting . . .");
					break;
				default:
					System.out.println("Wrong Choice!");
					break;
				}
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("Number expected!");
			} catch (Exception e) {
				System.out.println("Error");
			}
		} while (menuChoice != 7);
	}

	private static boolean checkIdAndChoice(String vehicle_id2, int ch) {

		if (vehicle_id2.startsWith("C_") && ch == 1) {
			return true;
		} else if (vehicle_id2.startsWith("V_") && ch == 2) {
			return true;
		} else {
			return false;
		}
	}

	// Returns true if vehicle creation successful
	private static boolean addVehicle(String vId, int ch2) {
		// Add Car
		int year, seats;
		String model, make, status;

		System.out.println("Enter Manufacturing year:");
		year = sc.nextInt();
		sc.nextLine();
		System.out.println("Enter MAKE:");
		make = sc.nextLine();
		System.out.println("Enter MODEL:");
		model = sc.nextLine();
		status = selectStatus();

		if (vId.startsWith("C_") && ch2 == 1) {
			do {
				System.out.println("Enter number of seats:");
				seats = sc.nextInt();
				sc.nextLine();

				if (seats == 4 || seats == 7) {
					if (rentalVehicles.putIfAbsent(vId,
							new Car(vId, year, make, model, seats, status)) == null)
						return true;
					else {
						System.out.println("ID already exists.");
						return false;
					}

				} else {
					System.out.println("Invalid seats. Only 4 or 7 seats allowed.");
				}
			} while (seats != 4 || seats != 7);
			
		} else
		// Add Van
		if (vId.startsWith("V_") && ch2 == 2) {
			
			// Accept last maintenance date for Van
			System.out.println("Enter last maintenance date:");
			DateTime last_srv_date = acceptDate();
			
			if(last_srv_date != null) {
			if (rentalVehicles.putIfAbsent(vId,
					new Van(vId, year, make, model, 15, status, last_srv_date)) == null)
				return true;
			else {
				System.out.println("ID already exists.");
				return false;
			}
			}
		}

		// Return because id invalid
		return false;
	}

	//Method to accept date from user and return DateTime object.
	private static DateTime acceptDate() {
		
		int dd,mm,yyyy;
		try {
		String[] dateString = sc.nextLine().split("/");
		dd = Integer.parseInt(dateString[0]);
		mm = Integer.parseInt(dateString[1]);
		yyyy = Integer.parseInt(dateString[2]);
		return new DateTime(dd, mm, yyyy);
		} catch (Exception e) {
			System.out.println("Error ocurred accepting the date.");
		}
		return null;
	}

	//Method to select vehicle status
	private static String selectStatus() {
		int status;
		do {
			try {
			System.out.println("Select status of vehilce:\n1.Available\n2.Rented\n3.Under Maintenance");
			status = sc.nextInt();
			
			} catch (InputMismatchException e) {
				System.out.println("Number expected.");
				status = 0;
			} finally {
				sc.nextLine();
			}
			if (status == 1) {
				return "Available";
			} else if (status == 2) {
				return "Rented";
			} else if (status == 3) {
				return "Under Maintenance";
			}
		} while (status < 1 || status > 3);

		return "";
	}
}
