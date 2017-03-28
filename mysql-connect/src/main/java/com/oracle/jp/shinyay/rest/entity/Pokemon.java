package com.oracle.jp.shinyay.rest.entity;

import javax.persistence.*;

@Entity
@Table(name="pokemon", schema = "mydatabase")
public class Pokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name_j")
    private String nameJ;
    @Column(name = "name_e")
    private String nameE;
    @Column
    private Integer hp;
    @Column
    private Integer atk;
    @Column
    private Integer def;
    @Column
    private Integer sat;
    @Column
    private Integer sde;
    @Column
    private Integer agl;
    @Column
    private Integer total;
    @Column
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameJ() {
        return nameJ;
    }

    public void setNameJ(String nameJ) {
        this.nameJ = nameJ;
    }

    public String getNameE() {
        return nameE;
    }

    public void setNameE(String nameE) {
        this.nameE = nameE;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getAtk() {
        return atk;
    }

    public void setAtk(Integer atk) {
        this.atk = atk;
    }

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
    }

    public Integer getSat() {
        return sat;
    }

    public void setSat(Integer sat) {
        this.sat = sat;
    }

    public Integer getSde() {
        return sde;
    }

    public void setSde(Integer sde) {
        this.sde = sde;
    }

    public Integer getAgl() {
        return agl;
    }

    public void setAgl(Integer agl) {
        this.agl = agl;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", nameJ='" + nameJ + '\'' +
                ", nemeE='" + nameE + '\'' +
                ", hp=" + hp +
                ", atk=" + atk +
                ", def=" + def +
                ", sat=" + sat +
                ", sde=" + sde +
                ", agl=" + agl +
                ", total=" + total +
                ", type='" + type + '\'' +
                '}';
    }
}
