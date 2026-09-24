import java.util.*;

/* =====================================================================================
 * SMART BLOOD BANK MANAGEMENT SYSTEM
 * PBL Capstone Project - Object Oriented Techniques using Java (CCSE0355)
 * Group 13
 *
 * OOP Concepts demonstrated:
 *  1. ABSTRACTION      -> abstract classes Person, BloodRequest
 *  2. ENCAPSULATION    -> private fields with getters/setters in every class
 *  3. INHERITANCE      -> Person -> Donor / Staff -> Admin
 *                         BloodRequest -> EmergencyRequest / RoutineRequest
 *  4. POLYMORPHISM     -> processRequest() and getRole()/getPriority() behave
 *                         differently depending on the actual object type
 *  5. EXCEPTION HANDLING -> BloodNotAvailableException, InvalidDonorException
 *  6. COLLECTIONS      -> ArrayList (donors, staff, requests) and
 *                         HashMap (blood-group wise inventory)
 * =====================================================================================
 */

// ---------------------------- CUSTOM EXCEPTIONS ----------------------------

class BloodNotAvailableException extends Exception {
    public BloodNotAvailableException(String message) {
        super(message);
    }
}

class InvalidDonorException extends Exception {
    public InvalidDonorException(String message) {
        super(message);
    }
}

// ---------------------------- BASE CLASS (ABSTRACTION) ----------------------------

abstract class Person {
    private String id;
    private String name;
    private String contact;

    public Person(String id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    // Abstract method -> forces every subclass to define its own role
    public abstract String getRole();

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Contact: " + contact + " | Role: " + getRole();
    }
}

// ---------------------------- DONOR (INHERITANCE) ----------------------------

class Donor extends Person {
    private String bloodGroup;
    private Date lastDonationDate;
    private int totalDonations;

    public Donor(String id, String name, String contact, String bloodGroup) {
        super(id, name, contact);
        this.bloodGroup = bloodGroup;
        this.totalDonations = 0;
        this.lastDonationDate = null;
    }

    public String getBloodGroup() { return bloodGroup; }
    public int getTotalDonations() { return totalDonations; }

    // Business rule: a donor must wait 90 days between donations
    public boolean isEligibleToDonate() {
        if (lastDonationDate == null) return true;
        long diffMillis = new Date().getTime() - lastDonationDate.getTime();
        long diffDays = diffMillis / (1000L * 60 * 60 * 24);
        return diffDays >= 90;
    }

    public void recordDonation() {
        this.lastDonationDate = new Date();
        this.totalDonations++;
    }

    @Override
    public String getRole() { return "Donor"; }

    @Override
    public String toString() {
        return super.toString() + " | BloodGroup: " + bloodGroup
                + " | TotalDonations: " + totalDonations
                + " | EligibleNow: " + isEligibleToDonate();
    }
}

// ---------------------------- STAFF / ADMIN (MULTI-LEVEL INHERITANCE) ----------------------------

class Staff extends Person {
    private String department;

    public Staff(String id, String name, String contact, String department) {
        super(id, name, contact);
        this.department = department;
    }

    public String getDepartment() { return department; }

    @Override
    public String getRole() { return "Staff"; }
}

class Admin extends Staff {
    private String privilegeLevel;

    public Admin(String id, String name, String contact, String privilegeLevel) {
        super(id, name, contact, "Administration");
        this.privilegeLevel = privilegeLevel;
    }

    public String getPrivilegeLevel() { return privilegeLevel; }

    @Override
    public String getRole() { return "Admin"; }

    // Admin has the authority to approve/reject a pending request
    public boolean approveRequest(BloodRequest request, BloodBankInventory inventory) {
        try {
            inventory.issueUnits(request.getBloodGroup(), request.getUnitsRequired());
            request.setStatus("APPROVED");
            System.out.println("[Admin " + getName() + "] Approved " + request.getRequestId());
            return true;
        } catch (BloodNotAvailableException e) {
            request.setStatus("REJECTED");
            System.out.println("[Admin " + getName() + "] Rejected " + request.getRequestId()
                    + " -> " + e.getMessage());
            return false;
        }
    }
}

// ---------------------------- BLOOD UNIT ----------------------------

