package org.simbotics.frc2018.auton.mode.middle;

import org.simbotics.frc2018.auton.AutonOverride;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.auton.drive.DriveSetOutput;
import org.simbotics.frc2018.auton.drive.DriveSetPosition;
import org.simbotics.frc2018.auton.drive.DriveStraight;
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
import org.simbotics.frc2018.util.FieldConstants;

public class MiddleSwitchLeft4Cubes implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {

		ab.addCommand(new DriveSetPosition(0.5, 0, 90));

		ab.addCommand(new DriveToPoint(-4.5, 8.3, 180 - 75.0, 0.25, 10, 0.75, 10, 20.0, false, 5000));
		ab.addCommand(new AutonWait(250));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL, 0.8, 1000));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL));
		ab.addCommand(new DriveWait());
		ab.addCommand(new IntakeSetOutput(-1));
		ab.addCommand(new DriveSetOutput(0.15));
		ab.addCommand(new AutonWait(400));
		ab.addCommand(new IntakeSetOutput(0));
		ab.addCommand(new DriveSetOutput(0));
		// get second cube
		ab.addCommand(new DriveToPoint(-0.5, 3.48, 180 - 61.3, 0.0, 11, 0.75, 7, true, 5000));
		ab.addCommand(new AutonWait(500));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL));
		ab.addCommand(new DriveWait());
		ab.addCommand(new DriveTurnToAngle(180 - 90, 5, 1000));
		ab.addCommand(new DriveToPoint(-0.5, 7.0, 180 - 90, 0.0, 7, 0.25, 25, true, 5000));
		ab.addCommand(new IntakeAntiJamNoBump(1.0, 150));
		ab.addCommand(new DriveSetOutput(0.0));
		ab.addCommand(new IntakeWait());
		ab.addCommand(new DriveSetOutput(0));

		// go score 2nd cube
		ab.addCommand(new DriveToPoint(0.08, 4.75, 180 - 48.0, 0.0, 6, 0.4, 25, true, 5000));
		ab.addCommand(new DriveWait());

		ab.addCommand(new DriveToPoint(-2.6, 9.4, 180 - 62.0, 0.0, 11, 0.75, 25, true, 5000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL, 0.7, 1000));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL));
		ab.addCommand(new DriveWait());
		ab.addCommand(new IntakeSetOutput(-1));
		ab.addCommand(new DriveSetOutput(0.15));
		ab.addCommand(new AutonWait(400));
		ab.addCommand(new IntakeSetOutput(0));
		ab.addCommand(new DriveSetOutput(0));

		// 3rd cube
		ab.addCommand(new DriveToPoint(-3.5, 6.55, 180 - 143, 0.0, 9, 0.75, 15, true, 5000));
		ab.addCommand(new AutonWait(500));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL));
		ab.addCommand(new DriveToPoint(-1.15, 9.13, 180 - 143.0, 0.0, 7, 0.75, 15, true, 5000));

		ab.addCommand(new IntakeAntiJamNoBump(1.0, 150));
		ab.addCommand(new IntakeWait());

		ab.addCommand(new DriveToPoint(-2.3, 8.0, 180 - 130.0, 0.0, 8, 0.75, 15, true, 5000));
		ab.addCommand(new AutonWait(500));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL, 0.8, 1000));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SWITCH, WristPosition.LEVEL));

		ab.addCommand(new DriveToPoint(-2.5, 10.5, 180 - 70.0, 0.0, 11, 0.75, 15, true, 5000));
		ab.addCommand(new DriveWait());

		ab.addCommand(new IntakeSetOutput(-1));
		ab.addCommand(new DriveSetOutput(0.15));
		ab.addCommand(new AutonWait(500));
		ab.addCommand(new IntakeSetOutput(0));
		ab.addCommand(new DriveSetOutput(0));
		// 4th cube

		ab.addCommand(new DriveToPoint(-2.4, 7.75, 180 - 90.0, 0.0, 5, 0.75, 7, true, 5000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL));
		ab.addCommand(new DriveToPoint(-0.77, 10.5, 180 - 140.0, 0.0, 5, 0.75, 7, true, 5000));
		ab.addCommand(new IntakeAntiJamNoBump(1.0, 150));
		ab.addCommand(new IntakeWait());
		ab.addCommand(new DriveToPoint(-2.4, 7.75, 180 - 90.0, 0.0, 5, 0.75, 7, true, 5000));
		ab.addCommand(new DriveWait());

	}
}
