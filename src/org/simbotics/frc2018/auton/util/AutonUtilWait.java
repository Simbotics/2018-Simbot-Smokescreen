package org.simbotics.frc2018.auton.util;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;

/**
 *
 * @author Michael
 */
public class AutonUtilWait extends AutonCommand {

	public AutonUtilWait() {
		super(RobotComponent.UTIL);
	}

	@Override
	public void firstCycle() {
		// nothing
	}

	@Override
	public boolean calculate() {
		return true;
	}

	@Override
	public void override() {
		// nothing to do

	}

}
