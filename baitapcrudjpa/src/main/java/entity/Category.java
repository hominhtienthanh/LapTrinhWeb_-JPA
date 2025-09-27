package entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
@NamedQuery(name = "Category.findAll", query = "SELECT o FROM Category o")
public class Category implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "categoryname", columnDefinition = "NVARCHAR(255)")
    private String categoryname;

    @Column(name = "icon", length = 255)
    private String icon;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    public Category() {}

    public Category(int id, String categoryname, String icon, User user) {
        this.id = id;
        this.categoryname = categoryname;
        this.icon = icon;
        this.user = user;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }
    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
