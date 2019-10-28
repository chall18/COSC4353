import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

public class ControlRoom extends parking{

    private int lotNum;
    private double totalRevenue;

    parking comparePrices(Car a, parking lot1, parking lot2){
        //compares prices b/t two lots given how long the driver will stay and the distance the lot is from their venue
        double timeSpent = 2400 + a.timeExited - a.timeEntered;
        double hours = timeSpent/100;

        double priceForLot1 = lot1.price(a)*hours;
        double priceForLot2 = lot2.price(a)*hours;

        parking bestLot;
        if(priceForLot1 < priceForLot2){
            bestLot = lot1;
        }
        else{
            bestLot = lot2;
        }
        return bestLot;
    }

    void readParkingData(String file)
    {
        //read input file line by line, each Car attribute delimited by a comma
        //Path myPath = Paths.get(file);
        ParkingPolice p = new ParkingPolice();
        try
        {
            BufferedReader lotNumReader = new BufferedReader(new FileReader(file));
            String line = lotNumReader.readLine();
            lotNum = Integer.valueOf(line);//# of lots this file of data will be giving info on

            parking lot1 = new parking();//these two will only be used if we have > 1 lots, but need them outside of if statements for scope
            parking lot2 = new parking();
            parking lot = new parking();

            if(lotNum > 1){//lotNum is either going to be 1 or 2 for this

                lot1.lotName = "Lot 1";
                //instead of assigning lot capacities through the txt file, we're randomly assigning a # from 50-100 here
                Random r = new Random();
                lot1.setCapacity(r.nextInt((100-50)+1)+50);
                lot1.lotSetUp(lot1.getCapacity());

                lot2.lotName = "Lot 2";
                lot2.setCapacity(r.nextInt((100-50)+1)+50);
                lot2.lotSetUp(lot1.getCapacity());
            }
            else{
                lot.lotName = "Lot";
                Random r = new Random();
                lot.setCapacity(r.nextInt((100-50)+1)+50);
                lot.lotSetUp(lot.getCapacity());
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            line = reader.readLine();
            //System.out.println("Capacity for this lot: " + newCap);
            while((line = reader.readLine()) != null)
            {
                //System.out.println("Line: " + line);
                String []arr = line.split(",");
                Car newCar = new Car();//use the data to fill in the info for a new Car
                newCar.name = arr[0];
                newCar.status = arr[1];
                newCar.timeEntered = Double.parseDouble(arr[2]);
                newCar.timeExited = Double.parseDouble(arr[3]);
                newCar.iHaveTicket = Boolean.parseBoolean(arr[4]);
                newCar.distanceFromVenue = Integer.parseInt(arr[5]);
                newCar.wantsLatestPrices = Boolean.parseBoolean(arr[6]);
                newCar.dayType = arr[7];


                parking chosenLot;
                if(lotNum > 1) {
                    chosenLot = comparePrices(newCar, lot1, lot2);
                }
                else {
                    chosenLot = lot;
                }
                if(arr[1].equals("Entering")){
                        if(newCar.wantsLatestPrices){
                            p.requestRules(newCar, chosenLot);
                        }
                        System.out.println("Car " + newCar.name + " is entering and will park shortly.");
                        chosenLot.entranceLine.add(newCar);
                }
                else if(arr[1].equals("Exiting"))
                {
                    if(chosenLot.isCarParked(newCar) != -1){
                        //if this is a repeat of the same car (w/ a status change) in the data to show the full cycle of entering, parking, and leaving

                        //first, find that car in the lot and switch them from parked to leaving
                        chosenLot.carIsLeaving(newCar, isCarParked(newCar));
                        chosenLot.checkForTicket(newCar);

                    }
                    else{
                        chosenLot.exitLine.add(newCar);
                        chosenLot.parkingProcedure(newCar);
                    }

                }
            }
            lotNumReader.close();
            reader.close();

            if(lotNum > 1){
                //start w/ lot 1 b/c we have to start somewhere
                lot1.beginTheDay(lot1.entranceLine,lot1.exitLine);
                totalRevenue += lot1.getRevenue();
                lot2.beginTheDay(lot2.entranceLine,lot2.exitLine);
                totalRevenue += lot1.getRevenue();
            }
            else{
                lot.beginTheDay(lot.entranceLine,lot.exitLine);
                totalRevenue += lot.getRevenue();
            }
        }
        catch(Exception e)
        {
            System.out.println("Unable to find/read given file.");
            e.printStackTrace();
        }
    }

    public static void main(String [] args) {

        String input = "test3 - 2.txt";//file that will be included
        ControlRoom c = new ControlRoom();
        c.readParkingData(input);//read the file, put everybody in their respective queues
        //park.beginTheDay(park.entranceLine,park.exitLine);
        //System.out.println("Revenue earned: " + c.totalRevenue);
    }
}
