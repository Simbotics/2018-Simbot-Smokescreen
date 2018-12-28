package org.simbotics.frc2018.auton.mode.middle;

import org.simbotics.frc2018.auton.AutonOverride;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.auton.drive.DriveSetOutput;
import org.simbotics.frc2018.auton.drive.DriveSetPosition;
import org.simbotics.frc2018.auton.drive.DriveStraight;
import org.simbotics.frc2018.auton.drive.DriveToPoint;
import org.simbotics.frc2018.auton.drive.DriveTurnToAngle;
import org.simbotics.frc2018.auton.drive.DriveWait;
import org.simbotics.frc2018.auton.intake.IntakeAntiJamClosed;
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

public class MiddleSwitchLeft1ScaleLeft1 implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {

		ab.addCommand(new DriveSetPosition(0.5, 0, 90));

		ab.addCommand(new DriveToPoint(-4.5, 8.3, 180 - 70.0, 0.25, 10, 0.75, 7, 20.0, false, 5000));
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
		ab.addCommand(new DriveToPoint(0.2, 3.48, 180 - 61.3, 0.0, 11, 0.75, 7, true, 5000));
		ab.addCommand(new AutonWait(500));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL));
		ab.addCommand(new DriveWait());
		ab.addCommand(new DriveTurnToAngle(180 - 90, 5, 1000));
		ab.addCommand(new DriveToPoint(0.2, 7.0, 180 - 90, 0.0, 7, 0.25, 25, true, 5000));
		ab.addCommand(new IntakeAntiJamNoBump(1.0, 150));
		ab.addCommand(new DriveSetOutput(0.0));
		ab.addCommand(new IntakeWait());
		ab.addCommand(new DriveSetOutput(0));

		ab.addCommand(new DriveToPoint(2.05, 6.13, 116, 3, 6, 0.25, 25, true, 5000));
		ab.addCommand(new IntakeAntiJamClosed());
		ab.addCommand(new DriveToPoint(-7.43, 10.07, 149, 8, 10, 0.25, 25, true, 5000));
		ab.addCommand(new DriveToPoint(-9.76, 17.96, 90, 8, 10, 1.5,  25, true,5000));
		ab.addCommand(new DriveToPoint(-7.03, 27.88, 55, 2, 8, 0.25, 25, true, 5000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new AutonOverride(RobotComponent.INTAKE));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL));
		ab.addCommand(new DriveTurnToAngle(55, 1, 500));
		ab.addCommand(new DriveWait());

		ab.addCommand(new IntakeSetOutput(-0.8));
		ab.addCommand(new AutonWait(500));
		ab.addCommand(new IntakeSetOutput(0));
		ab.addCommand(new DriveSetOutput(-0.2));
		ab.addCommand(new AutonWait(1000));
		ab.addCommand(new DriveSetOutput(0));

	}
}
