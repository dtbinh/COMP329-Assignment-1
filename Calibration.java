import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.robotics.navigation.DifferentialPilot;

public final class Calibration 
{
    private static int turnAngle = 253;
	
	static int Calibrate(DifferentialPilot pilot)
	{
		LCD.clear();
		LCD.drawString("Set me at 0 degrees and press \nenter to attempt \na turn", 0, 0);
		Button.waitForAnyPress();
		pilot.rotate(--turnAngle);	
	    LCD.clear();
		LCD.drawString("Enter = Confirm \nLeft = Reduce \nRight = Increase \nEscape = Test ", 0, 0);
		LCD.drawString("Angle is now:" + turnAngle , 0, 5);
		
		Button.waitForAnyPress();	
		int b = Button.readButtons();
		
		while(b != Button.ID_ENTER)
		{
			if(b == Button.ID_RIGHT)
			{
				turnAngle++;
			    LCD.clear();
				LCD.drawString("Enter = Confirm \nLeft = Reduce \nRight = Increase \nEscape = Test ", 0, 0);
				LCD.drawString("Angle is now:" + turnAngle , 0, 5);
			}	
			if(b == Button.ID_LEFT)
			{
				turnAngle--;
			    LCD.clear();
				LCD.drawString("Enter = Confirm \nLeft = Reduce \nRight = Increase \nEscape = Test ", 0, 0);
				LCD.drawString("Angle is now:" + turnAngle, 0, 5);
			}
			if(b == Button.ID_ESCAPE)
			{
				Calibrate(pilot);					
			}
			Button.waitForAnyPress();	
			b = Button.readButtons();
		}
		
		return turnAngle;
	}

}
