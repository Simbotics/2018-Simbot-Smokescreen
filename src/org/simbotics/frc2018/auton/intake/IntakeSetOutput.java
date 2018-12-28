package org.simbotics.frc2018.auton.intake;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;

public class IntakeSetOutput extends AutonCommand {

	private double speed;
	private RobotOutput robotOut;

	public IntakeSetOutput(double speed) {
		super(RobotComponent.INTAKE);
		this.speed = speed;
		this.robotOut = RobotOutput.getInstance();
	}

	@Override
	public void firstCycle() {

	}

	@Override
	public boolean calculate() {
		if (this.speed < 0) {
			this.robotOut.setIntakePancakeOpen(true);
		}
		this.robotOut.setIntakeLeft(this.speed);
		this.robotOut.setIntakeRight(this.speed);
		this.robotOut.setLed(false);
		return true;
	}

	@Override
	public void override() {
		this.robotOut.setIntakeLeft(0);
		this.robotOut.setIntakeRight(0);

	}

}
