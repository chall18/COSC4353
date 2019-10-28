//A Car object that is mainly used for member variables.

public class Car
{
    String status;//This will be either Entering, Parked, or Exiting
    boolean iHaveTicket;//true if driver has it, false it not.
    String name;//I think the cars will just be A, B, C, etc for now. This might be nice for organization of output
    double timeEntered, timeExited;//can record this in military time to make it easier to calculate
    String dayType;
    int distanceFromVenue;//cars will have different venues they're trying to get to, while parking lots remain in one place.
    boolean wantsLatestPrices;//T/F regarding if the driver wants to get the latest prices from the groups



    public Car(){}
}
