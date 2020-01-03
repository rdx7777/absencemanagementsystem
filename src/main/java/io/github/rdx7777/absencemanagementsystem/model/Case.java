package io.github.rdx7777.absencemanagementsystem.model;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Case {

    @Id
//    @GeneratedValue(generator = "inc")
//    @GenericGenerator(name = "inc", strategy = "increment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long headTeacherId;
    private LocalDate startDate;
    private LocalDate endDate;
    private PartDayType type;
    private String absenceReason;
    private String userComment; // option, must not be validated
    private Boolean isCoverRequired;
    private Boolean isCoverProvided;
    private String coverSupervisorComment; // option, must not be validated
    private Boolean isApprovedByHeadTeacher;
    private Boolean isAbsencePaid;
    private String headTeacherComment; // option, must not be validated
    private String hrSupervisorComment; // option, must not be validated
    private Boolean isCaseResolved;

    @SuppressWarnings("unused")
    private Case() {
        id = null;
        userId = null;
        headTeacherId = null;
        startDate = null;
        endDate = null;
        type = null;
        absenceReason = null;
        userComment = null;
        isCoverRequired = null;
        isCoverProvided = null;
        coverSupervisorComment = null;
        isApprovedByHeadTeacher = null;
        isAbsencePaid = null;
        headTeacherComment = null;
        hrSupervisorComment = null;
        isCaseResolved = null;
    }

    private Case(Case.Builder builder) {
        id = builder.id;
        userId = builder.userId;
        headTeacherId = builder.headTeacherId;
        startDate = builder.startDate;
        endDate = builder.endDate;
        type = builder.type;
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
    }

    public static Case.Builder builder() {
        return new Case.Builder();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getHeadTeacherId() {
        return headTeacherId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PartDayType getPartDayType() {
        return type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Case)) return false;
        Case aCase = (Case) o;
        return Objects.equals(id, aCase.id) &&
            Objects.equals(userId, aCase.userId) &&
            Objects.equals(headTeacherId, aCase.headTeacherId) &&
            Objects.equals(startDate, aCase.startDate) &&
            Objects.equals(endDate, aCase.endDate) &&
            type == aCase.type &&
            Objects.equals(absenceReason, aCase.absenceReason) &&
            Objects.equals(userComment, aCase.userComment) &&
            Objects.equals(isCoverRequired, aCase.isCoverRequired) &&
            Objects.equals(isCoverProvided, aCase.isCoverProvided) &&
            Objects.equals(coverSupervisorComment, aCase.coverSupervisorComment) &&
            Objects.equals(isApprovedByHeadTeacher, aCase.isApprovedByHeadTeacher) &&
            Objects.equals(isAbsencePaid, aCase.isAbsencePaid) &&
            Objects.equals(headTeacherComment, aCase.headTeacherComment) &&
            Objects.equals(hrSupervisorComment, aCase.hrSupervisorComment) &&
            Objects.equals(isCaseResolved, aCase.isCaseResolved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, headTeacherId, startDate, endDate, type, absenceReason, userComment, isCoverRequired, isCoverProvided, coverSupervisorComment, isApprovedByHeadTeacher, isAbsencePaid, headTeacherComment, hrSupervisorComment, isCaseResolved);
    }

    @Override
    public String toString() {
        return "Case{" +
            "id=" + id +
            ", userId=" + userId +
            ", headTeacherId=" + headTeacherId +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", type=" + type +
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
            '}';
    }

    public static class Builder {

        private Long id;
        private Long userId;
        private Long headTeacherId;
        private LocalDate startDate;
        private LocalDate endDate;
        private PartDayType type;
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

        public Builder withCase(Case aCase) {
            this.id = aCase.getId();
            this.userId = aCase.getUserId();
            this.headTeacherId = aCase.getHeadTeacherId();
            this.startDate = aCase.getStartDate();
            this.endDate = aCase.getEndDate();
            this.type = aCase.getPartDayType();
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
            return this;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withHeadTeacherId(Long headTeacherId) {
            this.headTeacherId = headTeacherId;
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

        public Builder withPartDayType(PartDayType type) {
            this.type = type;
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

        public Case build() {
            return new Case(this);
        }
    }
}
