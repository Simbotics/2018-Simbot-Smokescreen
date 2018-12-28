package org.simbotics.frc2018.auton.mode.left;

import org.simbotics.frc2018.auton.AutonOverride;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.auton.drive.DriveSetOutput;
import org.simbotics.frc2018.auton.drive.DriveSetPosition;
import org.simbotics.frc2018.auton.drive.DriveToPoint;
import org.simbotics.frc2018.auton.drive.DriveTurnToAngle;
import org.simbotics.frc2018.auton.drive.DriveWait;
import org.simbotics.frc2018.auton.intake.IntakeAntiJamNoBump;
import org.simbotics.frc2018.auton.intake.IntakeSetOutput;
import org.simbotics.frc2018.auton.intake.IntakeWait;
import org.simbotics.frc2018.auton.lift.LiftHoldPosition;
import org.simbotics.frc2018.auton.lift.LiftSetPosition;
import org.simbotics.frc2018.auton.mode.AutonBuilder;
import org.simbotics.frc2018.auton.mode.AutonMode;
import org.simbotics.frc2018.auton.util.AutonWait;
import org.simbotics.frc2018.subsystems.Lift.ElevatorPosition;
import org.simbotics.frc2018.subsystems.Lift.WristPosition;

public class LeftSide2RightSwitch implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		// go to score first cube
		ab.addCommand(new DriveSetPosition(0, 0, 90));
		ab.addCommand(new DriveToPoint(0, 1.4, 180-90, 6, 6, 0.25, 25, true, 5000));
		// ab.addCommand(new DriveToPoint(-3.3, 4.2, 145, 7, 8,0.5, 5000));
		ab.addCommand(new DriveToPoint(10.8, 4.2, 180-180, 7, 11, 0.5, 25, true, 5000));
		// ab.addCommand(new DriveToPoint(-13.2, 5.89, 128, 6, 9, 0.25,25,true, 5000));
		ab.addCommand(new DriveToPoint(14.8, 9.4, 180-90, 1, 7, 0.25, 25, true, 5000));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL));

		// score first cube
		ab.addCommand(new DriveWait());
		ab.addCommand(new IntakeSetOutput(-1));
		ab.addCommand(new DriveSetOutput(0.25));
		ab.addCommand(new AutonWait(300));
		ab.addCommand(new IntakeSetOutput(0));
		// go to and pick up 2nd cube
		ab.addCommand(new DriveToPoint(14.6, 8.48, 180-90, 6, 8, 0.25, 25, true, 5000));
		ab.addCommand(new AutonWait(250));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 20));
		ab.addCommand(new DriveToPoint(9.0, 3.38, 180-137, 0, 11, 0.25, 7, true, 5000));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL));
		ab.addCommand(new DriveTurnToAngle(180-90, 5, 750));
		ab.addCommand(new DriveToPoint(9.0, 7.0, 180-90, 0, 7, 0.25, 5000));
		ab.addCommand(new IntakeAntiJamNoBump(1.0, 55));
		ab.addCommand(new IntakeWait());
		// go to score 2nd cube
		ab.addCommand(new DriveToPoint(7.6, 5.0, 180-145, 0, 12, 0.25, 25, true, 5000));
		ab.addCommand(new DriveToPoint(12.6, 10.0, 180-120, 1, 9, 0.25, 25, 25, true, 5000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL));
		ab.addCommand(new DriveWait());
		// score 2nd cube
		ab.addCommand(new IntakeSetOutput(-1.0));
		ab.addCommand(new DriveSetOutput(0.2));
		ab.addCommand(new AutonWait(300));
		ab.addCommand(new IntakeSetOutput(0));
		// go to pick up third cube
		ab.addCommand(new DriveToPoint(13.6, 5.2, 180-42, 0, 8, 0.25, 25, true, 5000));
		ab.addCommand(new AutonWait(250));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 20));
		ab.addCommand(new DriveToPoint(11, 7.7, 180-42, 0, 7, 0.25, 7, true, 5000));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL));
		ab.addCommand(new IntakeAntiJamNoBump(1.0, 55));
		ab.addCommand(new IntakeWait());
		// go to score 3rd cube
		ab.addCommand(new DriveToPoint(11.8, 6.7, 180-45, 0, 12, 0.25, 25, true, 5000));
		ab.addCommand(new DriveWait());

	}
}
