package com.military.assetmanagement;

import com.military.assetmanagement.model.*;
import com.military.assetmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BaseRepository baseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AssetBalanceRepository assetBalanceRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private TransferRepository transferRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role(RoleName.ROLE_ADMIN);
            Role baseCmdRole = new Role(RoleName.ROLE_BASE_COMMANDER);
            Role logOfficerRole = new Role(RoleName.ROLE_LOGISTICS_OFFICER);
            roleRepository.saveAll(Arrays.asList(adminRole, baseCmdRole, logOfficerRole));

            Base baseAlpha = new Base();
            baseAlpha.setName("Camp Alpha");
            baseAlpha.setLocation("Sector 1");
            
            Base baseBravo = new Base();
            baseBravo.setName("Fort Bravo");
            baseBravo.setLocation("Sector 2");
            
            Base baseCharlie = new Base();
            baseCharlie.setName("Outpost Charlie");
            baseCharlie.setLocation("Sector 3");
            
            baseRepository.saveAll(Arrays.asList(baseAlpha, baseBravo, baseCharlie));

            User admin = new User("General Admin", "admin@military.com", passwordEncoder.encode("admin123"));
            admin.setRoles(new HashSet<>(Arrays.asList(adminRole)));
            
            User cmdAlpha = new User("Commander Alpha", "cmd.alpha@military.com", passwordEncoder.encode("cmd123"));
            cmdAlpha.setRoles(new HashSet<>(Arrays.asList(baseCmdRole)));
            cmdAlpha.setAssignedBase(baseAlpha);
            
            User logBravo = new User("Logistics Bravo", "log.bravo@military.com", passwordEncoder.encode("log123"));
            logBravo.setRoles(new HashSet<>(Arrays.asList(logOfficerRole)));
            logBravo.setAssignedBase(baseBravo);
            
            userRepository.saveAll(Arrays.asList(admin, cmdAlpha, logBravo));

            Equipment humvee = new Equipment();
            humvee.setName("M998 Humvee");
            humvee.setCategory(EquipmentCategory.VEHICLES);
            
            Equipment m4 = new Equipment();
            m4.setName("M4 Carbine");
            m4.setCategory(EquipmentCategory.WEAPONS);
            
            Equipment ammo556 = new Equipment();
            ammo556.setName("5.56mm Ammunition");
            ammo556.setCategory(EquipmentCategory.AMMUNITION);
            
            Equipment radio = new Equipment();
            radio.setName("Tactical Radio");
            radio.setCategory(EquipmentCategory.OTHER_EQUIPMENT);
            
            equipmentRepository.saveAll(Arrays.asList(humvee, m4, ammo556, radio));
            
            // Seed Asset Balances
            AssetBalance b1 = new AssetBalance();
            b1.setBase(baseAlpha);
            b1.setEquipment(humvee);
            b1.setOpeningBalance(10);
            b1.setTotalPurchases(5);
            b1.setTransfersIn(2);
            b1.setTransfersOut(1);
            b1.setAssignedAssets(4);
            b1.setExpendedAssets(1);
            b1.setCurrentBalance(11);
            
            AssetBalance b2 = new AssetBalance();
            b2.setBase(baseBravo);
            b2.setEquipment(m4);
            b2.setOpeningBalance(100);
            b2.setTotalPurchases(50);
            b2.setTransfersIn(0);
            b2.setTransfersOut(10);
            b2.setAssignedAssets(80);
            b2.setExpendedAssets(5);
            b2.setCurrentBalance(55);

            AssetBalance b3 = new AssetBalance();
            b3.setBase(baseCharlie);
            b3.setEquipment(ammo556);
            b3.setOpeningBalance(5000);
            b3.setTotalPurchases(2000);
            b3.setTransfersIn(1000);
            b3.setTransfersOut(500);
            b3.setAssignedAssets(2000);
            b3.setExpendedAssets(1500);
            b3.setCurrentBalance(4000);

            assetBalanceRepository.saveAll(Arrays.asList(b1, b2, b3));
            
            // Seed some purchases
            Purchase p1 = new Purchase();
            p1.setBase(baseAlpha);
            p1.setEquipment(humvee);
            p1.setQuantity(5);
            p1.setPurchaseDate(java.time.LocalDate.now().minusDays(10));
            p1.setSupplierDetails("AM General");
            p1.setUnitCost(220000.0);
            purchaseRepository.save(p1);

            System.out.println("Database seeded successfully!");
        }
    }
}
