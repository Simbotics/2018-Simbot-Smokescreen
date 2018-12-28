package org.simbotics.frc2018.auton.drive;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;

public class DriveWait extends AutonCommand {

	public DriveWait() {
		super(RobotComponent.DRIVE);
	}

	@Override
	public boolean calculate() {
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub

	}

	@Override
	public void firstCycle() {
		// TODO Auto-generated method stub

	}

}