class BloodUnit {
    private static int counter = 1000;
    private final String unitId;
    private final String bloodGroup;
    private final Date collectionDate;
    private final Date expiryDate;
    private String status; // AVAILABLE, ISSUED, EXPIRED

    public BloodUnit(String bloodGroup) {
        this.unitId = "BU-" + (counter++);
        this.bloodGroup = bloodGroup;
        this.collectionDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(collectionDate);
        cal.add(Calendar.DATE, 42); // standard whole-blood shelf life
        this.expiryDate = cal.getTime();
        this.status = "AVAILABLE";
    }

    public String getUnitId() { return unitId; }
    public String getBloodGroup() { return bloodGroup; }
    public Date getExpiryDate() { return expiryDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isExpired() { return new Date().after(expiryDate); }

    @Override
    public String toString() {
        return unitId + " [" + bloodGroup + "] Status: " + status + (isExpired() ? " (EXPIRED)" : "");
    }
}

// ---------------------------- HOSPITAL ----------------------------

class Hospital {
    private final String name;
    private final String location;

    public Hospital(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
}

// ---------------------------- REQUEST HIERARCHY (ABSTRACTION + POLYMORPHISM) ----------------------------

abstract class BloodRequest {
    private static int counter = 1;
    private final String requestId;
    private final Hospital hospital;
    private final String bloodGroup;
    private final int unitsRequired;
    private String status;

    public BloodRequest(Hospital hospital, String bloodGroup, int unitsRequired) {
        this.requestId = "REQ-" + (counter++);
        this.hospital = hospital;
        this.bloodGroup = bloodGroup;
        this.unitsRequired = unitsRequired;
        this.status = "PENDING";
    }

    public String getRequestId() { return requestId; }
    public Hospital getHospital() { return hospital; }
    public String getBloodGroup() { return bloodGroup; }
    public int getUnitsRequired() { return unitsRequired; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Every subclass processes a request differently -> runtime polymorphism
    public abstract void processRequest(BloodBankInventory inventory) throws BloodNotAvailableException;
    public abstract String getPriority();

    @Override
    public String toString() {
        return requestId + " [" + getPriority() + "] Hospital: " + hospital.getName()
                + " | BloodGroup: " + bloodGroup + " | Units: " + unitsRequired
                + " | Status: " + status;
    }
}

class EmergencyRequest extends BloodRequest {
    public EmergencyRequest(Hospital hospital, String bloodGroup, int unitsRequired) {
        super(hospital, bloodGroup, unitsRequired);
    }

    @Override
    public String getPriority() { return "EMERGENCY"; }

    @Override
    public void processRequest(BloodBankInventory inventory) throws BloodNotAvailableException {
        System.out.println("Processing EMERGENCY request " + getRequestId() + " with top priority...");
        inventory.issueUnits(getBloodGroup(), getUnitsRequired());
        setStatus("APPROVED");
    }
}

class RoutineRequest extends BloodRequest {
    public RoutineRequest(Hospital hospital, String bloodGroup, int unitsRequired) {
        super(hospital, bloodGroup, unitsRequired);
    }

    @Override
    public String getPriority() { return "ROUTINE"; }

    @Override
    public void processRequest(BloodBankInventory inventory) throws BloodNotAvailableException {
        System.out.println("Processing routine request " + getRequestId() + " through the normal queue...");
        inventory.checkAvailability(getBloodGroup(), getUnitsRequired());
        inventory.issueUnits(getBloodGroup(), getUnitsRequired());
        setStatus("APPROVED");
    }
}

// ---------------------------- INVENTORY (ENCAPSULATION + COLLECTIONS) ----------------------------

class BloodBankInventory {
    private final Map<String, List<BloodUnit>> stock;

    public BloodBankInventory() {
        stock = new HashMap<>();
        String[] groups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String g : groups) stock.put(g, new ArrayList<>());
    }

    public void addUnit(BloodUnit unit) {
        stock.get(unit.getBloodGroup()).add(unit);
    }

    public int getAvailableCount(String bloodGroup) {
        int count = 0;
        for (BloodUnit u : stock.get(bloodGroup)) {
            if (u.getStatus().equals("AVAILABLE") && !u.isExpired()) count++;
        }
        return count;
    }

