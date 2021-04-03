package gui.panels;

import java.util.ArrayList;
import java.util.List;

import interfaces.ITracker;
import systems.trackers.*;

public enum TrackerChartConfiguration {
	Population {
		public Class<? extends ITracker> trackerType() {
			return PopulationTracker.class;
		}
		
		public ChartType getChartType() {
			return ChartType.Line;
		}
	},
	Size {
		public Class<? extends ITracker> trackerType() {
			return null;
		}
		
		public ChartType getChartType() {
			return ChartType.Line;
		}
	},
	Speed {
		public double getChartMinY() {
			return .5;
		}

		public double getChartMaxY() {
			return 1.5;
		}

		public boolean shouldAutoRange() {
			return false;
		}

		public Class<? extends ITracker> trackerType() {
			return AverageSpeedTracker.class;
		}
		
		public ChartType getChartType() {
			return ChartType.Line;
		}
	},
	AvailableFood {
		public String toString() {
			return "Available Food";
		}

		public Class<? extends ITracker> trackerType() {
			return AvailableFoodTracker.class;
		}
		
		public ChartType getChartType() {
			return ChartType.Line;
		}
	},
	Awareness {
		public Class<? extends ITracker> trackerType() {
			return null;
		}
		
		public ChartType getChartType() {
			return ChartType.Line;
		}
	},
	CauseOfDeath {
		public String toString() {
			return "Cause of Death";
		}

		public Class<? extends ITracker> trackerType() {
			return CauseOfDeathTracker.class;
		}

		public ChartType getChartType() {
			return ChartType.Bar;
		}
		
		public List<String> getCategories() {
			List<String> categories = new ArrayList<>();			
			for (models.entities.CauseOfDeath type : models.entities.CauseOfDeath.values()) {
				categories.add(type.toString());
			}
			return categories;
		}
	};

	abstract Class<? extends ITracker> trackerType();
	abstract ChartType getChartType();
	
	List<String> getCategories() {
		return null;
	}

	boolean shouldAutoRange() {
		return true;
	}

	double getChartMinY() {
		return 0;
	}

	double getChartMaxY() {
		return 1000;
	}
}