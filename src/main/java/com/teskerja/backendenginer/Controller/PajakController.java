package com.teskerja.backendenginer.Controller;

import java.text.DecimalFormat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teskerja.backendenginer.Model.Entity.Component;
import com.teskerja.backendenginer.Model.Entity.Employee;
import com.teskerja.backendenginer.Model.Repository.SalaryRequest;

@RestController
public class PajakController {

    @PostMapping("/hitungpajak")
    public ResponseEntity<String> hitungPajak(@RequestBody SalaryRequest salaryRequest) {
        Employee employee = salaryRequest.getEmployee();
        String country = employee.getCountry();
        int simbol = 0;
        double totalEarnings = 0;
        double totalDeductions = 0;

        // Menentukan status keluarga
        if (employee.getMaritalStatus().equals("maried") && employee.getChilds() >= 1) {
            simbol = 2;
        } else if (employee.getMaritalStatus().equals("maried") && employee.getChilds() < 1) {
            simbol = 1;
        } else {
            simbol = 0;
        }

        // Menghitung total pendapatan dan potongan
        for (Component component : salaryRequest.getKomponengaji()) {
            if (component.getType().equals("earning")) {
                totalEarnings += component.getAmount();
            } else if (component.getType().equals("deduction")) {
                totalDeductions += component.getAmount();
            }
        }

        // Membuat objek HitungPajak sesuai dengan negara
        HitungPajak hitungPajak = HitungPajakFactory.createHitungPajak(country);

        // Menghitung penghasilan netto berdasarkan negara
        double penghasilanNetto = hitungPajak.hitungNetIncome(totalEarnings, totalDeductions, simbol);
        double totalPajak = hitungPajak.hitungPajak(penghasilanNetto);

        // Format angka dengan pemisah ribuan
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String totalPajakFormatted = decimalFormat.format(totalPajak);
        String penghasilanNettoFormatted = decimalFormat.format(penghasilanNetto);
        String totalEarningsFormatted = decimalFormat.format(totalEarnings);

        // Tanda nominal berdasarkan negara
        String tandaNominal = getTandaNominal(country);

        // Membuat response
        String response = "Total pajak bulan ini: " + tandaNominal + totalPajakFormatted +
                "\nPenghasilan Netto: " + tandaNominal + penghasilanNettoFormatted +
                "\nTotal Earnings: " + tandaNominal + totalEarningsFormatted;

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Mendapatkan tanda nominal berdasarkan negara
    private String getTandaNominal(String country) {
        if (country.equalsIgnoreCase("indonesia")) {
            return "IDR ";
        } else if (country.equalsIgnoreCase("vietnam")) {
            return "VDN ";
        }
        return "";
    }

    // HitungPajak interface
    interface HitungPajak {
        double hitungNetIncome(double totalEarnings, double totalDeductions, int simbol);

        double hitungPajak(double penghasilanNetto);
    }

    // IndonesiaHitungPajak
    static class IndonesiaHitungPajak implements HitungPajak {
        private static final double TK = 25000000;
        private static final double K0 = 50000000;
        private static final double K1 = 75000000;

        @Override
        public double hitungNetIncome(double totalEarnings, double totalDeductions, int simbol) {
            double ptkp = getPTKP(simbol);
            return (totalEarnings * 12) - ptkp;
        }

        @Override
        public double hitungPajak(double penghasilanNetto) {
            double pajak = 0;
            if (penghasilanNetto <= 50000000) {
                pajak = penghasilanNetto * 0.05;
            } else if (penghasilanNetto <= 300000000) {
                pajak = 50000000 * 0.05 + (penghasilanNetto - 50000000) * 0.1;
            } else {
                pajak = 50000000 * 0.05 + 250000000 * 0.1 + (penghasilanNetto - 300000000) * 0.15;
            }
            return pajak / 12;
        }

        private double getPTKP(int simbol) {
            double ptkp = 0;
            switch (simbol) {
                case 0:
                    ptkp = TK;
                    break;
                case 1:
                    ptkp = K0;
                    break;
                case 2:
                    ptkp = K1;
                    break;
            }
            return ptkp;
        }
    }

    // VietnamHitungPajak
    static class VietnamHitungPajak implements HitungPajak {
        private static final double TK = 15000000;
        private static final double K0_2 = 30000000;

        @Override
        public double hitungNetIncome(double totalEarnings, double totalDeductions, int simbol) {
            double ptkp = getPTKP(simbol);
            return (totalEarnings * 12) - (totalDeductions * 12) - ptkp;
        }

        @Override
        public double hitungPajak(double penghasilanNetto) {
            double pajak = 0;
            if (penghasilanNetto <= 50000000) {
                pajak = penghasilanNetto * 0.025;
            } else {
                pajak = 50000000 * 0.025 + (penghasilanNetto - 50000000) * 0.075;
            }
            return pajak / 12;
        }

        private double getPTKP(int simbol) {
            double ptkp = 0;
            switch (simbol) {
                case 0:
                    ptkp = TK;
                    break;
                case 1:
                case 2:
                    ptkp = K0_2;
                    break;
            }
            return ptkp;
        }
    }

    // HitungPajakFactory
    static class HitungPajakFactory {
        public static HitungPajak createHitungPajak(String country) {
            switch (country.toLowerCase()) {
                case "indonesia":
                    return new IndonesiaHitungPajak();
                case "vietnam":
                    return new VietnamHitungPajak();
                default:
                    throw new IllegalArgumentException("Tidak Ditemukan " + country);
            }
        }
    }
}
