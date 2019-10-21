//class parking will read and change data of Car objects while monitoring its capacity

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

public class parking extends Car {
    private int capacity; //can't have 1.5 spaces, so int is fine here
    private int spacesFilled;//how many cars we currently have
    private Car []parkingLot;
    private int revenue;

    //setting rules for money owed per hours spent in garage
    int price(Car a){
        double timeSpent = 0;
        if(a.timeExited < a.timeEntered){
            //if Car spent more than 12 hours parked
            timeSpent = 2400 + a.timeExited - a.timeEntered;
        }
        else{
            timeSpent = a.timeExited - a.timeEntered;
        }

        int moneyOwed = 0;
        double hours = timeSpent/100;
        if(hours < 0.5){
            moneyOwed = 0;
        }
        else if(hours >= .5 && hours <= 1){
            moneyOwed = 2;
        }
        else if(hours > 1 && hours <= 2){
            moneyOwed = 4;
        }
        else if(hours > 2 && hours <= 3){
            moneyOwed = 8;
        }
        else if(hours > 3 && hours <= 4)
        {
            moneyOwed = 10;
        }
        else if(hours > 4 && hours <= 5){
            moneyOwed = 12;
        }
        else if(hours > 5 && hours <= 24){
            moneyOwed = 14;
        }
        else if(hours < 24){
            moneyOwed = 20;
        }
        System.out.println("Car " + a.name + " spent " + hours + " hours parked and will be charged $" + moneyOwed + ".");
        return moneyOwed;
    }

    //Car objects will wait in these queues to be dealt with once they're read in
    private Queue<Car> entranceLine = new LinkedList<>();
    private Queue<Car> exitLine = new LinkedList<>();

//    public void setCapacity(int n){
//        this.capacity = n;
//    }
//    public int getCapacity(){
//        return this.capacity;
//    }
//
//    public void setParkingLot(Car []lot){
//        this.parkingLot = lot;
//    }
//    public Car [] getParkingLot(){
//        return this.parkingLot;
//    }

    //returns the index of the closest parking space in the parkingLot array
    int findNearestSpace()
    {
        int spaceIndex = -1;
        //check that we have any spaces open before going to the trouble of running this loop
        if(!isAtCapacity())//if we're not at capacity, run the loop
        {
            for(int i = 0; i < capacity; i++)
            {
                if((parkingLot[i].name).equals("Empty"))
                {
                    spaceIndex = i;
                    break;
                }
            }
        }
        //if we are at capacity, spaceIndex will remain -1 and be returned as such
        return spaceIndex;
    }

    //returns # of spaces filled w/ parked cars
    int findParkedNum(Car []lot)
    {
        int parkedNum = 0;
        for(int i = 0; i < capacity; i++)
        {
            if(!lot[i].equals("x"))
            {
                parkedNum++;
            }
        }
        spacesFilled = parkedNum;//make sure to update our member variable whenever this is run
        return parkedNum;
    }

    //Cars should only have tickets after entering, while they are parked, and right as they exit
    void checkForTicket(Car a){
        if(a.iHaveTicket){

            switch(a.status)
            {
                case "Entering":
                {
                    //Car shouldn't have ticket prior to entering
                    System.out.println("Uhhhh, you shouldn't have that.");
                    a.iHaveTicket = false;
                    break;
                    //add something to send cars like this to back of line so they can try again
                }
                case "Exiting":
                {
                    System.out.println("Car " + a.name + " has their ticket. I'll take that. Have a nice day!");
                    a.iHaveTicket = false;//flipping the flag and NOT sending them to the end of the line. They're done.
                    spacesFilled--;
                    revenue += price(a);
                    break;
                }
            }
        }
        else {

            switch (a.status) {
                case "Entering": {
                    a.iHaveTicket = true;
                    spacesFilled++;
                    //parkingProcedure(a);
                    break;
                }
                case "Exiting": {
                    System.out.println("Car " + a.name + " is trying to leave, but has no ticket. Whoops! Better go back and find it!");
                    a.iHaveTicket = true;//they found their ticket!
                    exitLine.add(a);//go back to the line and try again
                    break;
                }
            }
        }
    }

    boolean isAtCapacity() {
        boolean atCapacity = false;
        if (spacesFilled == capacity) {
            atCapacity = true;
            System.out.println("Parking lot is full. You'll have to go somewhere else!");
        }
        return atCapacity;
    }

    void parkingProcedure(Car a)
    {
        int goParkHere = findNearestSpace();
        parkingLot[goParkHere] = a;
        spacesFilled++;
        a.status = "Parked";
        System.out.println("Car " + a.name + " is parked.");
    }

