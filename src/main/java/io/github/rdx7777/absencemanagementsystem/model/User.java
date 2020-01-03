package io.github.rdx7777.absencemanagementsystem.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
//    @GeneratedValue(generator = "inc")
//    @GenericGenerator(name = "inc", strategy = "increment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String email;
    private String password;
    private String jobTitle;
    private Boolean isActive;
    private Position position;

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
            position == user.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, password, jobTitle, isActive, position);
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
            ", getIsActive=" + isActive +
            ", position=" + position +
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

        public Builder withUser(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.surname= user.getSurname();
            this.email = user.getEmail();
            this.password = user.getPassword();
            this.jobTitle = user.getJobTitle();
            this.isActive = user.getIsActive();
            this.position = user.getPosition();
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

        public User build() {
            return new User(this);
        }
    }
}
