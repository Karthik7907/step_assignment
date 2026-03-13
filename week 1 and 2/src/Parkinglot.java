import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    String status;

    public ParkingSpot() {
        status = "EMPTY";
    }
}

class Parkinglot{

    private ParkingSpot[] table;
    private int capacity = 500;
    private int occupied = 0;
    private int totalProbes = 0;

    public Parkinglot() {

        table = new ParkingSpot[capacity];

        for (int i = 0; i < capacity; i++)
            table[i] = new ParkingSpot();
    }


    // Hash function
    private int hash(String plate) {

        return Math.abs(plate.hashCode()) % capacity;
    }


    // Park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].status.equals("OCCUPIED")) {

            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = "OCCUPIED";

        occupied++;
        totalProbes += probes;

        System.out.println("parkVehicle(\"" + plate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)");
    }


    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (!table[index].status.equals("EMPTY")) {

            if (table[index].licensePlate != null &&
                    table[index].licensePlate.equals(plate)) {

                long durationMillis =
                        System.currentTimeMillis() - table[index].entryTime;

                double hours = durationMillis / 3600000.0;

                double fee = hours * 5; // $5 per hour

                table[index].status = "DELETED";
                occupied--;

                System.out.printf(
                        "exitVehicle(\"%s\") → Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n",
                        plate, index, hours, fee);

                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found.");
    }


    // Parking statistics
    public void getStatistics() {

        double occupancy =
                (occupied * 100.0) / capacity;

        double avgProbes =
                occupied == 0 ? 0 : (double) totalProbes / occupied;

        System.out.println("\nParking Statistics");

        System.out.printf("Occupancy: %.2f%%\n", occupancy);

        System.out.printf("Avg Probes: %.2f\n", avgProbes);

        System.out.println("Peak Hour: 2-3 PM (sample)");
    }


    public static void main(String[] args) {

        Parkinglot lot = new Parkinglot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}