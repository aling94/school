#pragma config(Hubs,  S1, HTMotor,  HTMotor,  HTMotor,  HTServo)
#pragma config(Sensor, S1,     ,               sensorI2CMuxController)
#pragma config(Motor,  mtr_S1_C1_1,     rearRight,     tmotorTetrix, openLoop)
#pragma config(Motor,  mtr_S1_C1_2,     frontRight,    tmotorTetrix, openLoop)
#pragma config(Motor,  mtr_S1_C2_1,     lift1,         tmotorTetrix, openLoop)
#pragma config(Motor,  mtr_S1_C2_2,     lift2,         tmotorTetrix, openLoop)
#pragma config(Motor,  mtr_S1_C3_1,     frontLeft,     tmotorTetrix, openLoop)
#pragma config(Motor,  mtr_S1_C3_2,     rearLeft,      tmotorTetrix, openLoop)
#pragma config(Servo,  srvo_S1_C4_1,    claw,                 tServoStandard)
#pragma config(Servo,  srvo_S1_C4_2,    servo2,               tServoNone)
#pragma config(Servo,  srvo_S1_C4_3,    servo3,               tServoNone)
#pragma config(Servo,  srvo_S1_C4_4,    servo4,               tServoNone)
#pragma config(Servo,  srvo_S1_C4_5,    servo5,               tServoNone)
#pragma config(Servo,  srvo_S1_C4_6,    servo6,               tServoNone)
//*!!Code automatically generated by 'ROBOTC' configuration wizard               !!*//

#include "JoystickDriver.c"  //Include file to "handle" the Bluetooth messages.

/*--------------------------------------------------------------------------------------------------------*\
|*                                                                                                        *|
|*                                    - FTC Team 5359 [SolinNoids] -                                      *|
|*                                           Tele-Op Program                                              *|
|*                                                                                                        *|
|*  This program is for the holonomic drive setup for the Team 5359's 2012-13 FTC Robot.                  *|
|*  This setup uses four omniwheels positioned at 45 degree angles at the corners of the robot.           *|
|*  ____________________________________________________________________________________________________  *|
|*                                                                                                        *|
|*                                              CONTROLS                                                  *|
|*     Main Robot Movement:                                                                               *|
|*         Left Joystick:                                                                                 *|
|*            - Up -------------> Foward                                                                  *|
|*            - Down -----------> Backward                                                                *|
|*            - Left -----------> Strafe Left                                                             *|
|*            - Right ----------> Strafe Right                                                            *|
|*            - Left+Up --------> Strafe Forward-Left                                                     *|
|*            - Right+Up -------> Strafe Forward-Right                                                    *|
|*            - Left+Down ------> Strafe Back-Left                                                        *|
|*            - Right+Down -----> Strafe Back-Right                                                       *|
|*         Right Stick:                                                                                   *|
|*            - Left -----------> Rotate Left/Anti-Clockwise                                              *|
|*            - Right ----------> Rotate Right/Clockwise                                                  *|
|*                                                                                                        *|
|*     Lift Movement:                                                                                     *|
|*            - D-Pad Up -------> Move lift up                                                            *|
|*            - D-Pad Down -----> Move lift down                                                          *|
|*                                                                                                        *|
|*     Claw Controls                                                                                      *|
|*            - Num-Button 5 ---> Claw open                                                               *|
|*            - Num-Button 6 ---> Claw close                                                              *|
|*            - Num-Button 2 ---> Lights on                                                               *|
|*            - Num-Button 4 ---> Lights off                                                              *|
|*                                                                                                        *|
\*---------------------------------------------------------------------------------------------------5359-*/

//------------------------------------------ ULTILITY METHODS --------------------------------------------//

int xClock()  {return abs(joystick.joy1_x1);}	//Left joystick x-axis value; clockwise
int xAClock() {return -abs(joystick.joy1_x1);}	//Left joystick x-axis value; anti-clockwise
int yClock()  {return abs(joystick.joy1_y1);}	//Left joystick y-axis value; clockwise
int yAClock() {return -abs(joystick.joy1_y1);}	//Left joystick y-axis value; anti-clockwise

/* Averages the left joystick x and y axis values. */
int joyAvg()	{return (joystick.joy1_x1 + joystick.joy1_y1) / 2;}

/* Moves wheel motors.
 * @param:
 * 	- fl: input for frontLeft motor
 * 	- fr: input for frontRight motor
 * 	- rl: input for rearLeft motor
 * 	- rr: input for rearRight motor
 */
void moveWheels(int fl, int fr, int rl, int rr)
{
   motor[frontLeft]  = fl;
   motor[frontRight] = fr;
   motor[rearLeft]   = rl;
   motor[rearRight]  = rr;
}

/* Moves all wheel motors.
 * @param:
 * 	- joyValue: input for frontLeft motor
 */
