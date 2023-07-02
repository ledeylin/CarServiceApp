package other;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CarsBase {

    private static Set<String> carBrands = createCarBrandsSet();
    private static Set<String> carModels = createCarModelsSet();

    public static Set<String> createCarBrandsSet() {
        String[] carBrandsArray = {"Toyota", "Honda", "Ford", "Chevrolet", "Volkswagen", "BMW", "Mercedes-Benz",
                "Audi", "Nissan", "Hyundai", "Kia", "Volvo", "Subaru", "Mazda", "Lexus", "Jeep", "Tesla", "Land Rover",
                "Porsche", "Jaguar", "Ferrari", "Lamborghini", "Maserati", "Bentley", "Rolls-Royce", "Aston Martin",
                "McLaren", "Bugatti", "Lotus", "Alfa Romeo", "Fiat", "Chrysler", "Dodge", "Peugeot", "Renault",
                "Citroen", "Suzuki", "Mitsubishi", "Infiniti", "GMC", "Lincoln", "Geely", "Great Wall", "BYD", "Chery"};

        return new HashSet<>(Arrays.asList(carBrandsArray));
    }

    public static boolean validateCarMake(String carModel) {
        String[] modelParts = carModel.split(" ");
        String brand = modelParts[0];

        return carBrands.contains(brand);
    }

    public static Set<String> createCarModelsSet() {
        String[] carModelsArray = {
                "Camry", "Corolla", "RAV4", "Highlander", "Sienna", "Tacoma", "Tundra", // Toyota
                "Civic", "Accord", "CR-V", "Pilot", "Odyssey", // Honda
                "Mustang", "F-150", "Explorer", "Escape", "Focus", // Ford
                "Cruze", "Malibu", "Equinox", "Traverse", // Chevrolet
                "Golf", "Passat", "Tiguan", "Jetta", // Volkswagen
                "X3", "X5", "3 Series", "5 Series", "7 Series", // BMW
                "C-Class", "E-Class", "S-Class", // Mercedes-Benz
                "A4", "A6", "Q5", "Q7", // Audi
                "Altima", "Maxima", "Rogue", "Pathfinder", // Nissan
                "Sonata", "Elantra", "Tucson", "Santa Fe", // Hyundai
                "Optima", "Sorento", "Sportage", // Kia
                "S60", "S90", "XC40", "XC60", "XC90", // Volvo
                "Outback", "Forester", "Legacy", "Impreza", // Subaru
                "Mazda3", "Mazda6", "CX-5", "CX-9", // Mazda
                "RX", "GX", "NX", "ES", // Lexus
                "Wrangler", "Grand Cherokee", "Cherokee", // Jeep
                "Model 3", "Model S", "Model X", "Model Y", // Tesla
                "Range Rover", "Range Rover Sport", "Discovery", // Land Rover
                "911", "Cayenne", "Macan", "Panamera", // Porsche
                "F-Type", "XF", "XJ", // Jaguar
                "488 GTB", "812 Superfast", "Portofino", // Ferrari
                "Aventador", "Huracan", "Urus", // Lamborghini
                "Ghibli", "Levante", "Quattroporte", // Maserati
                "Continental GT", "Flying Spur", "Bentayga", // Bentley
                "Phantom", "Cullinan", "Ghost", // Rolls-Royce
                "DB11", "Vantage", "DBS Superleggera", // Aston Martin
                "570S", "720S", "P1", // McLaren
                "Chiron", "Veyron", "Divo", // Bugatti
                "Evora", "Elise", "Exige", // Lotus
                "Giulia", "Stelvio", "4C", // Alfa Romeo
                "500", "Panda", "Tipo", // Fiat
                "300", "Pacifica", "Voyager", // Chrysler
                "Challenger", "Charger", "Durango", // Dodge
                "208", "308", "508", // Peugeot
                "Clio", "Megane", "Captur", // Renault
                "C3", "C4", "C5", // Citroen
                "Swift", "Vitara", "Jimny", // Suzuki
                "Outlander", "Eclipse Cross", "Pajero", "Lancer", // Mitsubishi
                "Q50", "Q60", "Q70", "QX50", "QX60", "QX80", // Infiniti
                "Sierra 1500", "Sierra HD", "Yukon", "Acadia", "Terrain", // GMC
                "Navigator", "Aviator", "Corsair", "Nautilus", // Lincoln
                "Emgrand", "Coolray", "Bo Yue", "Vision", // Geely
                "Haval H6", "Haval H9", "Wingle", "Pao", // Great Wall
                "Tang", "Han", "Yuan", "Song Plus", // BYD
                "Arrizo", "Tiggo", "Exeed", // Chery
        };

        Set<String> carModelsSet = new HashSet<>();
        Collections.addAll(carModelsSet, carModelsArray);

        return carModelsSet;
    }

    public static boolean validateCarModel(String carModel) {
        return carModels.contains(carModel);
    }
}
