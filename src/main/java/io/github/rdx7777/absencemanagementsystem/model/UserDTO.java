package io.github.rdx7777.absencemanagementsystem.model;

import java.util.Objects;

public class UserDTO {

    private final Long id;
    private final String name;
    private final String surname;
    private final String email;
    private final String jobTitle;
    private final Boolean isActive;
    private final Position position;
    private final String role;

    private UserDTO(Builder builder) {
        id = builder.id;
        name = builder.name;
        surname = builder.surname;
        email = builder.email;
        jobTitle = builder.jobTitle;
        isActive = builder.isActive;
        position = builder.position;
        role = builder.role;
    }

    public static UserDTO.Builder builder() {
        return new UserDTO.Builder();
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
        if (!(o instanceof UserDTO)) return false;
        UserDTO user = (UserDTO) o;
        return Objects.equals(id, user.id) &&
            Objects.equals(name, user.name) &&
            Objects.equals(surname, user.surname) &&
            Objects.equals(email, user.email) &&
            Objects.equals(jobTitle, user.jobTitle) &&
            Objects.equals(isActive, user.isActive) &&
            position == user.position &&
            Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, jobTitle, isActive, position, role);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", email='" + email + '\'' +
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
        private String jobTitle;
        private Boolean isActive;
        private Position position;
        private String role;

        public Builder withUser(UserDTO user) {
            this.id = user.getId();
            this.name = user.getName();
            this.surname= user.getSurname();
            this.email = user.getEmail();
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

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
