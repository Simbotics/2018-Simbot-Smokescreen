package org.simbotics.frc2018.auton.intake;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;

public class IntakeClose extends AutonCommand {

	private RobotOutput robotOut;

	public IntakeClose() {
		super(RobotComponent.INTAKE);
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public void firstCycle() {

	}

	@Override
	public boolean calculate() {
		this.robotOut.setIntakeOpen(false);
		this.robotOut.setIntakePancakeOpen(true);
		return true;
	}

	@Override
	public void override() {

	}
}