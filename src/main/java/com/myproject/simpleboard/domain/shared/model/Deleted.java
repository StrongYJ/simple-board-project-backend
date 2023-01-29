package com.myproject.simpleboard.domain.shared.model;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter(AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Deleted {
    
    private boolean isDeleted;
    private LocalDateTime deleteDT;

    public Deleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
        this.deleteDT = isDeleted == true ? LocalDateTime.now() : null;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isDeleted ? 1231 : 1237);
        result = prime * result + ((deleteDT == null) ? 0 : deleteDT.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Deleted other = (Deleted) obj;
        if (isDeleted != other.isDeleted)
            return false;
        if (deleteDT == null) {
            if (other.deleteDT != null)
                return false;
        } else if (!deleteDT.equals(other.deleteDT))
            return false;
        return true;
    }

    
    
}
