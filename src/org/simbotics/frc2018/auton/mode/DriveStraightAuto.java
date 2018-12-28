package org.simbotics.frc2018.auton.mode;

import org.simbotics.frc2018.auton.drive.DriveStraight;
import org.simbotics.frc2018.auton.drive.DriveWait;

public class DriveStraightAuto implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		ab.addCommand(new DriveStraight(9.25, 90, 5000));
		ab.addCommand(new DriveWait());
	}
}
