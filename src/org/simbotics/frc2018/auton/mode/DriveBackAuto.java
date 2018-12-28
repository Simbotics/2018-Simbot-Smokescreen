package org.simbotics.frc2018.auton.mode;

import org.simbotics.frc2018.auton.drive.DriveSetPosition;
import org.simbotics.frc2018.auton.drive.DriveStraight;
import org.simbotics.frc2018.auton.drive.DriveWait;

public class DriveBackAuto implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveSetPosition(0, 0, 270));

		ab.addCommand(new DriveStraight(-9.25, 270, 5000));
		ab.addCommand(new DriveWait());
	}
}
