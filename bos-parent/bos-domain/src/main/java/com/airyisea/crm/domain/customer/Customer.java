package com.airyisea.crm.domain.customer;
// Generated 2016-12-29 20:44:52 by Hibernate Tools 3.2.2.GA


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Customer generated by hbm2java
 */
@Entity
@Table(name="CUSTOMERS"
    ,schema="CRM"
)
@XmlRootElement(name="Customer")
public class Customer  implements java.io.Serializable {
	
     @Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", telephone="
				+ telephone + ", address=" + address + ", station=" + station
				+ ", decidedzoneId=" + decidedzoneId + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7817131101132015856L;
	private int id;
    private String name;
    private String telephone;
    private String address;
    private String station;
    private String decidedzoneId;

    public Customer() {
    }

	
    public Customer(int id, String name, String telephone, String address) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.address = address;
    }
    public Customer(int id, String name, String telephone, String address, String station, String decidedzoneId) {
       this.id = id;
       this.name = name;
       this.telephone = telephone;
       this.address = address;
       this.station = station;
       this.decidedzoneId = decidedzoneId;
    }
   
     @Id 
    
    @Column(name="ID", unique=true, nullable=false, precision=8, scale=0)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="NAME", nullable=false, length=20)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="TELEPHONE", nullable=false, length=11)
    public String getTelephone() {
        return this.telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    @Column(name="ADDRESS", nullable=false)
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    @Column(name="STATION")
    public String getStation() {
        return this.station;
    }
    
    public void setStation(String station) {
        this.station = station;
    }
    
    @Column(name="DECIDEDZONE_ID", length=32)
    public String getDecidedzoneId() {
        return this.decidedzoneId;
    }
    
    public void setDecidedzoneId(String decidedzoneId) {
        this.decidedzoneId = decidedzoneId;
    }




}


