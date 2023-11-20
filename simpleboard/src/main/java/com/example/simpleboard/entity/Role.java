package com.example.simpleboard.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.example.simpleboard.entity.utils.BaseUtcTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
public class Role extends BaseUtcTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("''")
    @Builder.Default
    private String name="";

    @Column(nullable=false)
    @ColumnDefault("''")
    @Builder.Default
    private String descRole = "";

    @ManyToMany(
        cascade = {
            CascadeType.PERSIST, CascadeType.MERGE
        }
        , fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "role_privilege"
        , joinColumns = {
            @JoinColumn(
                referencedColumnName = "id", name="role_id"
                , foreignKey = @ForeignKey(name="fk_roles_privileges_role_id")
            )
        }
        , inverseJoinColumns = {
            @JoinColumn(
                referencedColumnName = "id", name = "privilege_id"
                , foreignKey = @ForeignKey(name="fk_roles_privileges_privilege_id")
            )
        }
    )
    @Builder.Default
    private Set<Privilege> privileges = new HashSet<Privilege>();

    @ManyToMany(mappedBy = "roles")
    @Builder.Default
    @ToString.Exclude
    private Set<Member> members = new HashSet<Member>();

    @Override
    public boolean equals(Object other)
    {
        if(this == other)
            return true;
        if(other == null || other.getClass() != this.getClass())
            return false;
        return Objects.equals(((Role)other).name, this.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.name);
    }

    public void addPrivilege(Privilege privilege)
    {
        this.privileges.add(privilege);
        privilege.getRoles().add(this);
    }

    public void removePrivilege(Privilege privilege)
    {
        this.privileges.remove(privilege);
        privilege.getRoles().remove(this);
    }

    public void updateRole(Role other)
    {
        if(other == null)
        {
            return ;
        }
    
        if(other.getName() != null)
        {
            this.name = other.getName();
        }
        if(other.getDescRole() != null)
        {
            this.descRole = other.getDescRole();
        }

        if(other.getPrivileges() != null)
        {
            this.privileges.clear();
            this.privileges.addAll(other.getPrivileges());
        }
            
    }
}
