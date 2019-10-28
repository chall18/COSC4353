//class parking will read and change data of Car objects while monitoring its capacity

import java.util.LinkedList;
import java.util.Queue;

public class parking extends Car {
    private int capacity; //can't have 1.5 spaces, so int is fine here
    private int spacesFilled;//how many cars we currently have
    private Car []parkingLot;
    private int revenue;
    String rules;
    boolean lotType;
    String lotName;

    //setting rules for money owed per hours spent in garage
    double price(Car a){
        ParkingPolice p = new ParkingPolice();
        int prices[] = p.rulesForCalculation(a.dayType);

        double timeSpent = 0;
        if(a.timeExited < a.timeEntered){
            //if Car spent more than 12 hours parked
            timeSpent = 2400 + a.timeExited - a.timeEntered;
        }
        else{
            timeSpent = a.timeExited - a.timeEntered;
        }

        double moneyOwed = 0;
        double hours = timeSpent/100;

        if(a.dayType.equals("wd")){//if trying to park on a weekday
            if(hours < prices[0]){
                moneyOwed = p.calculatePriceForRevenue(0, prices[0], lotType, a.distanceFromVenue);
            }
            else if(hours >= prices[0] && hours <= prices[1]){
                moneyOwed = p.calculatePriceForRevenue(prices[0], prices[1], lotType, a.distanceFromVenue);
            }
            else if(hours > prices[1] && hours <= prices[2]){
                moneyOwed = p.calculatePriceForRevenue(prices[1], prices[2], lotType, a.distanceFromVenue);
            }
            else if(hours > prices[2] && hours <= prices[3]){
                moneyOwed = p.calculatePriceForRevenue(prices[2], prices[3], lotType, a.distanceFromVenue);
            }
            else if(hours > prices[3] && hours <= prices[4])
            {
                moneyOwed = p.calculatePriceForRevenue(prices[3], prices[4], lotType, a.distanceFromVenue);
            }
            else if(hours > prices[4] && hours <= prices[5]){
                moneyOwed = p.calculatePriceForRevenue(prices[4], prices[5], lotType, a.distanceFromVenue);
            }
        }
        else{//on a weekend
            if(hours < prices[0]){
                moneyOwed = p.calculatePriceForRevenue(0, prices[0], lotType, a.distanceFromVenue);
            }
            else if(hours > prices[0] && hours <= prices[1]){
                moneyOwed = p.calculatePriceForRevenue(prices[0], prices[1], lotType, a.distanceFromVenue);
            }
            else if(hours > prices[1] && hours < prices[2])
            {
                moneyOwed = p.calculatePriceForRevenue(prices[1], prices[2], lotType, a.distanceFromVenue);
            }

        }
        System.out.println("Car " + a.name + " spent " + hours + " hours parked and will be charged $" + moneyOwed + ".");
        return moneyOwed;
    }

    //Car objects will wait in these queues to be dealt with once they're read in
    public Queue<Car> entranceLine = new LinkedList<>();
    public Queue<Car> exitLine = new LinkedList<>();

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


    public void setCapacity(int newCap){
        this.capacity = newCap;
    }
    public int getCapacity(){
        return capacity;
    }

}
