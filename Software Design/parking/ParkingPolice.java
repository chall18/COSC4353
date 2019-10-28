//Separate class that is going to act as the boss(es) of the prices/discounts/policies each parking object
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
public class ParkingPolice extends parking {

    double choosePrice(double limitA, double limitB){
        double price = (Math.random() * ((limitB - limitA)+1)+limitA);
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.valueOf(df.format(price));
    }

    double discountByDistance(int distance){//lets say distance is a unit like blocks away from venue
        //How high-priced a parking lot is can vary based on distance.
        //If the lot is right by the popular venue, the price tends to be higher. The farther away you have to park, the cheaper the lot tends to be.
        double distanceDiscount = 1;
        if(distance < 2){
            distanceDiscount = 0;//as in prices don't change
        }
        else if(distance >= 3 && distance <= 5){
            distanceDiscount = 0.25;//reduce by a quarter
        }
        else if(distance > 5){
            distanceDiscount = 0.5;//reduce by half
        }
        return distanceDiscount;
    }

    double []calculatePrice(int []intervals, boolean lotType){
        double priceArr[] = new double[intervals.length];
        for(int i = 0; i < intervals.length; i++){
            double totalPrice;
            if(i == 0){
                //System.out.println("totalPrice = choosePrice(" + priceArr[i] + ", " + priceArr[i+1] + ")");
                totalPrice = choosePrice(intervals[i], intervals[i+1]);
            }
            else{
                //System.out.println("totalPrice = choosePrice(" + priceArr[i-1] + ", " + priceArr[i] + ")");
                totalPrice = choosePrice(intervals[i-1], intervals[i]);
            }
            if(lotType){//if true and we're in a covered lot, double the price
                totalPrice *= 2;
            }
            DecimalFormat d = new DecimalFormat("##.##");
            d.setRoundingMode(RoundingMode.CEILING);
            priceArr[i] = Double.valueOf(d.format(totalPrice));
        }
        return priceArr;
    }

    double calculatePriceForRevenue(int l, int h, boolean lotType, int distance){
        double totalPrice = choosePrice(l,h);
        if(lotType){//if true and we're in a covered lot, double the price
            totalPrice *= 2;
        }
        double discount = discountByDistance(distance);
        double moneySaved = totalPrice*discount;
        totalPrice = totalPrice - moneySaved;
        DecimalFormat d = new DecimalFormat("##.##");
        d.setRoundingMode(RoundingMode.CEILING);
        return Double.valueOf(d.format(totalPrice));
    }

    int []rulesForCalculation(String dayType){
        //For the simplicity's sake, all lots will have the same intervals of time, but varying prices
        int weekdays[] = {1, 2, 3, 4, 5, 24};
        int weekends[] = {3, 5, 24};
        if(dayType.equals("wd")){
            return weekdays;
        }
        else{
            return weekends;
        }
    }
    String setRules(parking lot){//This is akin to having a sign at the front of the lot w/ the times and prices on it

        Random r = new Random();
        int distance = r.nextInt(10);
        boolean lotType = lot.lotType;
        double discount = discountByDistance(distance);

        double []weekDayPrices = calculatePrice(rulesForCalculation("wd"), lotType);
        double []weekendPrices = calculatePrice(rulesForCalculation("we"), lotType);

        String weekdays = "Monday - Friday:\n 0-60 min: $" + weekDayPrices[0] + "\n 1-2 hr: $" + weekDayPrices[1] + "\n 2-3 hr: $" + weekDayPrices[2] + "\n 3-4 hr: $" + weekDayPrices[3] + "\n 4-5 hr: $" + weekDayPrices[4] + "\n 5-24 hr: $" + weekDayPrices[0] + "\n\n";
        String weekends = "Weekends: \n < 3: $" + weekendPrices[0] + "\n 3-5 hr: $" + weekendPrices[1] + "\n 5-24 hr: $" + weekendPrices[2] + "\n\n";
        String lotRules = weekdays + weekends;
        lot.rules = lotRules;
        return lot.rules;
    }

    void requestRules(Car a, parking lot){
        System.out.println("Car " + a.name + " has requested the rules of this lot. See the sign below: ");
        lot.rules = setRules(lot);
        System.out.println(lot.rules);
    }
}

