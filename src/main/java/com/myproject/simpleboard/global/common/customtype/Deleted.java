package com.myproject.simpleboard.global.common.customtype;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class Deleted {
    
    private Boolean isDeleted;
    private LocalDateTime deleteDT;
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((isDeleted == null) ? 0 : isDeleted.hashCode());
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
        if (isDeleted == null) {
            if (other.isDeleted != null)
                return false;
        } else if (!isDeleted.equals(other.isDeleted))
            return false;
        if (deleteDT == null) {
            if (other.deleteDT != null)
                return false;
        } else if (!deleteDT.equals(other.deleteDT))
            return false;
        return true;
    }

    
}
