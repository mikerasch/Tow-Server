package edu.uwp.appfactory.tow.WebSecurityConfig.models;

import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "roles")
public class Role {
    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     *
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    /**
     *
     */
    public Role() {

    }

    /**
     *
     * @param name
     */
    public Role(ERole name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public ERole getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(ERole name) {
        this.name = name;
    }
}