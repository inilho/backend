package com.teskerja.backendenginer.Model.Repository;

import java.util.List;

import com.teskerja.backendenginer.Model.Entity.Component;
import com.teskerja.backendenginer.Model.Entity.Employee;

public class SalaryRequest {
    private Employee employee;
    private List<Component> komponengaji;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Component> getKomponengaji() {
        return komponengaji;
    }

    public void setKomponengaji(List<Component> komponengaji) {
        this.komponengaji = komponengaji;
    }

    public SalaryRequest(Employee employee, List<Component> komponengaji) {
        this.employee = employee;
        this.komponengaji = komponengaji;
    }

}
