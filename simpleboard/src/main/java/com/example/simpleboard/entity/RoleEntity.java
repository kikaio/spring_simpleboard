import java.math.BigInteger;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
    name="role"
    , indexes = {
        @Index(unique = true, name = "idx_role", columnList = "role")
    }
)
public class RoleEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Nonnull
    private String role;
}
