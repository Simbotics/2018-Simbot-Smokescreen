package org.simbotics.frc2018.auton;

import org.simbotics.frc2018.auton.mode.AutonMode;
import org.simbotics.frc2018.auton.mode.DefaultMode;
import org.simbotics.frc2018.io.SensorInput;

import edu.wpi.first.wpilibj.DriverStation;

public class AutonModeSet {
	private String name;
	private AutonMode rrMode;
	private AutonMode rlMode;
	private AutonMode lrMode;
	private AutonMode llMode;

	public AutonModeSet(String name, AutonMode rr, AutonMode rl, AutonMode lr, AutonMode ll) {
		this.name = name;
		this.rrMode = rr;
		this.rlMode = rl;
		this.lrMode = lr;
		this.llMode = ll;
	}

	public AutonModeSet(AutonModeSet other) {
		this.name = other.getName();
		this.rrMode = other.rrMode;
		this.llMode = other.llMode;
		this.rlMode = other.rlMode;
		this.lrMode = other.lrMode;
	}

	public AutonMode getMode() {
		AutonMode mode = new DefaultMode();
		boolean waitingForString = true;
		while (DriverStation.getInstance().isAutonomous() && waitingForString) {
			String fieldChoice = SensorInput.getInstance().getFieldConfig();
			fieldChoice = fieldChoice.toUpperCase();

			if (fieldChoice.length() == 3) {
				if (fieldChoice.startsWith("RR")) {
					mode = this.rrMode;
					waitingForString = false;
				} else if (fieldChoice.startsWith("RL")) {
					mode = this.rlMode;
					waitingForString = false;
				} else if (fieldChoice.startsWith("LR")) {
					mode = this.lrMode;
					waitingForString = false;
				} else if (fieldChoice.startsWith("LL")) {
					mode = this.llMode;
					waitingForString = false;
				} else {
					System.out.print("INVALID FIELD STRING - " + fieldChoice);
					System.out.print("DOESNT START WITH RR/RL/LR/LL");
				}

			} else {
				System.out.print("INVALID FIELD STRING - " + fieldChoice);
				System.out.print("NOT 3 LONG");
			}

		}

		return mode;
	}

	public boolean equals(AutonModeSet other) {

		return (other != null && this.rrMode.getClass() == other.rrMode.getClass()
				&& this.llMode.getClass() == other.llMode.getClass()
				&& this.lrMode.getClass() == other.lrMode.getClass()
				&& this.rlMode.getClass() == other.rlMode.getClass());

	}

	public AutonMode getRRMode() {
		return rrMode;
	}

	public void setRRMode(AutonMode rrMode) {
		this.rrMode = rrMode;
	}

	public AutonMode getRLMode() {
		return rlMode;
	}

	public void setRLMode(AutonMode rlMode) {
		this.rlMode = rlMode;
	}

	public AutonMode getLRMode() {
		return lrMode;
	}

	public void setLRMode(AutonMode lrMode) {
		this.lrMode = lrMode;
	}

	public AutonMode getLLMode() {
		return llMode;
	}

	public void setLLMode(AutonMode llMode) {
		this.llMode = llMode;
	}

	public String getName() {
		return name;
	}
}