    void lotSetUp(int cap){
        parkingLot = new Car[capacity];//set up our new lot w/ the # of spaces set in file
        Car tempCar = new Car();
        tempCar.name = "Empty";
        for(int i = 0; i < capacity; i++){
            parkingLot[i] = tempCar;
        }
    }
    void readParkingData(String file)
    {
        //read input file line by line, each Car attribute delimited by a comma
        Path myPath = Paths.get(file);
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            int newCap = Integer.valueOf(line);
            capacity = newCap;
            lotSetUp(capacity);
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

                switch(arr[1])
                {
                    case "Entering":
                    {
                        System.out.println("Car " + newCar.name + " is entering and will park shortly.");
                        entranceLine.add(newCar);
                        break;
                    }
                    case "Exiting":
                    {
                        if(isCarParked(newCar) != -1){
                            //if this is a repeat of the same car (w/ a status change) in the data to show the full cycle of entering, parking, and leaving

                            //first, find that car in the lot and switch them from parked to leaving
                            carIsLeaving(newCar, isCarParked(newCar));
                            checkForTicket(newCar);

                        }
                        else{
                            exitLine.add(newCar);
                            parkingProcedure(newCar);
                        }
                        break;

                    }
                }
            }
            reader.close();
        }
        catch(Exception e)
        {
            System.out.println("Unable to find/read given file.");
            e.printStackTrace();
        }
    }

    int getRevenue(){
        return this.revenue;
    }

    int isCarParked(Car a)
    {
        int isParked = -1;//returns -1 if not already parked, otherwise returns the spot this car is in
        for(int i = 0; i < capacity; i++)
        {
            if((parkingLot[i].name).equals(a.name)){
                parkingLot[i].status = a.status;//switching a "Parked" Car to an "Exiting" Car
                isParked = i;
                break;
            }
        }
        return isParked;
    }

    void carIsLeaving(Car a, int space)
    {
        //change the real car's status from parked to leaving, then overwrite where it was in the array with a "blank" car
            //the temp obj is serving as a substitute for an empty spot. Searching for null wasn't working well.
        a.status = "Exiting";
        Car temp = new Car();
        temp.name = "Empty";
        parkingLot[space] = temp;
        System.out.println("Car " + a.name + " is heading to the exit line now.");
    }

    void beginTheDay(Queue <Car> enter, Queue<Car> exit)
    {
        while(!enter.isEmpty() || !exit.isEmpty())//as long as there are still cars waiting in either line...
        {
            //check who is entering or leaving first in each Q
            if(!enter.isEmpty() && !exit.isEmpty())//both Q's have Cars
            {
                Car enterFirst = enter.element();
                Car exitFirst = exit.element();

                if(enterFirst.timeEntered < exitFirst.timeExited && !(enterFirst.name).equals(exitFirst.name)){
                    //if the next person entering arrives before the next person to exit is leaving
                    enter.remove();
                    checkForTicket(enterFirst);
                }
                else{
                    exit.remove();
                    if(isCarParked(exitFirst) != -1)
                    {
                        //if the exiting car from the txt file is already parked, search for it in our lot, then switch its status from
                            //Parked to Exiting
                        carIsLeaving(exitFirst, isCarParked(exitFirst));
                        checkForTicket(exitFirst);
                    }
                    else{
                        //if this is a car that was already parked when this all started, then we only have to record them leaving
                        checkForTicket(exitFirst);

                    }
                }
            }
            else if(!enter.isEmpty() && exit.isEmpty()){//only have cars waiting to get in

                Car enterFirst = enter.element();
                enter.remove();
                checkForTicket(enterFirst);
            }
            else if(enter.isEmpty() && !exit.isEmpty()){//only have cars waiting to leave

                //if the car was parked prior to us knowing it had ever entered (so before this program started
                    //collecting data, maybe), then we'll set up its parking space when its read in from the txt file, not here
                Car exitFirst = exit.element();
                exit.remove();
                //parkingProcedure(exitFirst);
                int space = isCarParked(exitFirst);
                carIsLeaving(exitFirst, space);
                checkForTicket(exitFirst);
            }


        }

    }

    public static void main(String [] args) {

        parking park = new parking();
        String input = "test4.txt";//file that will be included
        park.readParkingData(input);//read the file, put everybody in their respective queues
        park.beginTheDay(park.entranceLine,park.exitLine);
        System.out.println("Revenue earned: " + park.getRevenue());

        //if something is read in as exiting, mark them initially as parked.


        //Once everything is read in, then the "day" of parking begins
            //start unloading the queues
                //Rather than unloading each queue all at once, we should check according to time
                //If car A is entering at noon, but car B is exiting at 11:30, then we should deal with head of exit Q first


        //Certain things should go hand-in-hand. If a car's status is "Entering", they shouldn't have a ticket.
            //Once they're given a ticket, change their status to parked. (They park super fast!)
            //If a car approaches the exit gate, they should have a ticket. At that point, take away their ticket and make that false.

        //If status parked, add 1 to spacesFilled.
            //Once car has approached exit gate and had their ticket taken, subtract 1 from spacesFilled.



    }
}