    public void checkAvailability(String bloodGroup, int required) throws BloodNotAvailableException {
        int available = getAvailableCount(bloodGroup);
        if (available < required) {
            throw new BloodNotAvailableException(
                    "Only " + available + " unit(s) of " + bloodGroup + " available, " + required + " required.");
        }
    }

    public void issueUnits(String bloodGroup, int required) throws BloodNotAvailableException {
        checkAvailability(bloodGroup, required);
        int issued = 0;
        for (BloodUnit u : stock.get(bloodGroup)) {
            if (issued == required) break;
            if (u.getStatus().equals("AVAILABLE") && !u.isExpired()) {
                u.setStatus("ISSUED");
                issued++;
            }
        }
    }

    public void refreshExpiredUnits() {
        for (String g : stock.keySet()) {
            for (BloodUnit u : stock.get(g)) {
                if (u.isExpired() && !u.getStatus().equals("EXPIRED")) {
                    u.setStatus("EXPIRED");
                }
            }
        }
    }

    public void displayInventory() {
        refreshExpiredUnits();
        System.out.println("\n----- Blood Inventory (available units) -----");
        for (String g : stock.keySet()) {
            System.out.printf("%-4s : %d unit(s)%n", g, getAvailableCount(g));
        }
    }
}

// ---------------------------- CORE SYSTEM MANAGER ----------------------------

class BloodBankSystem {
    private final List<Donor> donors = new ArrayList<>();
    private final List<Staff> staffList = new ArrayList<>();
    private final List<BloodRequest> requests = new ArrayList<>();
    private final BloodBankInventory inventory = new BloodBankInventory();

    public void registerDonor(Donor d) throws InvalidDonorException {
        if (d.getName() == null || d.getName().trim().isEmpty()) {
            throw new InvalidDonorException("Donor name cannot be empty.");
        }
        donors.add(d);
        System.out.println("Donor registered: " + d.getName() + " (" + d.getBloodGroup() + ")");
    }

    public void addStaff(Staff s) {
        staffList.add(s);
        System.out.println(s.getRole() + " added: " + s.getName());
    }

    public void donateBlood(Donor d) throws InvalidDonorException {
        if (!d.isEligibleToDonate()) {
            throw new InvalidDonorException(d.getName() + " is not eligible to donate yet (90-day rule).");
        }
        BloodUnit unit = new BloodUnit(d.getBloodGroup());
        inventory.addUnit(unit);
        d.recordDonation();
        System.out.println(d.getName() + " donated blood. New unit created: " + unit.getUnitId());
    }

    // Accepts ANY BloodRequest (Emergency or Routine) -> polymorphic call
    public void raiseRequest(BloodRequest request) {
        requests.add(request);
        try {
            request.processRequest(inventory);
            System.out.println("Request " + request.getRequestId() + " -> Status: " + request.getStatus());
        } catch (BloodNotAvailableException e) {
            request.setStatus("REJECTED");
            System.out.println("Request " + request.getRequestId() + " REJECTED -> " + e.getMessage());
        }
    }

    public void listDonors() {
        System.out.println("\n----- Registered Donors -----");
        if (donors.isEmpty()) System.out.println("(none yet)");
        for (Donor d : donors) System.out.println(d);
    }

    public void listStaff() {
        System.out.println("\n----- Staff / Admin -----");
        if (staffList.isEmpty()) System.out.println("(none yet)");
        for (Staff s : staffList) System.out.println(s);
    }

    public void listRequests() {
        System.out.println("\n----- Blood Requests -----");
        if (requests.isEmpty()) System.out.println("(none yet)");
        for (BloodRequest r : requests) System.out.println(r);
    }

    public Donor findDonorById(String id) {
        for (Donor d : donors) if (d.getId().equalsIgnoreCase(id)) return d;
        return null;
    }

    public BloodRequest findPendingRequestById(String id) {
        for (BloodRequest r : requests)
            if (r.getRequestId().equalsIgnoreCase(id) && r.getStatus().equals("PENDING")) return r;
        return null;
    }

    public List<BloodRequest> getRequests() { return requests; }
    public BloodBankInventory getInventory() { return inventory; }
}

// ---------------------------- DRIVER CLASS (MENU-DRIVEN CONSOLE APP) ----------------------------

public class SmartBloodBankManagementSystem {

