package org.simbotics.frc2018.auton.util;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.util.Debugger;

public class TestPrint extends AutonCommand {

	private String toPrint;

	public TestPrint(String s) {
		super(RobotComponent.UTIL);
		this.toPrint = s;
	}

	@Override
	public void firstCycle() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean calculate() {
		Debugger.println(this.toPrint);
		return true;
	}

	@Override
	public void override() {
		// TODO Auto-generated method stub

	}

}
