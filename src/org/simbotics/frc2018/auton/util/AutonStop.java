package org.simbotics.frc2018.auton.util;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.AutonControl;
import org.simbotics.frc2018.auton.RobotComponent;

/**
 *
 * @author Michael
 */
public class AutonStop extends AutonCommand {

	public AutonStop() {
		super(RobotComponent.UTIL);
	}

	@Override
	public void firstCycle() {
		// nothing
	}

	@Override
	public boolean calculate() {
		AutonControl.getInstance().stop();
		return true;
	}

	@Override
	public void override() {
		// nothing to do

	}

}
