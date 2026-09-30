package vn.iotstar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(nullable = false) private Integer id;
    @Column(nullable = false, columnDefinition = "nvarchar(50)") private String fullName;
    @Column(unique = true, length = 100, nullable = false) private String email;
    @Column(columnDefinition = "nvarchar(500)") private String images;
    @JsonIgnore @Column(nullable = false) private String password;
    @CreationTimestamp @Column(updatable = false, name = "created_at") private Date createdAt;
    @UpdateTimestamp @Column(name = "updated_at") private Date updatedAt;
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getFullName() { return fullName; } public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getImages() { return images; } public void setImages(String images) { this.images = images; }
    @Override public String getPassword() { return password; } public void setPassword(String password) { this.password = password; }
    public Date getCreatedAt() { return createdAt; } public Date getUpdatedAt() { return updatedAt; }
    @Override public String getUsername() { return email; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }
}
