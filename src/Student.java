//class Student {
//    private final String ID;
//    private String name;
//    private final Module MODULE1;
//    private final Module MODULE2;
//    private final Module MODULE3;
//    private String marksAdded;
//
//    public Student(String id, String name, Module module1, Module module2, Module module3, String marksAdded) {
//        this.ID = id;
//        this.name = name;
//        this.MODULE1 = module1;
//        this.MODULE2 = module2;
//        this.MODULE3 = module3;
//        this.marksAdded = marksAdded;
//    }
//
//    public String getId() {
//        return ID;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Module getModule1() {
//        return MODULE1;
//    }
//
//    public Module getModule2() {
//        return MODULE2;
//    }
//
//    public Module getModule3() {
//        return MODULE3;
//    }
//
//    public void addingMarks(String marksAdded) {
//        this.marksAdded = marksAdded;
//    }
//
//    public String isMarkAdded() {
//        return marksAdded;
//    }
//
//    public double getAverage() {
//        double totalMarks = this.MODULE1.getMarks() + this.MODULE2.getMarks() + this.MODULE3.getMarks();
//        double averageMark = totalMarks / 3;
//        return roundAverage(averageMark);
//    }
//
//    private double roundAverage(double value) {
//        return Math.round(value * 100.0) / 100.0;
//    }
//
//    public String getGrade() {
//        if (isMarkAdded().equals("MarksNotAdded")){
//            return "NotCalculated";
//        }
//        double averageMark = this.getAverage();
//        if (averageMark >= 80) {
//            return "Distinct";
//        } else if (averageMark >= 70) {
//            return "Merit";
//        } else if (averageMark >= 40) {
//            return "Pass";
//        } else {
//            return "Fail";
//        }
//    }
//}


class Student {
    private final String ID;
    private String name;
    private final Module[] modules;
    private String marksAdded;

    public Student(String id, String name, Module[] modules, String marksAdded) {
        this.ID = id;
        this.name = name;
        this.modules = modules;
        this.marksAdded = marksAdded;
    }

    public String getId() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Module[] getModules() {
        return modules;
    }

    public Module getModule1() {
        return modules[0];
    }

    public Module getModule2() {
        return modules[1];
    }

    public Module getModule3() {
        return modules[2];
    }

    public void addingMarks(String marksAdded) {
        this.marksAdded = marksAdded;
    }

    public String isMarkAdded() {
        return marksAdded;
    }

    public double getAverage() {
        if (modules.length == 0) return 0.0;

        double totalMarks = 0;
        for (Module module : modules) {
            totalMarks += module.getMarks();
        }
        return roundAverage(totalMarks / modules.length);
    }

    private double roundAverage(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public String getGrade() {
        if (isMarkAdded().equals("MarksNotAdded")) {
            return "NotCalculated";
        }
        double averageMark = getAverage();
        if (averageMark >= 80) {
            return "Distinct";
        } else if (averageMark >= 70) {
            return "Merit";
        } else if (averageMark >= 40) {
            return "Pass";
        } else {
            return "Fail";
        }
    }
}
