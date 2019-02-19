package br.com.andrewribeiro.ribrest.core.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author Andrew Ribeiro
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractModel implements Model, Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String currentStatus;
    
    private Timestamp dateReg;

    public AbstractModel() {}
    public AbstractModel(Long id){
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getCurrentStatus() {
        return currentStatus;
    }

    @Override
    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public Timestamp getDateReg() {
        return dateReg;
    }

    @Override
    public void setDateReg(Timestamp dateReg) {
        this.dateReg = dateReg;
    }
}
