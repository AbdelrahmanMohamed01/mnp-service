package com.example.mnp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "operators")
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String prefixStart;
    private String prefixEnd;

    public Operator() {
    }
    public Operator(String name, String code, String prefixStart, String prefixEnd) {
        this.name = name;
        this.code = code;
        this.prefixStart = prefixStart;
        this.prefixEnd = prefixEnd;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrefixStart() {
        return prefixStart;
    }

    public void setPrefixStart(String prefixStart) {
        this.prefixStart = prefixStart;
    }

    public String getPrefixEnd() {
        return prefixEnd;
    }

    public void setPrefixEnd(String prefixEnd) {
        this.prefixEnd = prefixEnd;
    }
}
