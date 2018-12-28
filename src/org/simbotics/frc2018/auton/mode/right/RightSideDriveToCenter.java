package org.simbotics.frc2018.auton.mode.right;

import org.simbotics.frc2018.auton.drive.DriveSetPosition;
import org.simbotics.frc2018.auton.drive.DriveToPoint;
import org.simbotics.frc2018.auton.drive.DriveTurnToAngle;
import org.simbotics.frc2018.auton.drive.DriveWait;
import org.simbotics.frc2018.auton.lift.LiftHoldPosition;
import org.simbotics.frc2018.auton.lift.LiftSetPosition;
import org.simbotics.frc2018.auton.mode.AutonBuilder;
import org.simbotics.frc2018.auton.mode.AutonMode;
import org.simbotics.frc2018.subsystems.Lift.ElevatorPosition;
import org.simbotics.frc2018.subsystems.Lift.WristPosition;

public class RightSideDriveToCenter implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		// Drive past the switch
		ab.addCommand(new DriveSetPosition(0, 0, 90));
		ab.addCommand(new DriveToPoint(0, 18.8, 90, 2.5, 11, 1.0, 25, true, 5000));
		ab.addCommand(new LiftSetPosition(ElevatorPosition.GROUND, WristPosition.STANDBY, 20));
		ab.addCommand(new LiftHoldPosition(ElevatorPosition.GROUND, WristPosition.STANDBY));
		ab.addCommand(new DriveToPoint(-1, 20.3, 135, 3, 7, 1.0, 25, true, 5000));
		// Drive across the field
		ab.addCommand(new DriveToPoint(-8.25, 20.6, 180, 0, 10.5, 1.0, 25, true, 5000));
		ab.addCommand(new DriveTurnToAngle(180, 0.25, 5000));
		ab.addCommand(new DriveWait());

	}

}
