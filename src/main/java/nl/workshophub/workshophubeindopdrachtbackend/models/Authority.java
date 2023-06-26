package nl.workshophub.workshophubeindopdrachtbackend.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@Entity
@IdClass(AuthorityKey.class)
@Table(name = "authorities")
public class Authority implements Serializable {

    @Id
    @Column(nullable = false)
    private Long userId;

    @Id
    @Column(nullable = false)
    private String authority;

    public Authority() {
    }

    public Authority(Long userId, String authority) {
        this.userId = userId;
        this.authority = authority;
    }
}
