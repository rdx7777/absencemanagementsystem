package io.github.rdx7777.absencemanagementsystem.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDate;
import java.util.Objects;

@JsonDeserialize(builder = AbsenceCaseDTO.Builder.class)
public class AbsenceCaseDTO {

    private final Long id;
    private final UserDTO user;
    private final UserDTO headTeacher;
    private final LocalDate createdDate;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final PartDayType partDayType;
    private final String absenceReason;
    private final String userComment;
    private final Boolean isCoverRequired;
    private final Boolean isCoverProvided;
    private final String coverSupervisorComment;
    private final Boolean isApprovedByHeadTeacher;
    private final Boolean isAbsencePaid;
    private final String headTeacherComment;
    private final String hrSupervisorComment;
    private final Boolean isCaseResolved;
    private final LocalDate resolvedDate;

    private AbsenceCaseDTO(Builder builder) {
        id = builder.id;
        user = builder.user;
        headTeacher = builder.headTeacher;
        createdDate = builder.createdDate;
        startDate = builder.startDate;
        endDate = builder.endDate;
        partDayType = builder.partDayType;
        absenceReason = builder.absenceReason;
        userComment = builder.userComment;
        isCoverRequired = builder.isCoverRequired;
        isCoverProvided = builder.isCoverProvided;
        coverSupervisorComment = builder.coverSupervisorComment;
        isApprovedByHeadTeacher = builder.isApprovedByHeadTeacher;
        isAbsencePaid = builder.isAbsencePaid;
        headTeacherComment = builder.headTeacherComment;
        hrSupervisorComment = builder.hrSupervisorComment;
        isCaseResolved = builder.isCaseResolved;
        resolvedDate = builder.resolvedDate;
    }

    public static AbsenceCaseDTO.Builder builder() {
        return new AbsenceCaseDTO.Builder();
    }

    public Long getId() {
        return id;
    }

    public UserDTO getUser() {
        return user;
    }

