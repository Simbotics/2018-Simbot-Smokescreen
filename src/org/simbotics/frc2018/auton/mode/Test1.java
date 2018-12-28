package org.simbotics.frc2018.auton.mode;

import org.simbotics.frc2018.auton.drive.DriveSetPosition;

public class Test1 implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {
		// go to score first cube
		ab.addCommand(new DriveSetPosition(0, 0, 90));

	}
}
