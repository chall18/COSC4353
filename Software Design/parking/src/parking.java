//class parking will read and change data of Car objects while monitoring its capacity

public class parking {
    int capacity; //can't have 1.5 spaces, so int is fine here
    int spacesFilled;//how many cars we currently have

    boolean checkForTicket(Car a){
        if(a.iHaveTicket){

            if(a.status.equals("Entering")){
                System.out.println("Uhhhh, you shouldn't have that.");
                a.iHaveTicket = false;
            }
            else if(a.status.equals("Exiting")){
                System.out.println("Car " + a.name + " has their ticket. I'll take that. Have a nice day!");
                a.iHaveTicket = false;
            }
            else{
                //else they're in parking mode
                System.out.println("Car " + a.name + " is parked and has their ticket.");
            }
        }
        else{
            if(a.status.equals("Entering")){
                System.out.println("Car " + a.name + " just entered and needs a ticket. Here you go!");
                a.iHaveTicket = true;
            }
            else if(a.status.equals("Exiting")){
                System.out.println("Car " + a.name + " is trying to leave, but has no ticket. Whoops! Better go back and find it!");
                a.iHaveTicket = true;
                //Maybe add a function that forces forgetful cars to the back of the line, but with this updated ticket status
            }
            else{
                //Parked, but forgot to get a ticket
                System.out.println("Car " + a.name + " is parked and but forgot to grab a ticket. Better go back and get one!");
                //this would probably be a signal of bad input data, but better have a rule to handle it
                //car magically got into garage without a ticket
            }
        }



        return a.iHaveTicket;
    }

    boolean isAtCapacity() {
        boolean atCapacity = false;
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