    public UserDTO getHeadTeacher() {
        return headTeacher;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PartDayType getPartDayType() {
        return partDayType;
    }

    public String getAbsenceReason() {
        return absenceReason;
    }

    public String getUserComment() {
        return userComment;
    }

    public Boolean getIsCoverRequired() {
        return isCoverRequired;
    }

    public Boolean getIsCoverProvided() {
        return isCoverProvided;
    }

    public String getCoverSupervisorComment() {
        return coverSupervisorComment;
    }

    public Boolean getIsApprovedByHeadTeacher() {
        return isApprovedByHeadTeacher;
    }

    public Boolean getIsAbsencePaid() {
        return isAbsencePaid;
    }

    public String getHeadTeacherComment() {
        return headTeacherComment;
    }

    public String getHrSupervisorComment() {
        return hrSupervisorComment;
    }

    public Boolean getIsCaseResolved() {
        return isCaseResolved;
    }

    public LocalDate getResolvedDate() {
        return resolvedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbsenceCaseDTO)) return false;
        AbsenceCaseDTO aCase = (AbsenceCaseDTO) o;
        return Objects.equals(id, aCase.id) &&
            Objects.equals(user, aCase.user) &&
            Objects.equals(headTeacher, aCase.headTeacher) &&
            Objects.equals(createdDate, aCase.createdDate) &&
            Objects.equals(startDate, aCase.startDate) &&
            Objects.equals(endDate, aCase.endDate) &&
            partDayType == aCase.partDayType &&
            Objects.equals(absenceReason, aCase.absenceReason) &&
            Objects.equals(userComment, aCase.userComment) &&
            Objects.equals(isCoverRequired, aCase.isCoverRequired) &&
            Objects.equals(isCoverProvided, aCase.isCoverProvided) &&
            Objects.equals(coverSupervisorComment, aCase.coverSupervisorComment) &&
            Objects.equals(isApprovedByHeadTeacher, aCase.isApprovedByHeadTeacher) &&
            Objects.equals(isAbsencePaid, aCase.isAbsencePaid) &&
            Objects.equals(headTeacherComment, aCase.headTeacherComment) &&
            Objects.equals(hrSupervisorComment, aCase.hrSupervisorComment) &&
            Objects.equals(isCaseResolved, aCase.isCaseResolved) &&
            Objects.equals(resolvedDate, aCase.resolvedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, headTeacher, createdDate, startDate, endDate, partDayType, absenceReason, userComment, isCoverRequired, isCoverProvided, coverSupervisorComment, isApprovedByHeadTeacher, isAbsencePaid, headTeacherComment, hrSupervisorComment, isCaseResolved, resolvedDate);
    }

    @Override
    public String toString() {
        return "AbsenceCase{" +
            "id=" + id +
            ", userId=" + user +
            ", headTeacherId=" + headTeacher +
            ", createdDate=" + createdDate +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", partDayType=" + partDayType +
            ", absenceReason='" + absenceReason + '\'' +
            ", userComment='" + userComment + '\'' +
            ", isCoverRequired=" + isCoverRequired +
            ", isCoverProvided=" + isCoverProvided +
            ", coverSupervisorComment='" + coverSupervisorComment + '\'' +
            ", isApprovedByHeadTeacher=" + isApprovedByHeadTeacher +
            ", isAbsencePaid=" + isAbsencePaid +
            ", headTeacherComment='" + headTeacherComment + '\'' +
            ", hrSupervisorComment='" + hrSupervisorComment + '\'' +
            ", isCaseResolved=" + isCaseResolved +
            ", resolvedDate=" + resolvedDate +
            '}';
    }

    @JsonPOJOBuilder
    public static class Builder {

        private Long id;
        private UserDTO user;
        private UserDTO headTeacher;
        private LocalDate createdDate;
        private LocalDate startDate;
        private LocalDate endDate;
        private PartDayType partDayType;
        private String absenceReason;
        private String userComment;
        private Boolean isCoverRequired;
        private Boolean isCoverProvided;
        private String coverSupervisorComment;
        private Boolean isApprovedByHeadTeacher;
        private Boolean isAbsencePaid;
        private String headTeacherComment;
        private String hrSupervisorComment;
        private Boolean isCaseResolved;
        private LocalDate resolvedDate;

        public Builder withCase(AbsenceCaseDTO aCase) {
            this.id = aCase.getId();
            this.user = aCase.getUser();
            this.headTeacher = aCase.getHeadTeacher();
            this.createdDate = aCase.getCreatedDate();
            this.startDate = aCase.getStartDate();
            this.endDate = aCase.getEndDate();
            this.partDayType = aCase.getPartDayType();
            this.absenceReason = aCase.getAbsenceReason();
            this.userComment = aCase.getUserComment();
            this.isCoverRequired = aCase.getIsCoverRequired();
            this.isCoverProvided = aCase.getIsCoverProvided();
            this.coverSupervisorComment = aCase.getCoverSupervisorComment();
            this.isApprovedByHeadTeacher = aCase.getIsApprovedByHeadTeacher();
            this.isAbsencePaid = aCase.getIsAbsencePaid();
            this.headTeacherComment = aCase.getHeadTeacherComment();
            this.hrSupervisorComment = aCase.getHrSupervisorComment();
            this.isCaseResolved = aCase.getIsCaseResolved();
            this.resolvedDate = aCase.getResolvedDate();
            return this;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withUser(UserDTO user) {
            this.user = user;
            return this;
        }

        public Builder withHeadTeacher(UserDTO headTeacher) {
            this.headTeacher = headTeacher;
            return this;
        }

        public Builder withCreatedDate(LocalDate createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder withPartDayType(PartDayType partDayType) {
            this.partDayType = partDayType;
            return this;
        }

        public Builder withAbsenceReason(String absenceReason) {
            this.absenceReason = absenceReason;
            return this;
        }

        public Builder withUserComment(String userComment) {
            this.userComment = userComment;
            return this;
        }

        public Builder withIsCoverRequired(Boolean isCoverRequired) {
            this.isCoverRequired = isCoverRequired;
            return this;
        }

        public Builder withIsCoverProvided(Boolean isCoverProvided) {
            this.isCoverProvided = isCoverProvided;
            return this;
        }

        public Builder withCoverSupervisorComment(String coverSupervisorComment) {
            this.coverSupervisorComment = coverSupervisorComment;
            return this;
        }

        public Builder withIsApprovedByHeadTeacher(Boolean isApprovedByHeadTeacher) {
            this.isApprovedByHeadTeacher = isApprovedByHeadTeacher;
            return this;
        }

        public Builder withIsAbsencePaid(Boolean isAbsencePaid) {
            this.isAbsencePaid = isAbsencePaid;
            return this;
        }

        public Builder withHeadTeacherComment(String headTeacherComment) {
            this.headTeacherComment = headTeacherComment;
            return this;
        }

        public Builder withHrSupervisorComment(String hrSupervisorComment) {
            this.hrSupervisorComment = hrSupervisorComment;
            return this;
        }

        public Builder withIsCaseResolved(Boolean isCaseResolved) {
            this.isCaseResolved = isCaseResolved;
            return this;
        }

        public Builder withResolvedDate(LocalDate resolvedDate) {
            this.resolvedDate = resolvedDate;
            return this;
        }

        public AbsenceCaseDTO build() {
            return new AbsenceCaseDTO(this);
        }
    }
}
