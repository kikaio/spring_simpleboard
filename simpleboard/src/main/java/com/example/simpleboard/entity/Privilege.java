package com.example.simpleboard.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.example.simpleboard.entity.utils.BaseUtcTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@DynamicInsert
public class Privilege  extends BaseUtcTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @ColumnDefault("''")
    @Builder.Default
    private String name = "";

    @ManyToMany(mappedBy = "privileges")
    @Builder.Default
    @ToString.Exclude
    private final Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    @ColumnDefault("''")
    private String descPrivilige = "";

    @Override
    public boolean equals(Object other)
    {
        if(this == other)
            return true;
        if(other == null || other.getClass() != getClass())
            return false;
        return Objects.equals(((Privilege)other).name, this.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.name);
    }

    public void updatePrivilege(Privilege other)
    {
        updateOnlyPrivilege(other);
        updateOnlyRole(other);
        return ;
    }

    public void updateOnlyPrivilege(Privilege other)
    {
        if(other == null)
        {
            return ;
        }
        if(other.getName() != null)
        {
            this.name = other.getName();
        }
        if(other.getDescPrivilige() != null)
        {
            this.descPrivilige = other.getDescPrivilige();
        }
    }

    public void updateOnlyRole(Privilege other)
    {
        if(other == null)
        {
            return ;
        }
        this.roles.clear();
        this.roles.addAll(other.getRoles());
    }
}
