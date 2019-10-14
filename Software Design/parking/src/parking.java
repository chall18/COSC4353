//class parking will read and change data of Car objects while monitoring its capacity

public class parking {
    int capacity; //can't have 1.5 spaces, so int is fine here
    int spacesFilled;//how many cars we currently have

    //Will input name of car currently parked when a slot is filled. That way we can even see who has what # spot
    String []parkingSpaces = new String[capacity];

    //Sets the value of all indices in the parking lot to 'x' so we know those are empty spots
    String [] setLot(String []lot)
    {
        for(int i = 0; i < capacity; i++)
        {
            lot[i] = "x";
            System.out.println(lot[i] + " ");
        }
        return lot;
    }

    int findParkedNum(String []lot)
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
    boolean checkForTicket(Car a){
        if(a.iHaveTicket){

            switch(a.status)
            {
                case "Entering":
                {
                    //Car shouldn't have ticket prior to entering
                    System.out.println("Uhhhh, you shouldn't have that.");
                    a.iHaveTicket = false;
                    //add something to send cars like this to back of line so they can try again
                }
                case "Exiting":
                {
                    System.out.println("Car " + a.name + " has their ticket. I'll take that. Have a nice day!");
                    a.iHaveTicket = false;//flipping the flag and NOT sending them to the end of the line. They're done.
                }
                case "Parked":
                {
                    //Add method to decide where to park this car. From that, we'll also get what space they parked in and add
                        //it to the println
                    System.out.println("Car " + a.name + " is parked and has their ticket.");
                }
            }
        }
        else{

            switch(a.status)
            {
                case "Entering":
                {
                    System.out.println("Car " + a.name + " just entered and needs a ticket. Here you go!");
                    a.iHaveTicket = true;
                    //Send this car through the parking method too?
                }
                case "Exiting":
                {
                    System.out.println("Car " + a.name + " is trying to leave, but has no ticket. Whoops! Better go back and find it!");
                    a.iHaveTicket = true;
                    //send car to back of line to try again
                }
                case "Parked":
                {
                    //The parked case might get removed from this later. Haven't decided yet. May be unnecessary
                        //Depends on what kind of data we feed into this, I think.

                    //Parked, but forgot to get a ticket
                    System.out.println("Car " + a.name + " is parked and but forgot to grab a ticket. Better go back and get one!");
                    //this would probably be a signal of bad input data, but better have a rule to handle it
                    //car magically got into garage without a ticket
                }
            }
        }
        return a.iHaveTicket;
    }

    boolean isAtCapacity() {
        boolean atCapacity = false;
        spacesFilled = findParkedNum(parkingSpaces);//Make sure this is updated before we continue
        if (spacesFilled == capacity) {
            atCapacity = true;
            System.out.println("Parking lot is full. You'll have to go somewhere else!");
        }
        else{
            System.out.println("Plenty of room! Come on in!");
        }
        return atCapacity;
    }

    public static void main(String [] args) {

        //will read data from text file in here?
        //write text file sample data in specific format so program can read it.
        //Each line in the file represents a car in whatever state it's currently in.

        //Certain things should go hand-in-hand. If a car's status is "Entering", they shouldn't have a ticket.
            //Once they're given a ticket, change their status to parked. (They park super fast!)
            //If a car approaches the exit gate, they should have a ticket. At that point, take away their ticket and make that false.

        //If status parked, add 1 to spacesFilled.
            //Once car has approached exit gate and had their ticket taken, subtract 1 from spacesFilled.
    }
}
