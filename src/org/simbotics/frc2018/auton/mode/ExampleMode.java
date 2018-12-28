package org.simbotics.frc2018.auton.mode;

import org.simbotics.frc2018.auton.drive.DriveSetOutput;
import org.simbotics.frc2018.auton.drive.DriveTurnTimer;
import org.simbotics.frc2018.auton.util.AutonWait;

public class ExampleMode implements AutonMode {

	@Override
	public void addToMode(AutonBuilder ab) {

		ab.addCommand(new DriveTurnTimer(0.8, 3000));
		ab.addCommand(new DriveTurnTimer(-0.5, 2000));
		ab.addCommand(new DriveSetOutput(0.3));
		ab.addCommand(new AutonWait(5000));

	}

}
