package br.com.vinnilmg.quarkussocial.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull Integer getAge() {
        return age;
    }

    public void setAge(@NotNull Integer age) {
        this.age = age;
    }
}