void moveWheels(int joyValue)
{
   motor[frontLeft]  = joyValue;
   motor[frontRight] = joyValue;
   motor[rearLeft]   = joyValue;
   motor[rearRight]  = joyValue;
}

/* Moves lift motors.
 * @param:
 * 	- power: power allocated to lift motors
 */
void moveLift(int power)
{
   motor[lift1] = power;
   motor[lift2] = power;
}
//------------------------------------------ END ULTILITY METHODS ----------------------------------------//


//-------------------------------------------- TELE-OP PROGRAM -------------------------------------------//

task main()
{
   waitForStart();   // wait for start of tele-op phase

   while (true)
   {
      getJoystickSettings(joystick);

      //---------------------------- MAIN ROBOT CONTROL ----------------------------//

      //=============== LEFT JOYSTICK ===============//

      //*** FORWARD ***//
      if (joystick.joy1_y1 > 10 && abs(joystick.joy1_x1) < 10 && abs(joystick.joy1_x2) < 10)
         moveWheels(yAClock(), yClock(),  yAClock(), yClock());

      //*** REVERSE ***//
      if (joystick.joy1_y1 < -10 && abs(joystick.joy1_x1) < 10 && abs(joystick.joy1_x2) < 10)
         moveWheels(yClock(), yAClock(),  yClock(), yAClock());

      //*** STRAFE LEFT ***//
      if (joystick.joy1_x1 > 10 && abs(joystick.joy1_y1) < 10 && abs(joystick.joy1_x2) < 10)
         moveWheels(yClock(), yClock(),  yAClock(), yAClock());

      //*** STRAFE RIGHT ***//
      if (joystick.joy1_x1 < -10 && abs(joystick.joy1_y1) < 10 && abs(joystick.joy1_x2) < 10)
         moveWheels(yAClock(), yAClock(),  yClock(), yClock());

      //*** STRAFE UP-LEFT ***//
      if (joystick.joy1_x1 < -10 && joystick.joy1_y1 > 10 && abs(joystick.joy1_x2) < 10)
         moveWheels(0, joyAvg(), -joyAvg(), 0);

      //*** STRAFE UP-RIGHT ***//
      if (joystick.joy1_x1 > 10 && joystick.joy1_y1 >10 && abs(joystick.joy1_x2) < 10)
         moveWheels(-joyAvg(), 0, 0, joyAvg());

      //*** STRAFE BACK-LEFT ***//
      if (joystick.joy1_x1 < -10 && joystick.joy1_y1 < -10 && abs(joystick.joy1_x2) < 10)
         moveWheels(joyAvg(), 0, 0, -joyAvg());

      //*** STRAFE BACK-RIGHT ***//
      if (joystick.joy1_x1 > 10 && joystick.joy1_y1 < -10 && abs(joystick.joy1_x2) < 10)
         moveWheels(0, -joyAvg(), joyAvg(), 0);
      //============= END LEFT JOYSTICK =============//

      //============== RIGHT JOYSTICK ===============//

      //*** TURN LEFT ***//
      if (joystick.joy1_x2 > 10 && abs(joystick.joy1_x1) < 10 && abs(joystick.joy1_y1) < 10)
         moveWheels(abs(joystick.joy1_x2));

      //*** TURN RIGHT ***//
      if (joystick.joy1_x2 < -10 && abs(joystick.joy1_x1)<10 && abs(joystick.joy1_y1) < 10)
         moveWheels(-abs(joystick.joy1_x2));
      //============ END RIGHT JOYSTICK ==============//

      // Stop all wheels if no joystick input
      if (abs(joystick.joy1_x1) < 10 && abs(joystick.joy1_y1) < 10 && abs(joystick.joy1_x2) < 10)
         moveWheels(0);
      //--------------------------- END MAIN ROBOT CONTROL -------------------------//


      //------------------------------- LIFT CONTROL -------------------------------//

      if (joystick.joy1_TopHat == 0) moveLift(-50);                     // Lift up
      else if (joystick.joy1_TopHat == 4) moveLift(50);                 // Lift Down
      else if (joystick.joy1_TopHat != 0 && joystick.joy1_TopHat != 4)  // Stop lift
         moveLift(0);
      //----------------------------- END LIFT CONTROL -----------------------------//


      //------------------------------- CLAW CONTROL--------------------------------//

      if (joy1Btn(5)==1) servo[claw] = 140; //open on pressing 5
      else if (joy1Btn(6)==1) servo[claw] = 220; //close on pressing 6
      //----------------------------- END CLAW CONTROL -----------------------------//


      //------------------------------ LIGHT CONTROL -------------------------------//

      //if (joy1Btn(2)==1) motor[light] = 50; //press 2 to turn on lights
      //if (joy1Btn(4)==1) motor[light] = 0; //press 4 to turn off lights
      //---------------------------- END LIGHT CONTROL -----------------------------//
   }
}
