package com.myproject.simpleboard.member.customtype;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class MemberStatus {
    
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime statusUpdateDT;
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((statusUpdateDT == null) ? 0 : statusUpdateDT.hashCode());
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
        MemberStatus other = (MemberStatus) obj;
        if (status != other.status)
            return false;
        if (statusUpdateDT == null) {
            if (other.statusUpdateDT != null)
                return false;
        } else if (!statusUpdateDT.equals(other.statusUpdateDT))
            return false;
        return true;
    }

    
}
