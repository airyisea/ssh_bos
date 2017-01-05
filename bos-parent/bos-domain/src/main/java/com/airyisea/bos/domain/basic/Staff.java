package com.airyisea.bos.domain.basic;

import com.airyisea.bos.domain.qp.NoticeBill;
import com.airyisea.bos.domain.qp.WorkBill;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="bc_staff"
    ,catalog="bos"
)
public class Staff  implements java.io.Serializable {


     private String id;
     private String name;
     private String telephone;
     private Character haspda = '0';
     private Character deltag = '0';
     private String station;
     private String standard;
     private Set<DecidedZone> decidedZones = new HashSet<DecidedZone>(0);
     private Set<WorkBill> workBills = new HashSet<WorkBill>(0);
     private Set<NoticeBill> noticeBills = new HashSet<NoticeBill>(0);

    public Staff() {
    }

    public Staff(String name, String telephone, Character haspda, Character deltag, String station, String standard, Set<DecidedZone> decidedZones, Set<WorkBill> workBills, Set<NoticeBill> noticeBills) {
       this.name = name;
       this.telephone = telephone;
       this.haspda = haspda;
       this.deltag = deltag;
       this.station = station;
       this.standard = standard;
       this.decidedZones = decidedZones;
       this.workBills = workBills;
       this.noticeBills = noticeBills;
    }
   
     @GenericGenerator(name="generator", strategy="uuid")@Id @GeneratedValue(generator="generator")
    
    @Column(name="id", unique=true, nullable=false, length=32)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name="name", length=20)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="telephone", length=20)
    public String getTelephone() {
        return this.telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    @Column(name="haspda", length=1)
    public Character getHaspda() {
        return this.haspda;
    }
    
    public void setHaspda(Character haspda) {
        this.haspda = haspda;
    }
    
    @Column(name="deltag", length=1)
    public Character getDeltag() {
        return this.deltag;
    }
    
    public void setDeltag(Character deltag) {
        this.deltag = deltag;
    }
    
    @Column(name="station", length=40)
    public String getStation() {
        return this.station;
    }
    
    public void setStation(String station) {
        this.station = station;
    }
    
    @Column(name="standard", length=100)
    public String getStandard() {
        return this.standard;
    }
    
    public void setStandard(String standard) {
        this.standard = standard;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="staff")
    public Set<DecidedZone> getDecidedZones() {
        return this.decidedZones;
    }
    
    public void setDecidedZones(Set<DecidedZone> decidedZones) {
        this.decidedZones = decidedZones;
    }
    @JSON(serialize=false)
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="staff")
    public Set<WorkBill> getWorkBills() {
        return this.workBills;
    }
    
    public void setWorkBills(Set<WorkBill> workBills) {
        this.workBills = workBills;
    }
    @JSON(serialize=false)
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="staff")
    public Set<NoticeBill> getNoticeBills() {
        return this.noticeBills;
    }
    
    public void setNoticeBills(Set<NoticeBill> noticeBills) {
        this.noticeBills = noticeBills;
    }




}


