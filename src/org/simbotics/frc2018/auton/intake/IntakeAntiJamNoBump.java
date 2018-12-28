package org.simbotics.frc2018.auton.intake;

import org.simbotics.frc2018.auton.AutonCommand;
import org.simbotics.frc2018.auton.RobotComponent;
import org.simbotics.frc2018.io.RobotOutput;
import org.simbotics.frc2018.io.SensorInput;
import org.simbotics.frc2018.subsystems.Intake;
import org.simbotics.frc2018.subsystems.Intake.IntakeState;

public class IntakeAntiJamNoBump extends AutonCommand {

	private RobotOutput robotOut;
	private double output;
	private Intake intake;
	private int cyclesIntaking;
	private int maxCycles;

	public IntakeAntiJamNoBump() {
		this(1.0, 100);
	}

	public IntakeAntiJamNoBump(double maxOutput, int cycles) {
		super(RobotComponent.INTAKE, (cycles * 20) + 650);
		this.output = maxOutput;
		this.intake = Intake.getInstance();
		this.robotOut = RobotOutput.getInstance();
		this.intake.setCurrentState(IntakeState.OFF);
		this.intake.setUsingSensors(false, true, false);
		this.maxCycles = cycles;

	}

	@Override
	public boolean calculate() {
		this.cyclesIntaking++;

		this.intake.setAutoOutput(this.output);
		this.intake.calculate();
		
		if (this.cyclesIntaking > this.maxCycles) {
			System.out.println("PASSED MAX");
			this.intake.setUsingSensors(false, true, true); // close the intake now
		} else {
			System.out.println(this.intake.getIntakeState() == IntakeState.HAS_CUBE);
		}

		return this.intake.getIntakeState() == IntakeState.HAS_CUBE;

	}

	@Override
	public void firstCycle() {
		this.intake.firstCycle();
		this.intake.setCurrentState(IntakeState.OFF);
		this.intake.setDesiredState(IntakeState.INTAKE);
		this.intake.setUsingSensors(false, true, false);
	}

	@Override
	public void override() {
		this.robotOut.setIntakeLeft(0.0);
		this.robotOut.setIntakeRight(0.0);
		this.robotOut.setIntakeOpen(false);
	}
}