    private static final Scanner sc = new Scanner(System.in);
    private static final BloodBankSystem system = new BloodBankSystem();
    private static Admin defaultAdmin;

    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("   SMART BLOOD BANK MANAGEMENT SYSTEM (OOP in Java)");
        System.out.println("=====================================================");

        loadSampleData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1: registerDonorFlow(); break;
                case 2: donateBloodFlow(); break;
                case 3: raiseRequestFlow(); break;
                case 4: approveRequestFlow(); break;
                case 5: system.getInventory().displayInventory(); break;
                case 6: system.listDonors(); break;
                case 7: system.listRequests(); break;
                case 8: system.listStaff(); break;
                case 0: running = false; System.out.println("Exiting... Thank you!"); break;
                default: System.out.println("Invalid choice, try again.");
            }
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n--------------- MAIN MENU ---------------");
        System.out.println("1. Register New Donor");
        System.out.println("2. Donate Blood (adds a Blood Unit)");
        System.out.println("3. Raise Blood Request (Hospital)");
        System.out.println("4. Approve a Pending Request (Admin)");
        System.out.println("5. View Inventory");
        System.out.println("6. View Donors");
        System.out.println("7. View Requests");
        System.out.println("8. View Staff/Admin");
        System.out.println("0. Exit");
    }

    private static void loadSampleData() {
        try {
            defaultAdmin = new Admin("A01", "Mr.Ankit Dhaka","9990001111","SUPER_ADMIN");
            system.addStaff(defaultAdmin);
            system.addStaff(new Staff("S01", "Archyansh Vishwakarma", "9990002222", "Front Desk"));

            system.registerDonor(new Donor("D01", "Dheeraj Tiwari", "9990003333", "O+"));
            system.registerDonor(new Donor("D02", "Anmol Singh Verma", "9990004444", "B+"));
            system.registerDonor(new Donor("D03", "Dev Raj Gupta", "9990005555", "O-"));
            system.registerDonor(new Donor("D04", "Ayush Singh", "9990006666", "A+"));

            // Seed a little initial stock so requests have something to match against
            system.donateBlood(system.findDonorById("D01"));
            system.donateBlood(system.findDonorById("D02"));
            system.donateBlood(system.findDonorById("D03"));
            system.donateBlood(system.findDonorById("D04"));
            System.out.println("(Sample donors, staff and initial stock loaded.)\n");
        } catch (InvalidDonorException e) {
            System.out.println("Setup error: " + e.getMessage());
        }
    }

    private static void registerDonorFlow() {
        try {
            System.out.print("Donor ID: ");
            String id = sc.nextLine();
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Contact: ");
            String contact = sc.nextLine();
            System.out.print("Blood Group (e.g. A+, O-): ");
            String bg = sc.nextLine();
            system.registerDonor(new Donor(id, name, contact, bg));
        } catch (InvalidDonorException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void donateBloodFlow() {
        System.out.print("Enter Donor ID: ");
        String id = sc.nextLine();
        Donor d = system.findDonorById(id);
        if (d == null) {
            System.out.println("No donor found with ID " + id);
            return;
        }
        try {
            system.donateBlood(d);
        } catch (InvalidDonorException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void raiseRequestFlow() {
        System.out.print("Hospital Name: ");
        String hName = sc.nextLine();
        System.out.print("Hospital Location: ");
        String hLoc = sc.nextLine();
        Hospital hospital = new Hospital(hName, hLoc);

        System.out.print("Blood Group Needed: ");
        String bg = sc.nextLine();
        int units = readInt("Units Required: ");

        System.out.print("Request Type (1 = Emergency, 2 = Routine): ");
        String type = sc.nextLine();

        BloodRequest request;
        if (type.trim().equals("1")) {
            request = new EmergencyRequest(hospital, bg, units);
        } else {
            request = new RoutineRequest(hospital, bg, units);
        }
        // Polymorphic call: the correct processRequest() runs automatically
        system.raiseRequest(request);
    }

    private static void approveRequestFlow() {
        System.out.print("Enter Request ID to approve (e.g. REQ-1): ");
        String id = sc.nextLine();
        BloodRequest r = system.findPendingRequestById(id);
        if (r == null) {
            System.out.println("No PENDING request found with ID " + id);
            return;
        }
        defaultAdmin.approveRequest(r, system.getInventory());
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine(); // consume newline
        return val;
    }
}
