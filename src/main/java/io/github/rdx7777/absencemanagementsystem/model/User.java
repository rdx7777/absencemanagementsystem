package io.github.rdx7777.absencemanagementsystem.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Primary;

@Entity
@Primary
@Table(name = "users")
public class User {

    @Id
//    @GeneratedValue(generator = "inc")
//    @GenericGenerator(name = "inc", strategy = "increment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    private final String name;
    private final String surname;
    private final String email;
    private final String password;
    private final String jobTitle;
    private final Boolean isActive;
    private final Position position;
    private final String role;

    /**
     * for JPA (Hibernate)
     */
    @SuppressWarnings("unused")
    private User() {
        id = null;
        name = null;
        surname = null;
        email = null;
        password = null;
        jobTitle = null;
        isActive = null;
        position = null;
        role = null;
    }

    private User(Builder builder) {
        id = builder.id;
        name = builder.name;
        surname = builder.surname;
        email = builder.email;
        password = builder.password;
        jobTitle = builder.jobTitle;
        isActive = builder.isActive;
        position = builder.position;
        role = builder.role;
    }

    public static User.Builder builder() {
        return new User.Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Position getPosition() {
        return position;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
            Objects.equals(name, user.name) &&
            Objects.equals(surname, user.surname) &&
            Objects.equals(email, user.email) &&
            Objects.equals(password, user.password) &&
            Objects.equals(jobTitle, user.jobTitle) &&
            Objects.equals(isActive, user.isActive) &&
            position == user.position &&
            Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, password, jobTitle, isActive, position, role);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", jobTitle='" + jobTitle + '\'' +
            ", isActive=" + isActive +
            ", position=" + position +
            ", role='" + role + '\'' +
            '}';
    }

    public static class Builder {
        private Long id;
        private String name;
        private String surname;
        private String email;
        private String password;
        private String jobTitle;
        private Boolean isActive;
        private Position position;
        private String role;

        public Builder withUser(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.surname= user.getSurname();
            this.email = user.getEmail();
            this.password = user.getPassword();
            this.jobTitle = user.getJobTitle();
            this.isActive = user.getIsActive();
            this.position = user.getPosition();
            this.role = user.getRole();
            return this;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }

        public Builder withIsActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder withPosition(Position position) {
            this.position = position;
            return this;
        }

        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
