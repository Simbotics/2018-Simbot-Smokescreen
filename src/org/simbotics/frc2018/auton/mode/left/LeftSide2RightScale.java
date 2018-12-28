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

public class LeftSide2RightScale implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveSetPosition(0, 0, 90));
		
		ab.addCommand(new AutonWait(250));

		// Move past the switch
		// ab.addCommand(new DriveToPoint(0, 3.0, 90, 11, 11, 1.0, 7, false, 5000));

		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.STANDBY, 1.0, true, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.STANDBY, true));

		ab.addCommand(new DriveToPoint(-1.0, 13.3,180- 90, 11, 11, 1.0, 5, false, 5000));
		ab.addCommand(new DriveToPoint(1, 17.3, 180-135, 11, 11, 1.0, 15, false, 5000));

		// Drive across the field
		ab.addCommand(new DriveToPoint(17.3, 20.6, 180-176, 7, 11.0, 1.0, 7, false, 5000));
		ab.addCommand(new DriveWait());
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL, 1.0, true, 2500));

		// Point towards the scale
		ab.addCommand(new DriveToPoint(16.9, 23.6, 180-75, 0, 4, 0.5, 2000));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL, true));
		ab.addCommand(new DriveWait());

		// Score first cube
		ab.addCommand(new IntakeSetOutput(-0.7));
		ab.addCommand(new AutonWait(650));
		ab.addCommand(new IntakeSetOutput(0));

		// go to second cube
		ab.addCommand(new DriveToPoint(19.5, 21.5, 180-75, 0.5, 5, 0.25, 5000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 1.0, true, 3000));
		ab.addCommand(new AutonWait(500));
		ab.addCommand(new IntakeAntiJamNoBump(1.0, 140));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, true));
		ab.addCommand(new DriveToPoint(16.45, 17.7, 180+30, 0, 4, 0.25, 7, true, 2500));
		ab.addCommand(new DriveSetOutput(0.1));
		ab.addCommand(new IntakeWait());
		ab.addCommand(new AutonOverride(RobotComponent.DRIVE));

		// Score 2nd cube
		ab.addCommand(new DriveToPoint(18.0, 21.5, 180+45, 0, 7, 0.5, 5000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.STANDBY, 1.0, true, 5000));
		ab.addCommand(new DriveToPoint(17.5, 24.0, 180-70, 0, 3, 0.5, 5000));

		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL, 1.0, true, 5000));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.SCALE_UP, WristPosition.LEVEL, true));
		ab.addCommand(new DriveWait());
		ab.addCommand(new IntakeSetOutput(-0.6));
		ab.addCommand(new AutonWait(650));
		ab.addCommand(new IntakeSetOutput(0));

		// back up
		ab.addCommand(new DriveToPoint(18.5, 21.5, 180-75, 0.5, 6, 0.25, 5000));
		ab.addCommand(new AutonOverride(RobotComponent.LIFT));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, 0.7, true, 3000));
		ab.addCommand(new AutonWait(500));
		ab.addCommand(new IntakeAntiJamNoBump(1.0, 140));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.LEVEL, true));
		ab.addCommand(new DriveToPoint(14.75, 19.2, 180+30, 0, 4, 0.25, 7, true, 2500));
		ab.addCommand(new DriveWait());

	}
}
